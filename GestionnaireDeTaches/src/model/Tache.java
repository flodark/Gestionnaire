package model;

import android.util.Log;

public class Tache {

	String name;
	private boolean validee;
	private int myID;
	private static int id = 1;
	
	public Tache(){
		this.name = "Default_Tache";
		this.validee = false;
		//this.myID = id;
		Log.d("idtache vide",""+id);
		//id++;
	}
	
	public Tache(String name) {
		this.name = name;
		this.validee = false;
		this.myID = id;
		Log.d("idtache name",""+id);
		id++;
	}
	
	public Tache(String name, boolean validee) {
		this.name = name;
		this.validee = validee;
		this.myID = id;
		Log.d("idtache nambool",""+id);
		id++;
	}
	
	public Tache(Tache t){
		this.name=t.getName();
		this.validee=t.isValidee();
		this.myID=t.getId();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValidee() {
		return validee;
	}

	public void setValidee(boolean validee) {
		this.validee = validee;
	}

	public int getId(){
		return this.myID;
	}
	
	@Override
	public String toString(){
		return this.name;
	}

	public void setId(int parseInt) {
		// TODO Auto-generated method stub
		this.myID=parseInt;
	}
	
	public static void setGlobalID(int i){
		id=i;
	}
}
