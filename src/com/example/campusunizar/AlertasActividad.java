package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE ALERTAS
 */
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlertasActividad extends Activity{
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
	Httppostaux post;//Manejador de peticiones
	
		//String URL_connect
    String directorio="/campusUnizar/alertasActividad.php";
    String URL_connect;
    
    //Array donde vamos a guardar las Alertas
    JSONArray jdata;
    
    String idActividad;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alertas_actividad);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		//Obtenemos los parametros que nos pasa el Intent
		Bundle bundle=getIntent().getExtras();
		idActividad=bundle.getString("idActividad");
		
		//Ejecución en segundo Plano que muestra las Alertas
		new asyncAlertas().execute();
		
	}
	
	public boolean consultaAlertasActividad(){
		
		//Como parámetro le pasamos el id de la Actividad 
  		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("idActividad",idActividad));
  		
  		//Se realiza la petición sobre alertas que tiene la actividad	
  		jdata= post.getserverdata(postparameters2send, URL_connect);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
	}
	
	public void muestraAlertas(){
		   
		LinearLayout layout_botones= (LinearLayout)findViewById(R.id.linearlayout_buttons);
		//JSONObject json_data; //creamos un objeto JSON
			int i = 0;
			
  			while (i <= jdata.length()){
  				try {
  					JSONObject row = jdata.getJSONObject(i);
  					String nombre = row.getString("nombreAlerta");
  					String profesor = row.getString("nombreProfesor");
  					String apellidos = row.getString("Apellidos");
  					String info = row.getString("informacion");
 
  					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  
		  			//Nombre Alerta
					TextView nombreAlerta = new TextView(layout_botones.getContext());
					nombreAlerta.setTextAppearance(this, R.style.textoH1);
					nombreAlerta.setTextColor((Color.parseColor("#164fc9")));
					nombreAlerta.setText(nombre);
					params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 3, 0, 0);
				    nombreAlerta.setLayoutParams(params);
				    nombreAlerta.setId(122);
				    layout_botones.addView(nombreAlerta);
			
				    //Profesor Alerta
					TextView etiquetaProfesor = new TextView(layout_botones.getContext());
					etiquetaProfesor.setTextAppearance(this, R.style.textoH2);
					etiquetaProfesor.setText("Profesor:");
					params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 3, 0, 0);
				    etiquetaProfesor.setLayoutParams(params);
				    etiquetaProfesor.setId(112);
				    layout_botones.addView(etiquetaProfesor);
					
				    TextView nombreProfesor = new TextView(layout_botones.getContext());
				    nombreProfesor.setTextAppearance(this, R.style.texto);
				    nombreProfesor.setText(profesor+' '+apellidos);
					params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 3, 0, 0);
				    nombreProfesor.setLayoutParams(params);
				    nombreProfesor.setId(122);
				    layout_botones.addView(nombreProfesor);
			
					//Mail Profesor
					TextView titulomail = new TextView(layout_botones.getContext());
					titulomail.setTextAppearance(this, R.style.textoH2);
					titulomail.setText("Informacion");
					params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 3, 0, 0);
				    titulomail.setLayoutParams(params);
				    titulomail.setId(112);
				    layout_botones.addView(titulomail);
					
					TextView informacion = new TextView(layout_botones.getContext());
					informacion.setTextAppearance(this, R.style.texto);
					informacion.setText(info);
					params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 3, 0, 0);
				    informacion.setLayoutParams(params);
				    informacion.setId(122);
				    layout_botones.addView(informacion);
		
				    
  					}catch (JSONException e) {
  	  					// TODO Auto-generated catch block
  	  					e.printStackTrace();
  	  				}	
  				i=i+1;
  			}
	}
	
	//vibra y muestra un Toast
    public void err_alertas(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:No hay alertas registradas", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
	
	/*		CLASE ASYNCTASK
	 * 
	 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
	 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
	 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
	 * ademas observariamos el mensaje de que la app no responde.     
	 */
	    
	    class asyncAlertas extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(AlertasActividad.this);
	            pDialog.setMessage("Buscando Alertas....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (consultaAlertasActividad()==true){    		    		
	    			return "ok"; //login valido
	    		}else{    		
	    			return "err"; //login invalido     	          	  
	    		}
	        	
			}
	       
			/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
			 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
	        @Override
			protected void onPostExecute(String result) {

	           pDialog.dismiss();//ocultamos progess dialog.
	           Log.e("onPostExecute=",""+result);
	           
	           if (result.equals("ok")){
					muestraAlertas();
	            }else{
	            	err_alertas();
	            }
	            
	                									}
			
	        }
}
