package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map<Integer, Country> idMap;
	private ConnectivityInspector<Country, DefaultEdge> connessioni;
	private Map<Country, Country> visita;

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
	
	public List<Country> getStatiraggiungibili(Country c){
		List<Country> percorso=new ArrayList<>();
		
		BreadthFirstIterator<Country, DefaultEdge> it=new BreadthFirstIterator<>(grafo, c);
		
		visita=new HashMap<>();
		visita.put(c, null);
		
		it.addTraversalListener(new TraversalListener<Country, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				Country c1=grafo.getEdgeSource(e.getEdge());
				Country c2=grafo.getEdgeTarget(e.getEdge());
				
				if(visita.containsKey(c1) && !visita.containsKey(c2)) {
					visita.put(c2, c1); //arrivo in c2 da c1
				} else if (visita.containsKey(c2) && !visita.containsKey(c1)) {
					visita.put(c1, c2); //arrivo in c1 da c2
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			} 
		});
	
		while(it.hasNext()) {
			Country country=it.next();
			//System.out.println(country);
			percorso.add(country);
		}
		
		/*
		System.out.println("------*******-------");
		for(int i=0; i<percorso.size(); i++)
			System.out.println(percorso.get(i).getStateNme());
			*/
		
		return percorso;
	}

}
