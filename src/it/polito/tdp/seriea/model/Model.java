package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO dao;
	private DirectedGraph<Team, DefaultWeightedEdge> grafo;
	private Map<String, Team> teamMap;
	 
	private List<Team> camminoBest;
	
	public Model() {
		this.dao = new SerieADAO();
	}

	public List<Season> getSeason() {
		// TODO Auto-generated method stub
		return dao.listSeasons();
	}

	public void creaGrafo(int season) {
	
		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.teamMap = new HashMap<String, Team>();
		
		dao.loadGraph(grafo, teamMap, season);
		
	}

	public List<Team> getClassifica() {
		
		if(grafo!=null) {
			
			List<Team> ris = new LinkedList<Team>();
			
			for(Team t : teamMap.values()) {
				
				for(Team succ : Graphs.successorListOf(grafo, t)) {
					
					if(grafo.getEdgeWeight(grafo.getEdge(t, succ))==1)
						t.win();
					else if(grafo.getEdgeWeight(grafo.getEdge(t, succ))==0)
						t.draw();
				}
				
                   for(Team prec : Graphs.predecessorListOf(grafo, t)) {
					
					if(grafo.getEdgeWeight(grafo.getEdge(prec,t))==-1)
						t.win();
					else if(grafo.getEdgeWeight(grafo.getEdge(prec, t))==0)
						t.draw();
				}
                   
                  ris.add(t); 
					
			}
			
			Collections.sort(ris);
			return ris;
			
		}
		
		return null;
	}
	
	/*
	 * Algoritmo per la ricerca del cammino massimo 
	 * 
	 */
	public List<Team> cercaCammino(String s){
		
		if(grafo!=null) {
			
			Team partenza = teamMap.get(s);
			
			this.camminoBest = new ArrayList<Team>();
		
			List<Team> parziale = new ArrayList<>();
			Set<Arco> visitati = new HashSet<Arco>();
			parziale.add(partenza);
			cerca(parziale, visitati);
			
			
			return camminoBest;
		}
		return null;
		
	}

	private void cerca(List<Team> parziale, Set<Arco> visitati) {

		while(!parziale.isEmpty()) {
			
			Team squadra = parziale.get(parziale.size()-1);
			
			while(visitaInProfondita(visitati, parziale));
			
			if(parziale.size()>camminoBest.size()) 
				camminoBest = new ArrayList<>(parziale);
			
			parziale.remove(squadra);
		}
		
	}

	private boolean visitaInProfondita(Set<Arco> visitati, List<Team> parziale) {
	
	        Team squadra = parziale.get(parziale.size()-1);
		  
		for(Team succ : Graphs.successorListOf(grafo, squadra)) {
			
			if(grafo.getEdgeWeight(grafo.getEdge(squadra, succ))==1 && !visitati.contains(new Arco(squadra, succ))) {
				
		         parziale.add(succ);
		         visitati.add(new Arco(squadra, succ));
		         cerca(parziale, visitati);
			}
			
		}
		
		return false;
	}

	public List<String> getVertici() {
		
		List<String> vertici  = new LinkedList<>();
		for(Team t : grafo.vertexSet())
			vertici.add(t.getTeam());
		Collections.sort(vertici);
		return vertici;
	}
}
