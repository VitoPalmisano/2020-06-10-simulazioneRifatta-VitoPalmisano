package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INTERVISTA,
		PAUSA,
	}
	
	private EventType type;
	private Actor attore;
	private int giorno;
	
	public Event(EventType type, Actor attore, int giorno) {
		super();
		this.type = type;
		this.attore = attore;
		this.giorno = giorno;
	}

	public EventType getType() {
		return type;
	}

	public Actor getAttore() {
		return attore;
	}

	public int getGiorno() {
		return giorno;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno - o.getGiorno();
	}
}
