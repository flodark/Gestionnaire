package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQL extends SQLiteOpenHelper {

	private static final String TABLE_DOMAINES = "table_domaines";
	private static final String TABLE_TACHES = "table_taches";
	private static final String COL_ID_DOM = "ID_DOM";
	private static final String COL_NOM_DOM = "NOM_DOM";
	private static final String COL_ID_TACHE = "ID_TACHE";
	private static final String COL_NOM_TACHE = "NOM_TACHE";
	private static final String COL_VALIDEE = "VALIDEE";
	private static final String COL_ID_DOM_REF = "ID_DOM";
 
	private static final String CREATE_TABLE_DOMAINES = "CREATE TABLE " + TABLE_DOMAINES + " ("
	+ COL_ID_DOM + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM_DOM + " TEXT NOT NULL);";
	private static final String CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_TACHES + " ("
			+ COL_ID_TACHE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM_TACHE + " TEXT NOT NULL," +
					COL_VALIDEE+" INTEGER," +
					COL_ID_DOM_REF + " INTEGER," +
					" FOREIGN KEY ("+COL_ID_DOM_REF+") REFERENCES "+TABLE_DOMAINES+" ("+COL_ID_DOM+"));";
	
	public MaBaseSQL(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
		//String CREATE_BDD = CREATE_TABLE_DOMAINES+CREATE_TABLE_TACHES;
		db.execSQL(CREATE_TABLE_DOMAINES);
		db.execSQL(CREATE_TABLE_TACHES);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_TACHES + ";");
		db.execSQL("DROP TABLE " + TABLE_DOMAINES + ";");
		onCreate(db);
	}
}
