package hirondelle.predict.pub.json;

import hirondelle.predict.main.lists.PredictionList;
import hirondelle.predict.main.lists.PredictionListDAO;
import hirondelle.predict.main.prediction.Prediction;
import hirondelle.predict.main.prediction.PredictionDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 Serve a given {@link PredictionList} in <a href='http://www.json.org/'>JSON</a> format.
<P>The data is escaped using {@link SafeText#getJsonSafe()}.
  
  <P>See the <tt>pub/JsonTest.jsp</tt> for an example of using javascript to call this 
  action, and render the data in a web page.
   
 @view view.json
*/
public final class ViewPublicListJsonAction  extends ActionImpl {

  /** Constructor. */
  public ViewPublicListJsonAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  public static final RequestParameter LIST_ID = RequestParameter.withLengthCheck("ListId");
  public static final SqlId LIST_PREDICTIONS =  new SqlId("LIST_PREDICTIONS");

  /**
   Fetch a {@link PredictionList}, its {@link Prediction}s, and its average score, and place them all in request scope. 
  */
  @Override public ResponsePage execute() throws AppException {
    HttpServletResponse response = getRequestParser().getResponse();
    //This setter does not 'stick' - need to set in JSP using a page directive
    response.setContentType("application/json");
    
    List<Prediction> predictions = fDAO.list(getIdParam(LIST_ID));
    addToRequest(ITEMS_FOR_LISTING, predictions);
    
    addToRequest("averageScore", Prediction.calculateAverageScore(predictions));
    
    PredictionList list = fetchPredictionList();
    addToRequest("list", list);
    
    return getResponsePage();
  }

  // PRIVATE 
  private PredictionDAO fDAO = new PredictionDAO();
  private static final Logger fLogger = Util.getLogger(ViewPublicListJsonAction.class);
  
  private PredictionList fetchPredictionList() throws DAOException {
    PredictionListDAO dao = new PredictionListDAO();
    PredictionList result = dao.fetchPublic(getIdParam(LIST_ID));
    fLogger.fine("Parent list: " + result);
    return result;
  }
  
  private static final ResponsePage FORWARD = new ResponsePage("view.json", ViewPublicListJsonAction.class);
}
