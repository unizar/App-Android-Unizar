package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE CRÉDITOS
 */
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class Creditos extends Activity{
	JSONArray jdata;
	TextView textUser;
	String nombreActividades;
	String[] datos; //idActividad&NombreActividad&CreditosActividad&CreditosTotales
    String usuario;
    String p;
    
    //Maneja las peticiones 
    Httppostaux post;
    private ProgressDialog pDialog;

    // String URL_connect    
    String directorio="/campusUnizar/creditos.php";
    String URL_connect;
    
    //Para llevar el control de creditos totales de la bbdd y los nuevos
    int totalCreditos;
    int totalCreditosBBDD;
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditos);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		//Obtencion del usuario de otra actividad
		Bundle bundle=getIntent().getExtras();
        usuario = bundle.getString("user");
		
        //Mostramos el nombre del usuario actual
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
        
	    //Invocamos a la clase auxiliar para obtener datos de BBDD
        new asynCreditos().execute(); 	
}




public boolean mostrarActividades()
{

	//ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
	//postparameters2send.add(new BasicNameValuePair("usuario",usuario));
	
	LinearLayout vista= (LinearLayout)findViewById(R.id.LinearCreditos);
	
	//JSONArray jdata= post.getserverdata(postparameters2send, URL_connect);
	
	
	if (jdata!= null && jdata.length()>0 )
	{
		int i = 0;
		while (i < jdata.length())
		{
			try
			{
				JSONObject row = jdata.getJSONObject(i);
				
				//Guardamos cada valor en su respectiva varible
				totalCreditosBBDD = Integer.parseInt(row.getString("maxCreditos"));
				int idActividadAct = Integer.parseInt(row.getString("idActividad"));
				String nombreActividadAct = row.getString("Nombre");
				String creditosActividadAct = row.getString("Creditos");
				
				
				//Para mostrar el titulo de la Actividad
				TextView lbNameActivity = new TextView(vista.getContext());
				lbNameActivity.setTextAppearance(this, R.style.textoH2);
				lbNameActivity.setText(nombreActividadAct);
				LinearLayout.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				params.setMargins(0,3,0,0);
				lbNameActivity.setLayoutParams(params);
				lbNameActivity.setId(idActividadAct);
				vista.addView(lbNameActivity);
				
				//Row para mostrar titulo y creditos en una misma linea
				TableRow tablerow = new TableRow(this);
				tablerow.setId(i);
				tablerow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
				
				//Para mostrar el titulo "Creditos"
				TextView lbCreditos = new TextView(vista.getContext());
				lbCreditos.setTextAppearance(this, R.style.texto);
				lbCreditos.setText("Créditos: ");
				lbCreditos.setId(idActividadAct);
				
				//Para mostrar el numero de creditos relativo a la actividad
				TextView txtCreditos = new TextView(vista.getContext());
				txtCreditos.setTextAppearance(this, R.style.texto);
				txtCreditos.setText(creditosActividadAct);
				txtCreditos.setId(idActividadAct);
				
				//Para añadirlo a la vista original
				tablerow.addView(lbCreditos);
				tablerow.addView(txtCreditos);
				vista.addView(tablerow);
				
				//Sumamos el numero de creditos de la nueva Actividad
				totalCreditos += Integer.parseInt(creditosActividadAct);
			
			}
			catch(JSONException e)
			{
				e.printStackTrace();
				return false;
				
			}
			
			i=i+1;
		}
		
		//Para mostrar por pantalla el total de creditos que tiene
		String total = "Total Creditos: " + totalCreditos;
		TextView lbTotalCreditos = new TextView(vista.getContext());
		lbTotalCreditos.setTextAppearance(this, R.style.textoH2);
		lbTotalCreditos.setText(total);
		LinearLayout.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.setMargins(0,3,0,30);
		lbTotalCreditos.setLayoutParams(params);
		lbTotalCreditos.setId(1);
		vista.addView(lbTotalCreditos);
		
		
		
		return true;
	}else
	{
		Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"No tienes Actividades suscritas", Toast.LENGTH_SHORT);
	    toast1.show();
		return false;
		
	}
}


public boolean consultabbdd(String username) {
	
	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
 		
	postparameters2send.add(new BasicNameValuePair("usuario",usuario));
	    		
	//realizamos una peticion y como respuesta obtenes un array JSON
	jdata=post.getserverdata(postparameters2send, URL_connect);

	if (jdata!=null && jdata.length() > 0)
	{
		return true;
	}else
	{		
		return false;
	}	
}

