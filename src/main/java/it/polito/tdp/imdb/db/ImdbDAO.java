package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getGeneri(){
		String sql = "SELECT DISTINCT genre FROM movies_genres";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listActorsGenres(String genre, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT a.id, a.first_name, a.last_name, gender FROM actors AS a, roles AS r, movies_genres AS mg " + 
				"WHERE mg.genre=? AND mg.movie_id=r.movie_id AND r.actor_id=a.id";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, genre);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("a.id"), res.getString("a.first_name"), res.getString("a.last_name"),
						res.getString("a.gender"));
				
				idMap.put(res.getInt("a.id"), actor);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> listAdiacenze(String genre, Map<Integer, Actor> idMap){
		String sql = "SELECT r1.actor_id AS a1, r2.actor_id AS a2, COUNT(DISTINCT r1.movie_id) AS peso FROM movies_genres AS mg, roles AS r1, roles AS r2 " + 
				"WHERE r1.actor_id>r2.actor_id AND mg.genre=? AND mg.movie_id=r1.movie_id AND mg.movie_id=r2.movie_id " + 
				"GROUP BY r1.actor_id, r2.actor_id";
		
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, genre);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {

				adiacenze.add(new Adiacenza(idMap.get(res.getInt("a1")), idMap.get(res.getInt("a2")), res.getInt("peso")));
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
