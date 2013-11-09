package model;

import java.util.ArrayList;


public class Domaine {

	private ArrayList<Tache> listedetaches;
	private String name;
	private int myID;
	private static int id = 1;
	
	public Domaine(){
		this.listedetaches = new ArrayList<Tache> ();
	}
	
	public Domaine(String name){
		this.name=name;
		this.listedetaches = new ArrayList<Tache> ();
		this.myID = id;
		id++;
	}
	
	public String getNom() {
		return name;
	}

	public void setNom(String nom) {
		this.name = nom;
	}

	public ArrayList<Tache> getTache() {
		return listedetaches;
	}

	public void setTache(ArrayList<Tache> t) {
		this.listedetaches = t;
	}
	
	public void addTache(Tache t) {
		listedetaches.add(t);
	}
	
	public String toString(){
		String s=name;
		return s;
	}

	public int howManyChecked(){
		int valide = 0;
		for(int i = 1 ; i < listedetaches.size() ; i++){
			if (listedetaches.get(i).isValidee()){
				valide++;
			}
		}
		return valide;
	}

	public int getId() {
		return this.myID;
	}
	
	public void setId(int i){
		this.myID=i;
	}

	public static void setGlobalID(int i) {
		// TODO Auto-generated method stub
		id=i;
	}
}
