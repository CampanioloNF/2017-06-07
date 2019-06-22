package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public void loadGraph(Graph<Team, DefaultWeightedEdge> grafo, Map<String, Team> teamMap, int anno) {
		
         String sql = "SELECT HomeTeam, AwayTeam, sum(case when FTR = 'H' then 1 when FTR = 'A' then -1 else 0 END) AS ftr " + 
         		"FROM matches " + 
         		"WHERE season = ? " + 
         		"GROUP BY  HomeTeam, AwayTeam" ;
		
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(!teamMap.containsKey(res.getString("HomeTeam"))) {
				   teamMap.put(res.getString("HomeTeam"),new Team(res.getString("HomeTeam"))) ;
				   grafo.addVertex(teamMap.get(res.getString("HomeTeam")));
				}
				if(!teamMap.containsKey(res.getString("AwayTeam"))) {
				   teamMap.put(res.getString("AwayTeam"),new Team(res.getString("AwayTeam"))) ;
				   grafo.addVertex(teamMap.get(res.getString("AwayTeam")));
				}
				Graphs.addEdge(grafo, teamMap.get(res.getString("HomeTeam")), 
						teamMap.get(res.getString("AwayTeam")), res.getInt("ftr"));
			}
			
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}


}
