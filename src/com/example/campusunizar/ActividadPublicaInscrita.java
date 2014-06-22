package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE UNA ACTIVIDAD PÚBLICA INSCRITA
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
//Acceso a MySQl
import android.widget.Toast;

public class ActividadPublicaInscrita extends Activity implements View.OnClickListener{
	Connection conexionMySQL;//Variable de conexión
    
	  //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
	    private ProgressDialog pDialog;
	    Httppostaux post;
	    
	    // String URL_connect
	    String directorio="/campusUnizar/actividad_inscrita.php";
	    String directorioAnular="/campusUnizar/anular_actividad.php";
	    String URL_connect;
	    String URL_anular;
	    
	    int creditos;
	    int maxCreditos;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.actividad_actual);
			//Manejador del envío de peticiones
			post=new Httppostaux();
			URL_connect= post.getURL(directorio);
			URL_anular= post.getURL(directorioAnular);
			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
			new asynclogin().execute();        		               
		}
		
		 public boolean checklogindata(String username ,String password ){
		    	
		    if 	(username.equals("") || password.equals("")){
		    	Log.e("Login ui", "checklogindata user or pass error");
		    return false;
		    
		    }else{
		    	
		    	return true;
		    }
	   	}
	    
		//vibra y muestra un Toast
	    public void err_activ(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error: Ya estás inscrito en ésta actividad", Toast.LENGTH_SHORT);
	 	    toast1.show();
	 	   rellenarInformacion();
	    }
	    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
	    
	    public void rellenarInformacion(){
			RelativeLayout vista = (RelativeLayout)findViewById(R.id.activ_pub_act);
			Bundle bundle=getIntent().getExtras();
			String extras= bundle.getString("actividad");
			String usuario;
			TextView textUser= (TextView) findViewById(R.id.text_user_pub);
    		try{
    			usuario=bundle.getString("user");
    		}catch(Exception e){
    			usuario="";
    		}
    		if(!usuario.equals("")){
    		    textUser.setText("Usuario: " + usuario);
    		}
			String[] datos=extras.split("&");
			String actividad=datos[0];
		//Titulo de actividad
			TextView titulo = new TextView(vista.getContext());
			titulo.setTextAppearance(this, R.style.textoH1);
			titulo.setText(actividad); 
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,textUser.getId());
			titulo.setLayoutParams(params);
			titulo.setId(111);
			vista.addView(titulo);
		//Fecha y Hora de actrividad
			TextView fechaTitulo = new TextView(vista.getContext());
			fechaTitulo.setTextAppearance(this, R.style.textoH2);
			fechaTitulo.setText("-Fecha / Hora:");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,titulo.getId());
		    params.setMargins(0, 3, 0, 0);
		    fechaTitulo.setLayoutParams(params);
		    fechaTitulo.setId(112);
			vista.addView(fechaTitulo);
			
			String fecha=datos[1];
			String hora=datos[2];
			TextView fechaHora = new TextView(vista.getContext());
			fechaHora.setTextAppearance(this, R.style.texto);
			fechaHora.setText(fecha+" / "+hora);
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,fechaTitulo.getId());
		    params.setMargins(0, 3, 0, 0);
		    fechaHora.setLayoutParams(params);
		    fechaHora.setId(122);
			vista.addView(fechaHora);
		//fin Fecha y Hora
		//Lugar de actrividad
			TextView LugarTitulo = new TextView(vista.getContext());
			LugarTitulo.setTextAppearance(this, R.style.textoH2);
			LugarTitulo.setText("-Lugar:");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,fechaHora.getId());
		    params.setMargins(0, 3, 0, 0);
		    LugarTitulo.setLayoutParams(params);
		    LugarTitulo.setId(113);
			vista.addView(LugarTitulo);
			
			String Sitio=datos[3];
			TextView Lugar = new TextView(vista.getContext());
			Lugar.setTextAppearance(this, R.style.texto);
			Lugar.setText(Sitio);
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,LugarTitulo.getId());
		    params.setMargins(0, 3, 0, 0);
		    Lugar.setLayoutParams(params);
		    Lugar.setId(123);
			vista.addView(Lugar);
		//fin Lugar
		//Duracion de actrividad
			TextView TiempoTitulo = new TextView(vista.getContext());
			TiempoTitulo.setTextAppearance(this, R.style.textoH2);
			TiempoTitulo.setText("-Duración:");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,Lugar.getId());
		    params.setMargins(0, 3, 0, 0);
		    TiempoTitulo.setLayoutParams(params);
		    TiempoTitulo.setId(114);
			vista.addView(TiempoTitulo);
			
			String duracion=datos[4];
			TextView Tiempo = new TextView(vista.getContext());
			Tiempo.setTextAppearance(this, R.style.texto);
			Tiempo.setText(duracion+" Horas");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,TiempoTitulo.getId());
		    params.setMargins(0, 3, 0, 0);
		    Tiempo.setLayoutParams(params);
		    Tiempo.setId(124);
			vista.addView(Tiempo);
		//fin Duración
		//Información de la actividad
			TextView infoTitulo = new TextView(vista.getContext());
			infoTitulo.setTextAppearance(this, R.style.textoH2);
			infoTitulo.setText("-Información:");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,Tiempo.getId());
		    params.setMargins(0, 3, 0, 0);
		    infoTitulo.setLayoutParams(params);
		    infoTitulo.setId(115);
			vista.addView(infoTitulo);
			
			String infor=datos[5];
			TextView informacion = new TextView(vista.getContext());
			informacion.setTextAppearance(this, R.style.texto);
			informacion.setText(infor);
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,infoTitulo.getId());
		    params.setMargins(0, 3, 0, 0);
		    informacion.setLayoutParams(params);
		    informacion.setId(125);
			vista.addView(informacion);
		//fin de información
			
		//INSCRITA
			TextView inscrita;
			inscrita = new TextView(vista.getContext());
			inscrita.setTextAppearance(this, R.style.textoRojo);
			inscrita.setText("Actividad inscrita");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,informacion.getId());
		    params.setMargins(0, 3, 0, 0);
		    inscrita.setLayoutParams(params);
		    inscrita.setId(116);
			vista.addView(inscrita);
		//FIN INSCRITA
			Button cancelarInsc;
			cancelarInsc = new Button(vista.getContext());
			cancelarInsc.setText("Anular Inscripción");
			cancelarInsc.setContentDescription(extras);
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,inscrita.getId());
		    params.setMargins(0, 3, 0, 0);
		    cancelarInsc.setLayoutParams(params);
		    cancelarInsc.setClickable(true);
		    cancelarInsc.setOnClickListener(this);
		    cancelarInsc.setBackgroundColor(Color.parseColor("#2d6898"));
		    cancelarInsc.setTextAppearance(this, R.style.boton);
		    vista.addView(cancelarInsc);
	    }
	    public String actividadInscrita(String actividad, String usuario){
			String inscrita="";
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("usuario",usuario));
			postparameters2send.add(new BasicNameValuePair("actividad",actividad));

			//realizamos una peticion y como respuesta obtenes un array JSON
			JSONArray jdata=post.getserverdata(postparameters2send,URL_connect);

			try {
				JSONObject row = jdata.getJSONObject(1);
				inscrita = row.getString("inscrita");
				if (inscrita.equals("no")){
					row = jdata.getJSONObject(2);
					creditos = Integer.parseInt(row.getString("creditos"));
					maxCreditos = Integer.parseInt(row.getString("maxCreditos"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return inscrita;
		}
	    
	    class asynclogin extends AsyncTask< String, String, String > {
	   	 
	    	String user,pass;
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(ActividadPublicaInscrita.this);
	            pDialog.setMessage("Validando...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				Bundle bundle=getIntent().getExtras();
				String extras= bundle.getString("actividad");
				String[] datos=extras.split("&");
				String usuario=bundle.getString("user");
				String idAct=datos[6];
				String yaInscrita=actividadInscrita(idAct,usuario);
				if (yaInscrita.equals("no")){
					return "no";
				}else
					return"si";
			}
	       
			/*Una vez terminado doInBackground segun lo que halla ocurrido 
			pasamos a la sig. activity
			o mostramos error*/
	        @Override
			protected void onPostExecute(String result) {
	        	if (result=="no"){
	        		if(maxCreditos<=creditos)
						mostrarPopUp();
	        		rellenarInformacion();
	        	}else
	        		err_activ();
		        pDialog.dismiss();//ocultamos progess dialog.
	        }
	    }
	    @Override
		public void onClick(View v) {
	    	String actividad=v.getContentDescription().toString();
			Intent in = new Intent(this,ActividadPublicaActual.class);
			in.putExtra("actividad", actividad);
			in.putExtra("anular", "si");
			new asyncanular().execute();
			this.finish();
		    startActivity(in);
	    }
	    
	    public String anularInscripcion(String actividad, String usuario){
			String anulada="";
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("usuario",usuario));
			postparameters2send.add(new BasicNameValuePair("actividad",actividad));

			//realizamos una peticion y como respuesta obtenes un array JSON
			JSONArray jdata=post.getserverdata(postparameters2send,URL_anular);

			try {
				JSONObject row = jdata.getJSONObject(0);
				anulada = row.getString("anulada");			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return anulada;
		}
	    public void anular_activ(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Anulada la inscripción a esta asignatura", Toast.LENGTH_SHORT);
	 	    toast1.show();
	    }
	    class asyncanular extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(ActividadPublicaInscrita.this);
	            pDialog.setMessage("Anulando inscripción....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				Bundle bundle=getIntent().getExtras();
				String extras= bundle.getString("actividad");
				String[] datos=extras.split("&");
				String usuario=bundle.getString("user");
				String idAct=datos[6];
				String anulada=anularInscripcion(idAct,usuario);
				if (anulada.equals("no"))
					return "no";
				else
					return"si";
	        	
			}
	       
			/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
			 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
	        @Override
			protected void onPostExecute(String result) {

	           pDialog.dismiss();//ocultamos progess dialog.
	           Log.e("onPostExecute=",""+result);
	           if(result.equals("si"))
	        	   anular_activ();
	        }
			
	    }
	    
	    public void mostrarPopUp(){
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    	alert.setTitle("Créditos Alcanzados");
	    	String mensaje="Con esta inscripción has alcanzado tu máximo de créditos \n ¡¡ENHORABUENA!!";
	    	alert.setMessage(mensaje);
	    	alert.setCancelable(true);
	    	alert.setNeutralButton("Aceptar",null);
	    	alert.setIcon(R.drawable.ic_launcher);
	        AlertDialog alertCred = alert.create();
	        alertCred.show();
	    }
	}