public void pintarTitulo()
{
	LinearLayout vista= (LinearLayout)findViewById(R.id.LinearCreditos);
	
	TextView lbTitulo = new TextView(vista.getContext());
	lbTitulo.setTextAppearance(this, R.style.textoH1);
	lbTitulo.setText("Créditos");
	LinearLayout.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	params.setMargins(0,0,0,15);
	lbTitulo.setLayoutParams(params);
	lbTitulo.setId(12);
	vista.addView(lbTitulo);

}

public void pintarCreditosBBDD()
{
	LinearLayout vista= (LinearLayout)findViewById(R.id.LinearCreditos);
	
	TextView lbCreditosBBDD = new TextView(vista.getContext());
	lbCreditosBBDD.setTextAppearance(this, R.style.textoH2);
	lbCreditosBBDD.setText("Objetivo a Cumplir: " + totalCreditosBBDD + " créditos");
	LinearLayout.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	params.setMargins(0,0,0,15);
	lbCreditosBBDD.setLayoutParams(params);
	lbCreditosBBDD.setId(12);
	vista.addView(lbCreditosBBDD);

}

public void pintarBoton()
{
	LinearLayout vista= (LinearLayout)findViewById(R.id.LinearCreditos);
	
	//Boton para que acceda a una nueva actividad y modificar el maximo de creditos
			Button btnMaxCreditos = new Button (vista.getContext());
			btnMaxCreditos.setText("Establecer Máximo Creditos");
			btnMaxCreditos.setTextColor(Color.parseColor("#ffffff"));
			btnMaxCreditos.setBackgroundColor(Color.parseColor("#2d6898"));
			LinearLayout.LayoutParams paramet=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			paramet.setMargins(0, 30, 0, 15);
			btnMaxCreditos.setLayoutParams(paramet);
			btnMaxCreditos.setClickable(true);
			btnMaxCreditos.setOnClickListener(new View.OnClickListener() {
							        @Override
							        public void onClick(View view) {
									          Intent i = new Intent(Creditos.this, CreditosMaximo.class);
									          i.putExtra("user", usuario);
									          i.putExtra("totalCreditosAct", totalCreditos);
									          startActivity(i);
									          finish();
							        }
		      				});
			vista.addView(btnMaxCreditos);
}

//vibra y muestra un Toast
public void err_creditos(){
	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    vibrator.vibrate(200);
    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:No tiene Actividades Suscritas", Toast.LENGTH_SHORT);
	    toast1.show();    	
}

public void popUpCreditosMaximos()
{
	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	alert.setTitle("Créditos Alcanzados");
	alert.setMessage("Ha llegado al máximo de créditos.");
	alert.setCancelable(true);
	alert.setNeutralButton("Aceptar",new OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });
	alert.setIcon(R.drawable.ic_launcher);
    AlertDialog alert11 = alert.create();
    alert11.show();
}


class asynCreditos extends AsyncTask < String, String, String > {
	 
	String user;
    @Override
	protected void onPreExecute() {
    	//para el progress dialog
        pDialog = new ProgressDialog(Creditos.this);
        pDialog.setMessage("Creditos....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

	@Override
	protected String doInBackground(String... params) {
		
		//enviamos y recibimos y analizamos los datos en segundo plano.
		if (consultabbdd(usuario)==true)
		{    		    		
			return "ok"; //login valido
		}
		else
		{    		
			return "err"; //login invalido     	          	  
		}
	}
   

    @Override
	protected void onPostExecute(String result) {

    	pDialog.dismiss();//ocultamos progess dialog.
        Log.e("onPostExecute=",""+result);
        
        pintarTitulo();
        if (result.equals("ok")){
        	mostrarActividades();
        	pintarCreditosBBDD();
        	pintarBoton();
        	//Si ha superado el numero de creditos maximo establecido, le mostramos popup informativo
        	if ((totalCreditos >= totalCreditosBBDD) && (totalCreditosBBDD > 0 ))
    		{
    			popUpCreditosMaximos();
    		}
        }else
        {
        	pintarCreditosBBDD();
        	pintarBoton();
        	err_creditos();
        	//SystemClock.sleep(1500);
        	//finish();
        }
	}
        
}



	
}
