package hirondelle.predict.pub.search;

import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DynamicCriteria;
import static hirondelle.web4j.database.DynamicCriteria.WHERE;
import static hirondelle.web4j.database.DynamicCriteria.AND;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.util.Util;
import static hirondelle.web4j.util.Consts.NEW_LINE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for returning {@link SearchResult}s for the given
 * {@link SearchCriteria}.
 * 
 * <P>
 * The input parameters from the user are date-only. For the purposes of this
 * SELECT, those dates are coerced into date-times, to match with the underlying
 * data type of the column.
 * 
 * <P>
 * For example, the from-date is coerced to the start-of-day, as in:
 * 
 * <PRE>
 * 2010-01-15 00:00:00.000000000
 * </PRE>
 * 
 * while the to-date is coerced to the end-of-day, as in:
 * 
 * <PRE>
 * 2010-01-15 23:59:59.999999999
 * </PRE>
 */
final class SearchDAO {

	/**
	 * Return search results.
	 * <P>
	 * This method uses {@link DynamicCriteria} to construct the SQL SELECT
	 * statement. The number of items returned is limited to 200. The search is
	 * not sensitive to case. Items are sorted by creation date, with the newest
	 * appearing first.
	 */
	List<SearchResult> listSearchResults(SearchCriteria aSearchCriteria)
			throws DAOException {
		List<SearchResult> result = new ArrayList<SearchResult>();
		int i=0;
		while (result.isEmpty()) {
			if (SearchStyle.ExactPhrase == aSearchCriteria.getSearchStyle()) {
				result = Db.search(SearchResult.class,
						SearchAction.SEARCH_FOR_EXACT_PHRASE,
						getCriteriaExactPhrase(aSearchCriteria),
						getParamsExactPhrase(aSearchCriteria));
			} else if (SearchStyle.AllOfTheWords == aSearchCriteria
					.getSearchStyle()) {
				String[] searchTerms = getSearchTerms(aSearchCriteria);
				result = Db.search(SearchResult.class,
						SearchAction.SEARCH_FOR_ALL_OF_THESE_WORDS,
						getCriteriaAllWords(aSearchCriteria, searchTerms),
						getParamsAllWords(aSearchCriteria, searchTerms));
			} else {
				throw new AssertionError("Unexpected SearchStyle: "
						+ aSearchCriteria);
			} if(i>20){
				break;
			}
			i++;
		}
		return result;
	}

	// PRIVATE

	private static final String TEXT_LIKE = "Prediction.Text LIKE ?" + NEW_LINE;
	private static final String AND_FROM_DATE = "AND Prediction.CreationDate >= ?"
			+ NEW_LINE;
	private static final String AND_TO_DATE = "AND Prediction.CreationDate <= ?"
			+ NEW_LINE;
	private static final String ORDER_BY_DATE_DESC = "ORDER BY Prediction.CreationDate DESC LIMIT 200"
			+ NEW_LINE;

	private static final Logger fLogger = Util.getLogger(SearchDAO.class);

	/** Return the criteria for the case of searching for an exact phrase. */
	private DynamicCriteria getCriteriaExactPhrase(
			SearchCriteria aSearchCriteria) {
		StringBuilder result = new StringBuilder(WHERE + TEXT_LIKE);
		if (aSearchCriteria.getFromDate() != null) {
			result.append(AND_FROM_DATE);
		}
		if (aSearchCriteria.getToDate() != null) {
			result.append(AND_TO_DATE);
		}
		result.append(ORDER_BY_DATE_DESC);
		fLogger.fine("Dynamic SQL criteria: " + result);
		return new DynamicCriteria(result);
	}

	private Object[] getParamsExactPhrase(SearchCriteria aSearchCriteria) {
		List<Object> result = new ArrayList<Object>();
		result.add(like(aSearchCriteria.getSearchText().getRawString()));
		if (aSearchCriteria.getFromDate() != null) {
			result.add(aSearchCriteria.getFromDate());
		}
		if (aSearchCriteria.getToDate() != null) {
			result.add(aSearchCriteria.getToDate());
		}
		return result.toArray();
	}

	/** Return the criteria for the case of searching for all words. */
	private DynamicCriteria getCriteriaAllWords(SearchCriteria aSearchCriteria,
			String[] aSearchTerms) {
		StringBuilder result = new StringBuilder(WHERE);
		int idx = 0;
		for (String searchTerm : aSearchTerms) {
			if (idx > 0) {
				result.append(AND);
			}
			result.append(TEXT_LIKE);
			++idx;
		}
		if (aSearchCriteria.getFromDate() != null) {
			result.append(AND_FROM_DATE);
		}
		if (aSearchCriteria.getToDate() != null) {
			result.append(AND_TO_DATE);
		}
		result.append(ORDER_BY_DATE_DESC);
		fLogger.fine("Dynamic SQL criteria: " + result);
		return new DynamicCriteria(result);
	}

	/** Search terms are separated by white space. */
	private String[] getSearchTerms(SearchCriteria aSearchCriteria) {
		String WHITESPACE = "\\s";
		String searchTerms = aSearchCriteria.getSearchText().getRawString();
		return searchTerms.split(WHITESPACE);
	}

	private Object[] getParamsAllWords(SearchCriteria aSearchCriteria,
			String[] aSearchTerms) {
		List<Object> result = new ArrayList<Object>();
		// common words are left in here; they are not removed
		for (String searchTerm : aSearchTerms) {
			result.add(like(searchTerm));
		}
		if (aSearchCriteria.getFromDate() != null) {
			result.add(aSearchCriteria.getFromDate());
		}
		if (aSearchCriteria.getToDate() != null) {
			result.add(aSearchCriteria.getToDate());
		}
		return result.toArray();
	}

	/** Surround by '%', for LIKE operator. */
	private Id like(String aSearchTerm) {
		return Id.from("%" + aSearchTerm + "%");
	}
}
