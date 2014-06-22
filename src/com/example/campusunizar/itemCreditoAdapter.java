package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE CRÉDITOS
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class itemCreditoAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<String> itemsListView;
	
	itemCreditoAdapter(Activity activity, ArrayList<String> items)
	{
		this.activity = activity;
		this.itemsListView = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemsListView.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemsListView.get(position);
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View vi = contentView;
		
		if (contentView == null){
		      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      vi = inflater.inflate(R.layout.list_item_creditos, null);
		}
		
		String actividad = itemsListView.get(position);
		
		TextView nombreAsignatura = (TextView) vi.findViewById(R.id.lblnombreActividadCred);
		nombreAsignatura.setText(actividad.split(";")[1]);
	         
	    EditText creditos = (EditText) vi.findViewById(R.id.txtCreditosAct);
	    creditos.setText(actividad.split(";")[2]);
	    
		return vi;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}

