package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE UNA ACTIVIDAD PRIVADA
 */
import java.sql.Connection;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadPrivadaActual extends Activity  {
Connection conexionMySQL;//Variable de conexión
	TextView textUser;
	String usuario;
	String idActividad;
	String tituloAct;
    //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    //private ProgressDialog pDialog;
    Httppostaux post;
    //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
    JSONArray jdata;
    
    String directorio="/campusUnizar/actividad_privada_actual.php";
    String URL_connect;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_privada_actual);
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);		
		
		new asyncbotones().execute();
		Bundle bundle=getIntent().getExtras();
		idActividad=bundle.getString("idActividad");
		tituloAct=bundle.getString("tituloActividad");
		
		String usuario = bundle.getString("user");
		
		textUser = (TextView) findViewById(R.id.text_user);
	    textUser.setText("Usuario: " + usuario);
	    
	}
	public boolean actividadPrivadaActual(){
		
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("idActividad",idActividad));
  		
  		//Se realiza la petición
  		jdata= post.getserverdata(postparameters2send, URL_connect);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
	}
	public boolean muestraInformacion() throws JSONException{
  		LinearLayout vista = (LinearLayout)findViewById(R.id.info_act_privadas);
  		
  		if (jdata!=null && jdata.length() > 0){
  			//Accedemos a la primera posición del array porque solo esperamos ese valor
  			JSONObject row = jdata.getJSONObject(0);
				
		    //text = row.getString("NombreAsignatura")
  		  //Titulo de actividad
  	  		TextView titulo = new TextView(vista.getContext());
  	  		titulo.setTextAppearance(this, R.style.textoH1);
  	  		titulo.setText(row.getString("NombreAsignatura")); 
  	  		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  	  		titulo.setLayoutParams(params);
  	  		titulo.setId(111);
  	  		vista.addView(titulo);
  	  		
  	  	 //Código Asignatura
  			TextView codigoAsig = new TextView(vista.getContext());
  			codigoAsig.setTextAppearance(this, R.style.textoH2);
  			codigoAsig.setText("Código Asignatura:");
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    codigoAsig.setLayoutParams(params);
  		    codigoAsig.setId(112);
  			vista.addView(codigoAsig);
  			
  			String codigo= row.getString("Asignatura");
  			TextView cod = new TextView(vista.getContext());
  			cod.setTextAppearance(this, R.style.texto);
  			cod.setText(codigo);
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    cod.setLayoutParams(params);
  		    cod.setId(122);
  			vista.addView(cod);
  			
  		//Profesor Asignatura
  			TextView tituloProfesor = new TextView(vista.getContext());
  			tituloProfesor.setTextAppearance(this, R.style.textoH2);
  			tituloProfesor.setText("Profesor:");
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    tituloProfesor.setLayoutParams(params);
  		    tituloProfesor.setId(112);
  			vista.addView(tituloProfesor);
  			
  			String nombreProfesor = row.getString("Profesor");
  			String apellidosProfesor = row.getString("apellidos");
  			TextView profesor = new TextView(vista.getContext());
  			profesor.setTextAppearance(this, R.style.texto);
  			profesor.setText(nombreProfesor+' '+apellidosProfesor);
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    profesor.setLayoutParams(params);
  		    profesor.setId(122);
  			vista.addView(profesor);
  			
  		//Mail Profesor
  			TextView titulomail = new TextView(vista.getContext());
  			titulomail.setTextAppearance(this, R.style.textoH2);
  			titulomail.setText("Mail:");
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    titulomail.setLayoutParams(params);
  		    titulomail.setId(112);
  			vista.addView(titulomail);
  			
  			String mail = row.getString("mail");
  			TextView vistamail = new TextView(vista.getContext());
  			vistamail.setTextAppearance(this, R.style.texto);
  			vistamail.setText(mail);
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    vistamail.setLayoutParams(params);
  		    vistamail.setId(122);
  			vista.addView(vistamail);
  			
  		//Informacion Asignatura
  			TextView tituloInfo = new TextView(vista.getContext());
  			tituloInfo.setTextAppearance(this, R.style.textoH2);
  			tituloInfo.setText("Informacion:");
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    tituloInfo.setLayoutParams(params);
  		    tituloInfo.setId(112);
  			vista.addView(tituloInfo);
  			
  			String info = row.getString("Informacion");
  			TextView vistainfo = new TextView(vista.getContext());
  			vistainfo.setTextAppearance(this, R.style.texto);
  			vistainfo.setText(info);
  			params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		    params.setMargins(0, 3, 0, 0);
  		    vistainfo.setLayoutParams(params);
  		    vistainfo.setId(122);
  			vista.addView(vistainfo);
  			
	  			
	  			return true;
 		  }else{
  				return false;
  			}
		
	}
	//vibra y muestra un Toast
    public void err_info(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Error al mostrar la asignatura", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
	/*		CLASE ASYNCTASK
	 * 
	 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
	 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
	 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
	 * ademas observariamos el mensaje de que la app no responde.     
	 */
	    
	    class asyncbotones extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(ActividadPrivadaActual.this);
	            pDialog.setMessage("Actividades Privadas....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (actividadPrivadaActual()==true){    		    		
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
					try {
						muestraInformacion();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }else{
	            	err_info();
	            }
	            
	                									}
			
	        }
	
}
