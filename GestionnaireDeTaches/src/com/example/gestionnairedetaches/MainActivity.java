package com.example.gestionnairedetaches;

import java.util.ArrayList;
import java.util.HashMap;

import sql.MesTachesBDD;

import controller.AddTacheListener;
import model.Domaine;
import model.Tache;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	private ListView list;
	private final Context text = this;
	private TextView tv;
	private ArrayList<Domaine> groupDomaine = new ArrayList<Domaine>();
	private Domaine domaineCourant;
	private MesTachesBDD bdd;
	Spinner domaine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//suppression du titre de l'appli en haut + full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        //si on veut plein Ã©cran
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.bdd = new MesTachesBDD(text);
		
		setContentView(R.layout.activity_main);
		TabHost tabs=(TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		
		TabHost.TabSpec spec=tabs.newTabSpec("tag1");
		spec.setContent(R.id.Gestion);
		spec.setIndicator("Gestion");
		tabs.addTab(spec);		
		
		spec=tabs.newTabSpec("tag2");
		spec.setContent(R.id.Aide);
		spec.setIndicator("Aide");
		tabs.addTab(spec);
		
		tabs.setCurrentTab(0);
		
		tv = (TextView) findViewById(R.id.CreationDom);
		tv.setText("Appuyer sur le bouton \"Ajouter un Domaine\", puis choissisez le nom voulu\n");
		tv = (TextView) findViewById(R.id.ModifDom);
		tv.setText("Choississez le Domaine Ã  modifier et cliquer dans le menu sur Modifier nom domaine\n");
		tv = (TextView) findViewById(R.id.SupprDom);
		tv.setText("Choississez le Domaine Ã  supprimer et cliquer dans le menu sur Supprimer domaine courant\n");
		tv = (TextView) findViewById(R.id.CreationTache);
		tv.setText("Appuyer sur le bouton \"Ajouter une Tache\", puis choissisez le nom voulu\n");
		tv = (TextView) findViewById(R.id.ModifTache);
		tv.setText("Appuie long sur la Tache a modifier puis cliquer sur Modifier tache selectionne\n");
		tv = (TextView) findViewById(R.id.SupprTache);
		tv.setText("Appuie long sur la Tache a supprimer puis cliquer sur Supprimer tache selectionne\n");
		tv = (TextView) findViewById(R.id.remiseZero);
		tv.setText("Cliquer sur Remise Ã  zero dans le menu, cela permet de tout supprimer\n");
		domaine= (Spinner) this.findViewById(R.id.domaines);
		final Button addTache = (Button) this.findViewById(R.id.addTache);
		final Button addDomaine = (Button) this.findViewById(R.id.addDomaine);
		
		list = getListView();
		bdd.open();
		ArrayAdapter<Domaine> listeDesDomaines;
		
		if(this.bdd.getAllDomaines() != null){
			//groupDomaine.addAll(this.bdd.getAllDomaines());
			Tache.setGlobalID(this.bdd.selectMaxIDTache()+1);
			Domaine.setGlobalID(this.bdd.selectMaxIdDomaine()+1);
			listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, this.bdd.getAllDomaines());
			
			addTache.setEnabled(true);
			refreshTache();
		}
		else{
			groupDomaine.add(new Domaine("Aucun domaine présent"));
			domaineCourant = groupDomaine.get(0);
			listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, groupDomaine);
			addTache.setEnabled(false);
		}
		//ArrayAdapter<Domaine> listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, groupDomaine);
		listeDesDomaines.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    
	    
	    domaine.setAdapter(listeDesDomaines);
	    //list.setAdapter(getListAdapter());
	    registerForContextMenu(getListView());
	    domaine.setOnItemSelectedListener(new OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	    		if(!((Domaine) domaine.getSelectedItem()).getNom().equals("Aucun domaine présent"))
	    			domaineCourant = (Domaine) domaine.getSelectedItem();
	    		else
	    			domaineCourant = null;
	    		refreshTache();
	        }

	        public void onNothingSelected(AdapterView<?> parentView) {
	           return;
	        }

	    });
	    
		addTache.setOnClickListener(new AddTacheListener(this,listeDesDomaines));
		
		addDomaine.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAddDomaineDialog();
			}
			
		});
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_tache, menu);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        final Tache t = this.bdd.getAllTaches(domaineCourant.getId()).get(info.position);
        menu.setHeaderTitle("Tache : "+t.getName());
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  final Tache t = this.bdd.getAllTaches(domaineCourant.getId()).get(info.position);
	  switch (item.getItemId()) {
	  	case R.id.editTacheMenu:
	  		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					text);

			alertDialogBuilder.setTitle("Modification du nom de la tÃ¢che "+t.getName());
			final EditText input = new EditText(text);
			alertDialogBuilder
			.setView(input)
			.setCancelable(false)
			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					String nomTache = input.getText().toString();
					if (!nomTache.equals("")){
						// Creation de la tache et ajout dans le domaine correspondant							
						t.setName(nomTache);
						bdd.updateTache(t);
						refreshTache();
						return;
					}
					else dialog.cancel();
				}
			})
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			alertDialog.show();
	    	return true;
	  	case R.id.deleteTacheMenu:
			bdd.removeTacheFromDomaine(t.getId(),domaineCourant.getId());
			refreshTache();							
	  		return true;
	  	default:
	    	return super.onContextItemSelected(item);
	  }
	}
	
	public void refreshTache(){
		ArrayList<HashMap<String, Tache>> listItem = new ArrayList<HashMap<String, Tache>>();
		HashMap<String, Tache> map;
		
		if(domaineCourant!=null && this.bdd.getAllTaches(domaineCourant.getId()) != null){
			for(Tache t : this.bdd.getAllTaches(domaineCourant.getId())){
				map = new HashMap<String, Tache>();
				map.put("titre",t);
				listItem.add(map);
			}
		}
		
		Adapter mSchedule = new Adapter(this.getBaseContext(), listItem,
				R.layout.tache_vue, new String[] {"titre"}, new int[] {R.id.nomTache},this);
		
		// On attribue ï¿½ notre listView l'adaptateur que l'on vient de crï¿½er
		//this.getListView().setAdapter(mSchedule);
		list.setAdapter(mSchedule);
		//list = getListView();
	}
	
	public void MyHandler(View v) {
		
		//on rï¿½cupï¿½re la position ï¿½ l'aide du tag dï¿½fini dans la classe MyListAdapter
		CheckBox cb =(CheckBox)v;
		int wantedPosition = Integer.parseInt(cb.getTag().toString()); // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		/*if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.w("handler wantedchiled", "Unable to get view for desired position, because it's not being displayed on screen.");
		  return;
		}*/
		/*Log.d("handler first",""+list.getFirstVisiblePosition());
		Log.d("handler header",""+list.getHeaderViewsCount());
		
		Log.d("handler wantedChild", ""+wantedChild);
		Log.d("handler wantedPosition", ""+wantedPosition);*/
		// On rï¿½cupï¿½re l'ï¿½lï¿½ment sur lequel on va changer la couleur
		View o = list.getChildAt(wantedChild).findViewById(R.id.blocCheck);
		//On change la couleur
		if (cb.isChecked()) {
			o.setBackgroundResource(R.color.green);
		} else {
			o.setBackgroundResource(R.color.blue);
		}
		
		/*Log.d("tachew nom",this.bdd.getAllTaches(domaineCourant.getId()).get(wantedPosition).getName());
		Log.d("tachew id",""+this.bdd.getAllTaches(domaineCourant.getId()).get(wantedPosition).getId());
		Log.d("tachew valid",""+this.bdd.getAllTaches(domaineCourant.getId()).get(wantedPosition).isValidee());
		Log.d("tachew validcheckbox",""+cb.isChecked());*/
		Tache t = new Tache(this.bdd.getAllTaches(domaineCourant.getId()).get(wantedPosition));
		t.setValidee(cb.isChecked());
		/*Log.d("tachew valid apres",""+t.isValidee());
		Log.d("handler tname",t.getName());*/
		this.bdd.updateTache(t);
	}
	
	public void refreshDom(int pos){
		//ArrayAdapter<Domaine> DataAdapter = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, bdd.getAllDomaines());
		ArrayAdapter<Domaine> listeDesDomaines;
		final Button addTache = (Button) this.findViewById(R.id.addTache);
		if(this.bdd.getAllDomaines() != null){
			//groupDomaine.addAll(this.bdd.getAllDomaines());
			listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, this.bdd.getAllDomaines());
			addTache.setEnabled(true);
		}
		else{
			if(groupDomaine.size()==0)
				groupDomaine.add(new Domaine("Aucun domaine présent"));
			listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, groupDomaine);
			addTache.setEnabled(false);
		}
		listeDesDomaines.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    Spinner domaine = (Spinner) this.findViewById(R.id.domaines);
	    domaine.setAdapter(listeDesDomaines);
	    if(pos ==-1)
	    	domaine.setSelection(domaine.getCount()-1);
	    else
	    	domaine.setSelection(pos);
	}
	
	public void showAddDomaineDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				text);

		alertDialogBuilder.setTitle("Ajout d'un Domaine");
		final EditText input = new EditText(text);
		alertDialogBuilder
		.setView(input)
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				String nomDomaine = input.getText().toString();
				if (!nomDomaine.equals("")){
					// Creation du domaine et ajout du domaine dans la liste pour la vue
					Domaine dom = new Domaine(nomDomaine);
					//groupDomaine.add(Dom);
					bdd.insertDomaine(dom);
					refreshDom(-1); //TODO
					return;
				}
				else dialog.cancel();
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}

	public void showModifDomaineDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				text);

		alertDialogBuilder.setTitle("Modification du nom de domaine pour le domaine "+domaineCourant.getNom());
		final EditText input = new EditText(text);
		alertDialogBuilder
		.setView(input)
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				String nomDom = input.getText().toString();
				if (!nomDom.equals("")){
					// Creation de la tache et ajout dans le domaine correspondant							
					domaineCourant.setNom(nomDom);
					bdd.updateDomaine(domaineCourant);
					refreshDom(domaine.getSelectedItemPosition());
					return;
				}
				else dialog.cancel();
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		
		/*if(domaineCourant!=null){
			if(domaineCourant.getNom().equals("Selectionnez un domaine")){
				menu.getItem(1).setEnabled(false);
				menu.getItem(2).setEnabled(false);
			}
			else{
				menu.getItem(1).setEnabled(true);
				menu.getItem(2).setEnabled(true);
			}
		}
		*/
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onStop(){
		super.onStop();
		this.bdd.close();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		final Spinner domaine = (Spinner) this.findViewById(R.id.domaines);
		switch (item.getItemId()) {
			case R.id.modifItemCurrent:
				if(domaineCourant==null || domaineCourant.getNom().equals("Aucun domaine courant")){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							text);
					alertDialogBuilder.setTitle("Pas de domaine disponible !");
					alertDialogBuilder
					.setCancelable(false)
					.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					alertDialog.show();
						
				}
				else{
					showModifDomaineDialog();
				}
				return true;
			case R.id.deleteItemCurrent:
				
				if(domaineCourant==null || domaineCourant.getNom().equals("Aucun domaine présent")){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(text);
					alertDialogBuilder.setTitle("Pas de domaine disponible !");
					alertDialogBuilder.setCancelable(false).setNeutralButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
						
				}
				else{
					ArrayList<Tache> at=this.bdd.getAllTaches(domaineCourant.getId());
					if(at!=null && at.size()>3){
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(text);
						alertDialogBuilder.setTitle("ÃŠtes-vous sÃ»r de vouloir supprimer le domaine "+domaineCourant.getNom()+"?");
						alertDialogBuilder.setCancelable(false)
						.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
														
							}
						})
						.setNegativeButton("Non",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else{
						bdd.removeDomaineWithID(domaineCourant.getId());
						refreshDom(-1);	
					}
					
				}
				return true;
			case R.id.deleteAllItem:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(text);
				alertDialogBuilder.setTitle("ÃŠtes-vous sÃ»r de vouloir tout supprimer?");
				alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						//groupDomaine.clear();
						if(groupDomaine.size()==0)
							groupDomaine.add(new Domaine("Aucun domaine présent"));
						bdd.truncateAll();
						Tache.setGlobalID(1);
						Domaine.setGlobalID(1);
						ArrayAdapter<Domaine> listeDesDomaines = new ArrayAdapter<Domaine>(text,android.R.layout.simple_spinner_item, groupDomaine);
						domaine.setAdapter(listeDesDomaines);
						
						refreshDom(-1);
						list.refreshDrawableState();
					}
				})
				.setNegativeButton("Non",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;  
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public ArrayList<Domaine> getGroupDomaine() {
		// TODO Auto-generated method stub
		return this.groupDomaine;
	}
	
	public Context getContext(){
		return this.text;
	}
	
	public Domaine getDomaineCourant(){
		return this.domaineCourant;
	}
	
	public MesTachesBDD getBDD(){
		return this.bdd;
	}
}