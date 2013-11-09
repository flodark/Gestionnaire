package com.example.gestionnairedetaches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Tache;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;

public class Adapter extends SimpleAdapter
{
	private LayoutInflater	mInflater;
	private List<HashMap<String, Tache>> nosTaches;
	private MainActivity ma;
	
	public Adapter (Context context, ArrayList<HashMap<String, Tache>> listItem,
			int resource, String[] from, int[] to, MainActivity mainActivity)
	{
		super (context, listItem, resource, from, to);
		this.mInflater = LayoutInflater.from (context);
		this.nosTaches = listItem;
		this.ma = mainActivity;

	}

	@Override
	public Object getItem (int position)
	{
		return super.getItem (position);
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent)
	{
			// On r�cup�re les �l�ments de notre vue
			convertView = mInflater.inflate (R.layout.tache_vue, null);
			// On r�cup�re notre checkBox
			CheckBox cb = (CheckBox) convertView.findViewById (R.id.check);
			// On lui affecte un tag comportant la position de l'item afin de
			// pouvoir le r�cup�rer au clic de la checkbox
			cb.setTag (position);
			//Log.d("tache id adapt",""+nosTaches.get(position).get("titre").getId());
			cb.setChecked(this.ma.getBDD().selectValideeFromTache(nosTaches.get(position).get("titre")));
			//Log.d("adapt pos", ""+position);
			View o = convertView.findViewById(R.id.blocCheck);
			//On change la couleur
			if (cb.isChecked()) {
				o.setBackgroundResource(R.color.green);
			} else {
				o.setBackgroundResource(R.color.blue);
			}
			o.setLongClickable(true);
		return super.getView (position, convertView, parent);
	}

}