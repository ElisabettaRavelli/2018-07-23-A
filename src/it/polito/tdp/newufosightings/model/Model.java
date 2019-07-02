package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private NewUfoSightingsDAO dao;
	private List<String> shape;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<ArcoPeso> arcopeso;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		this.shape = new LinkedList<>();
		this.arcopeso = new LinkedList<>();
	}
	
	public List<String> getShape(){
		this.shape = this.dao.getShape();
		Collections.sort(this.shape);
		return this.shape;
	}
	
	public void creaGrafo(Integer anno, String shape) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		System.out.println("Creo la lista");
		this.arcopeso = this.dao.getConnessioni(anno, shape);
		System.out.println("Creo il grafo");
		for(ArcoPeso ap: this.arcopeso) {
			Graphs.addEdgeWithVertices(this.grafo, ap.getV1(), ap.getV2(), ap.getPeso());
			System.out.println("Arco aggiunto"+ ap.getV1()+ " -> "+ ap.getV2()+ " con peso: "+ ap.getPeso());
		}
	}

	public int getVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<VerticeSommaPeso> getVicini(){
		int somma = 0;
		List<VerticeSommaPeso> result = new ArrayList<>();
		for(String vertice: this.grafo.vertexSet()) {
			List<String> vicini = Graphs.neighborListOf(this.grafo, vertice);
			for(String vicino: vicini) {
				somma = somma + (int) this.grafo.getEdgeWeight(this.grafo.getEdge(vertice, vicino));
			}
			result.add(new VerticeSommaPeso(vertice, somma));
		}
		return result;
	}

}
