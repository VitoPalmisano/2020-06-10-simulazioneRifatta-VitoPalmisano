package it.polito.tdp.imdb.model;

public class Vicino implements Comparable<Vicino>{

	private Actor a;
	private int peso;
	public Vicino(Actor a, int peso) {
		super();
		this.a = a;
		this.peso = peso;
	}
	public Actor getA() {
		return a;
	}
	public int getPeso() {
		return peso;
	}
	@Override
	public int compareTo(Vicino o) {
		return o.getPeso()-this.peso;
	}
	
	
}
