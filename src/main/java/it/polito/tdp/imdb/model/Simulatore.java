package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;
import it.polito.tdp.imdb.model.Giorno.GType;

public class Simulatore {
	
	// PARAMETRI DI SIMULAZIONE
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int ng;
	
	public Simulatore(Graph<Actor, DefaultWeightedEdge> grafo, int ng) {
		this.grafo = grafo;
		this.ng = ng;
	}
	
	// OUTPUT DA CALCOLARE
	private int numPause;
	private List<Actor> intervistati;
	
	public int getNumPause() {
		return numPause;
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}
		
	// STATO DI SISTEMA
	private int g;
	private List<Giorno> giorni;
	private List<Actor> intervistabili;
	private Random prob = new Random();

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	// INIZIALIZZAZIONE

	public void init() {
		
		g = 1;
		giorni = new ArrayList<Giorno>();
		
		queue = new PriorityQueue<Event>();
		
		intervistati = new ArrayList<Actor>();
		intervistabili = new ArrayList<Actor>(grafo.vertexSet());
		
		Actor partenza = intervistabili.get(prob.nextInt(intervistabili.size()));
		
		intervistabili.remove(partenza);
		intervistati.add(partenza);
		
		if(partenza.getGender().equals("M")) {
			giorni.add(new Giorno(g, GType.INTERVISTA_M));
		}else {
			giorni.add(new Giorno(g, GType.INTERVISTA_D));
		}
		
		queue.add(new Event(EventType.INTERVISTA, partenza , g));
	}

	// ESECUZIONE
	public void run() {
		
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		
		switch(e.getType()) {
		case INTERVISTA:
			
			g++;
			if(g>ng) {
				break;
			}
			
			if(giorni.size()>1) {
				if(giorni.get(giorni.size()-1).getType().equals(giorni.get(giorni.size()-2).getType()) && prob.nextFloat()<=0.9) {
					queue.add(new Event(EventType.PAUSA, null, g));
					numPause++;
					giorni.add(new Giorno(g, GType.PAUSA));
					break;
				}
			}
			
			if(prob.nextFloat()<=0.6 || Graphs.neighborListOf(grafo, e.getAttore()).isEmpty()) {
				Actor nuovo = intervistabili.get(prob.nextInt(intervistabili.size()));
				
				intervistabili.remove(nuovo);
				intervistati.add(nuovo);
				
				queue.add(new Event(EventType.INTERVISTA, nuovo, g));
				
				if(nuovo.getGender().equals("M")) {
					giorni.add(new Giorno(g, GType.INTERVISTA_M));
				}else {
					giorni.add(new Giorno(g, GType.INTERVISTA_D));
				}
				
			}else {
				List<DefaultWeightedEdge> archi = new ArrayList<DefaultWeightedEdge>(grafo.edgesOf(e.getAttore()));
				List<Vicino> vicini = new ArrayList<Vicino>();
				
				for(DefaultWeightedEdge edge : archi) {
					if(!grafo.getEdgeSource(edge).equals(e.getAttore()))
						vicini.add(new Vicino(grafo.getEdgeSource(edge), (int)grafo.getEdgeWeight(edge)));
					else
						vicini.add(new Vicino(grafo.getEdgeTarget(edge), (int)grafo.getEdgeWeight(edge)));
				}
				
				Collections.sort(vicini);
				
				List<Vicino> papabili = new ArrayList<Vicino>();
				
				papabili.add(vicini.get(0));
				for(int i = 1; i<vicini.size(); i++) {
					if(vicini.get(i).getPeso()==papabili.get(0).getPeso()) {
						papabili.add(vicini.get(i));
					}
				}
				
				Actor nuovo = papabili.get(prob.nextInt(papabili.size())).getA();
				
				intervistabili.remove(nuovo);
				intervistati.add(nuovo);
				
				queue.add(new Event(EventType.INTERVISTA, nuovo, g));
				
				if(nuovo.getGender().equals("M")) {
					giorni.add(new Giorno(g, GType.INTERVISTA_M));
				}else {
					giorni.add(new Giorno(g, GType.INTERVISTA_D));
				}
			}
			
			break;
			
		case PAUSA:
			
			g++;
			
			if(g>ng) {
				break;
			}
			
			Actor nuovo = intervistabili.get(prob.nextInt(intervistabili.size()));
			
			intervistabili.remove(nuovo);
			intervistati.add(nuovo);
			
			queue.add(new Event(EventType.INTERVISTA, nuovo, g));
			
			if(nuovo.getGender().equals("M")) {
				giorni.add(new Giorno(g, GType.INTERVISTA_M));
			}else {
				giorni.add(new Giorno(g, GType.INTERVISTA_D));
			}
			
			break;
		}
	}
	
	
}
