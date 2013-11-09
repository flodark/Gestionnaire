package controller;

import model.Domaine;
import model.Tache;

import com.example.gestionnairedetaches.MainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class AddTacheListener implements OnClickListener {

	private MainActivity ma;
	private ArrayAdapter<Domaine> listDom;
	
	public AddTacheListener(MainActivity mainActivity, ArrayAdapter<Domaine> listeDesDomaines) {

		this.ma = mainActivity;
		this.listDom = listeDesDomaines;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		final Domaine domaineCourant = this.ma.getDomaineCourant();
		
		if(domaineCourant.getNom().equals("Selectionnez un domaine")){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.ma.getContext());
			alertDialogBuilder.setTitle("Selectionnez un domaine pour ajouter une Tache");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
				
		}
		else{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.ma.getContext());
			alertDialogBuilder.setTitle("Ajout d'une Tache dans le domaine "+domaineCourant.getNom());
			
			final EditText input = new EditText(this.ma.getContext());
			alertDialogBuilder.setView(input);
			alertDialogBuilder.setCancelable(false);
			
			alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int id) {

					String nomTache = input.getText().toString();
					if (!nomTache.equals("")){
						// Creation de la tache et ajout dans le domaine correspondant
						Tache t=new Tache(nomTache);
						ma.getBDD().insertTache(t, domaineCourant.getId());
						//domaineCourant.addTache(t);
						//listDom.remove(domaineCourant);
						//listDom.add(domaineCourant);
						//listDom.notifyDataSetChanged();
						ma.refreshTache();
						return;
					}
					else dialog.cancel();
				}
			});
			
			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			alertDialog.show();
		}
	}
}