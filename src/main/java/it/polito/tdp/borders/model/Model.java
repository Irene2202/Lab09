package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map<Integer, Country> idMap;
	private ConnectivityInspector<Country, DefaultEdge> connessioni;

	public Model() {
		dao=new BordersDAO();
		idMap=new HashMap<>();
		dao.loadAllCountries(idMap);
	}
	
	public void creaGrafo(int anno) {
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		
		//Aggiungo vertici
		//Devo prendere solo quelli che hanno un border con anno<=a quello in input
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, anno));
		
		//Aggiungo gli archi
		for(Border b:dao.getCountryPairs(idMap, anno)) {
			Graphs.addEdgeWithVertices(this.grafo, b.getState1(), b.getState2());
		}
		
		System.out.println("Grago creato");
		System.out.println("#Vertici: "+grafo.vertexSet().size());
		System.out.println("#Archi: "+grafo.edgeSet().size());
		
	}
	
	public SimpleGraph<Country, DefaultEdge> getGrafo(){
		return grafo;
	}
	
	public int getNumConnessioni() {
		connessioni=new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return connessioni.connectedSets().size();
	}

}
