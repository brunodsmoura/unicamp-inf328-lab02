package aspects;

import hirondelle.predict.pub.search.SearchCriteria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public aspect SearchDAOAspect {
	
	private Map<Long, String> executionTime = null;
	
	public SearchDAOAspect(){ 
		executionTime = new TreeMap<Long, String>(
				new Comparator<Long>() {
			@Override
			public int compare(Long one, Long another) {
				return one.compareTo(another) * -1;
			}
		}); 
	}

	pointcut interceptarBuscaTermos(SearchCriteria aSearchCriteria) :
		execution(* hirondelle.predict.pub.search.SearchDAO.getSearchTerms(..)) &&
		args(aSearchCriteria);
	
	after(SearchCriteria aSearchCriteria)
	returning(String[] resultado) : 
	interceptarBuscaTermos(aSearchCriteria) {
		if(resultado.length == 1) {
			System.out.println("\n\n>>> ASPECTO ATIVADO::. APENAS 1 TERMO CONSULTADO\n\n");
		}
		if(resultado.length > 1) {
			System.out.println("\n\n>>> ASPECTO ATIVADO::. MAIS DE 1 TERMO CONSULTADO\n\n");
		}		
	}
	
	pointcut trackingTempoMetodosDAO() : 
		execution( * *..*DAO.* (..) );
	
	Object around() : trackingTempoMetodosDAO() {
		String clazzAndMethod = thisJoinPoint.getSignature().getDeclaringTypeName() + ": " + thisJoinPoint.getSignature().getName();
		System.out.println("\n\n>>> ASPECTO ATIVADO::. " + clazzAndMethod + "\n\n");

		Long time = System.nanoTime();
		Object result = proceed();

		executionTime.put((System.nanoTime() - time), clazzAndMethod);

		Set<Long> keys = executionTime.keySet();
		List<String> clazzAndMethods = new ArrayList<String>();
		int counting = 0;

		System.out.println("\n\nTOP 3 MAIS LENTOS: ");

		for(Long key : keys){
			if(clazzAndMethods.contains(executionTime.get(key))){
				continue;
			}

			counting++;

			System.out.println(counting + " - " + executionTime.get(key) + " - " + key + " ms.");

			clazzAndMethods.add(executionTime.get(key));

			if(counting == 3) { 
				System.out.println(); 
				break; 
			}
		}

		return result;
	}
	
	pointcut clearMapOnLogoff() :
		execution(* hirondelle.predict.main.logoff.LogoffAction.execute());
	
	after() returning() : clearMapOnLogoff(){
		
		System.out.println("\n\n>>> ASPECTO ATIVADO::. Logoff, limpando a lista de tempo de execução\n\n");
		
		executionTime.clear();
	}

}