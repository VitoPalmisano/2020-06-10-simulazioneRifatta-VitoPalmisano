package it.polito.tdp.imdb.model;

public class Giorno {

	public enum GType{
		INTERVISTA_M,
		INTERVISTA_D,
		PAUSA,
	}
	
	private int g;
	private GType type;
	
	public Giorno(int g, GType type) {
		super();
		this.g = g;
		this.type = type;
	}
	public int getG() {
		return g;
	}
	public GType getType() {
		return type;
	}
	
	
}
