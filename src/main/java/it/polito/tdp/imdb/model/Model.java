package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	private Simulatore sim;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public List<String> getGeneri(){
		List<String> generi = dao.getGeneri();
		Collections.sort(generi);
		return generi;
	}
	
	public void creaGrafo(String genre) {
		
		grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap = new HashMap<Integer, Actor>();
		
		dao.listActorsGenres(genre, idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a : dao.listAdiacenze(genre, idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Actor> getVertici() {
		List<Actor> attori = new ArrayList<Actor>(grafo.vertexSet());
		Collections.sort(attori);
		return attori;
	}
	
	public List<Actor> getRaggiungibili(Actor attore){
		
		List<Actor> attori = new ArrayList<Actor>();
		
		BreadthFirstIterator<Actor, DefaultWeightedEdge> bfi = new BreadthFirstIterator<Actor, DefaultWeightedEdge>(grafo, attore);
		
		while(bfi.hasNext()) {
			attori.add(bfi.next());
		}
		
		attori.remove(0);
		
		Collections.sort(attori);
		
		return attori;
	}

	public void simulazione(int ng) {
		
		sim = new Simulatore(grafo, ng);
		sim.init();
		sim.run();
		
	}

	public int getNumPause() {
		return sim.getNumPause();
	}

	public List<Actor> getIntervistati() {
		return sim.getIntervistati();
	}
}
