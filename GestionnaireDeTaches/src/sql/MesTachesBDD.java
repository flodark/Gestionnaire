package sql;

import java.util.ArrayList;

import model.Domaine;
import model.Tache;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class MesTachesBDD {
 
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "mestaches.db";
 
	private static final String TABLE_DOMAINES = "table_domaines";
	private static final String COL_ID_DOM = "ID_DOM";
	private static final String COL_NOM_DOM = "NOM_DOM";
	
	private static final String TABLE_TACHES = "table_taches";
	private static final String COL_ID_TACHE = "ID_TACHE";
	private static final String COL_NOM_TACHE = "NOM_TACHE";
	private static final String COL_VALIDEE = "VALIDEE";
	private static final String COL_ID_DOM_REF = "ID_DOM";
 
	private SQLiteDatabase bdd;
 
	private MaBaseSQL maBaseSQL;
 
	public MesTachesBDD(Context context){
		//On créer la BDD et sa table
		this.maBaseSQL = new MaBaseSQL(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQL.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertDomaine(Domaine dom){
		ContentValues values = new ContentValues();
		values.put(COL_NOM_DOM, dom.getNom());
		return bdd.insert(TABLE_DOMAINES, null, values);
	}
	
	public long insertTache(Tache t, int domID){
		ContentValues values = new ContentValues();
		values.put(COL_NOM_TACHE, t.getName());
		int bo = 0;
		if(t.isValidee())
			bo = 1;
		values.put(COL_VALIDEE, bo);
		values.put(COL_ID_DOM_REF, domID);
		//Log.d("dom tache","tache "+t.getName()+" inseree !!");
		return bdd.insert(TABLE_TACHES, null, values);
	}
 
	public int updateDomaine(Domaine dom){
		ContentValues values = new ContentValues();
		values.put(COL_NOM_DOM, dom.getNom());
		return bdd.update(TABLE_DOMAINES, values, COL_ID_DOM + " = " +dom.getId(), null);
	}
	
	public int updateTache(Tache tach){
		ContentValues values = new ContentValues();
		values.put(COL_NOM_TACHE, tach.getName());
		int bo = 0;
		if(tach.isValidee())
			bo = 1;
		values.put(COL_VALIDEE, bo);
		return bdd.update(TABLE_TACHES, values, COL_ID_TACHE + " = " +tach.getId(), null);
	}
 
	public int selectMaxIDTache(){
		String query = "SELECT max("+COL_ID_TACHE+") FROM "+TABLE_TACHES;
		Cursor c = bdd.rawQuery(query, null);
		//Log.d("cursor count",c.getString(0));
		int id = 0; 
	    if (c.moveToFirst())
	    {
	        do
	        {           
	            id = c.getInt(0);                  
	        } while(c.moveToNext());           
	    }
	    return id;
	}
	
	public void removeDomaineWithID(int id){
		bdd.delete(TABLE_TACHES, COL_ID_DOM_REF + " = "+id, null);
		bdd.delete(TABLE_DOMAINES, COL_ID_DOM + " = " +id, null);
	}
 
	/*public Domaine getLivreWithTitre(String nomDom){
		Cursor c = bdd.query(TABLE_DOMAINES, new String[] {COL_ID_DOM, COL_NOM_DOM}, COL_NOM_DOM + " LIKE \"" + nomDom +"\"", null, null, null, null);
		return cursorToDomaine(c).get(0);
	}*/
 
	public ArrayList<Domaine> getAllDomaines(){
		String query = "SELECT "+COL_ID_DOM+","+COL_NOM_DOM+" FROM "+TABLE_DOMAINES;
		Cursor c = bdd.rawQuery(query, null);
		return this.cursorToDomaine(c);
	}
	
	public int selectMaxIdDomaine(){
		String query = "SELECT max("+COL_ID_DOM+") FROM "+TABLE_DOMAINES;
		Cursor c = bdd.rawQuery(query, null);
		//Log.d("cursor count",c.getString(0));
		int id = 0;     
	    if (c.moveToFirst())
	    {
	        do
	        {           
	            id = c.getInt(0);                  
	        } while(c.moveToNext());           
	    }
	    return id;
	}
	
	private ArrayList<Domaine> cursorToDomaine(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		//Log.d("dom count",""+c.getCount());
		if (c.getCount() == 0)
			return null;
		ArrayList<Domaine> list = new ArrayList<Domaine>();
		
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		////Log.d("dom nom1",c.getString(0));
		////Log.d("dom is last",""+c.isLast());
		////Log.d("dom index",""+c.getColumnIndex(COL_NOM_DOM));
		do{
			Domaine d = new Domaine();
			////Log.d("dom nom1",c.getString(1));
			////Log.d("dom nom2",c.getString(2));
			d.setNom(c.getString(c.getColumnIndex(COL_NOM_DOM)));
			d.setId(Integer.parseInt(c.getString(c.getColumnIndex(COL_ID_DOM))));
			String query = "SELECT "+COL_NOM_TACHE+","+COL_VALIDEE+","+COL_ID_TACHE+" FROM "+TABLE_TACHES+" where "+COL_ID_DOM_REF+" = "+d.getId();
			Cursor ct = bdd.rawQuery(query, null);
			d.setTache(cursorToTaches(ct));
			
			list.add(d);
		}while(c.moveToNext());
		//On ferme le cursor
		c.close();

		return list;
	}

	private ArrayList<Tache> cursorToTaches(Cursor c) {
		//Log.d("dom tache","jrentre dans tache !!");
		//Log.d("dom tache count",""+c.getCount());
		if (c.getCount() == 0)
			return null;
		
		ArrayList<Tache> list = new ArrayList<Tache>();
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		do{
			Tache t = new Tache();
			//Log.d("tacheid nom",c.getString(c.getColumnIndex(COL_NOM_TACHE)));
			//Log.d("tacheid id",c.getString(c.getColumnIndex(COL_ID_TACHE)));
			//Log.d("tacheid valid",c.getString(c.getColumnIndex(COL_VALIDEE)));
			t.setName(c.getString(c.getColumnIndex(COL_NOM_TACHE)));
			t.setId(Integer.parseInt(c.getString(c.getColumnIndex(COL_ID_TACHE))));
			if(Integer.parseInt(c.getString(c.getColumnIndex(COL_VALIDEE))) == 1)
				t.setValidee(true);
			else
				t.setValidee(false);
			list.add(t);
		}while(c.moveToNext());
		
		//On ferme le cursor
		c.close();

		return list;
	}

	public ArrayList<Tache> getAllTaches(int idDom) {
		// TODO Auto-generated method stub
		String query = "SELECT "+COL_NOM_TACHE+","+COL_VALIDEE+","+COL_ID_TACHE+" FROM "+TABLE_TACHES+" where "+COL_ID_DOM_REF+" = "+idDom;
		//Log.d("query",query);
		Cursor ct = bdd.rawQuery(query, null);
		return cursorToTaches(ct);
	}

	public boolean selectValideeFromTache(Tache tache) {
		// TODO Auto-generated method stub
		String query = "SELECT "+COL_VALIDEE+" FROM "+TABLE_TACHES+" where "+COL_ID_TACHE+" = "+tache.getId();
		Cursor c = bdd.rawQuery(query, null);
		////Log.d("cursor count",c.getString(0));
		int i = 0;
		boolean valid = false;
	    if (c.moveToFirst())
	    {
	        do
	        {           
	            i = c.getInt(0);                  
	        } while(c.moveToNext());           
	    }
	    if(i==1)
	    	return !valid;
	    else
	    	return valid;
	}

	public void truncateAll() {
		// TODO Auto-generated method stub
		this.maBaseSQL.onUpgrade(bdd, VERSION_BDD, VERSION_BDD+1);
	}

	public void removeTacheFromDomaine(int idTache, int idDom) {
		// TODO Auto-generated method stub
		this.bdd.delete(TABLE_TACHES, COL_ID_DOM_REF+" = "+idDom+" and "+COL_ID_TACHE+" = "+idTache, null);
	}
}
