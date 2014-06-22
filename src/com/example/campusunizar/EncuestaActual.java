package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE UNA ENCUESTA
 */
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//Acceso a MySQl

public class EncuestaActual extends Activity implements View.OnClickListener{
	Connection conexionMySQL;//Variable de conexión
    
	  //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
	    private ProgressDialog pDialog1;
	    private ProgressDialog pDialog2;
	    Httppostaux post;
	    
	    // String URL_connect
	    String directorio="/campusUnizar/encuesta_actual.php";
	    String directorioGuardar="/campusUnizar/guardar_encuesta.php";
	    String directorioActualizar="/campusUnizar/actualiza_encuesta_realizada.php";
	    String URL_connect;
	    String URL_guardar;
	    String URL_actualizar;
	    
	    //Para almacenar los datos de la encuesta
	    List<String> datos= new ArrayList<String>();
	    
	    //Para almacenar los id de los grupos de radioButon y poder saber cual está marcado dentro del grupo al guardar (método onClick) 
	    List<Integer> idRespuestas= new ArrayList<Integer>();
	    
	    //Para realizar los insert
	    String idEncuesta="";
    	List<String> preguntas= new ArrayList<String>();
    	List<String> respuestas= new ArrayList<String>();
    	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.encuesta_actual);
			//Manejador del envío de peticiones
			post=new Httppostaux();
			URL_connect= post.getURL(directorio);
			URL_guardar= post.getURL(directorioGuardar);
			URL_actualizar= post.getURL(directorioActualizar);
			new asynclogin().execute();        		               
		}
		
		//Ejecución desde onCreate
		class asynclogin extends AsyncTask< String, String, String > {
		   	 
	    	String user,pass;
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog1 = new ProgressDialog(EncuestaActual.this);
	            pDialog1.setMessage("Validando...");
	            pDialog1.setIndeterminate(false);
	            pDialog1.setCancelable(false);
	            pDialog1.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				Bundle bundle=getIntent().getExtras();
				String idAct= bundle.getString("actividad");
				preguntasEncuesta(idAct);
				if (datos.size()>0)
					return "si";
				else
					return "no";
			}
	       
	        @Override
			protected void onPostExecute(String result) {
	        	if (result.equals("si"))
	        		rellenarInformacion(datos);
	        	else
	        		err_enc();
		        pDialog1.dismiss();//ocultamos progess dialog.
	        }
	    }
		
		//Recoge la información sobre la encuesta de la BBDD
		public void preguntasEncuesta(String idAct){
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("id_actividad",idAct));

			//realizamos una peticion y como respuesta obtenes un array JSON
			JSONArray jdata=post.getserverdata(postparameters2send,URL_connect);
			List<String> datosAux= new ArrayList<String>();
			for(int i=0; i<jdata.length(); i++){
				try {
					JSONObject row = jdata.getJSONObject(i);
					datosAux.add(row.getString("titulo")+"&"+row.get("pregunta").toString()+"&"+row.get("idEncuesta").toString()+"&"+row.get("idPregunta").toString());			
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			datos=datosAux;
		}
		
		//Muestra la encuesta en el layout
		public void rellenarInformacion(List<String> infoEncuesta){
			RelativeLayout vista = (RelativeLayout)findViewById(R.id.infoEncuesta);
			Bundle bundle=getIntent().getExtras();
			String usuario;
			TextView textUser= (TextView) findViewById(R.id.usuarioEncAct);
    		try{
    			usuario=bundle.getString("usuario");
    		}catch(Exception e){
    			usuario="";
    		}
    		if(!usuario.equals("")){
    		    textUser.setText("Usuario: " + usuario);
    		}
    		String[] infoPregunta;
    		String nombre, pregunta="";
    		TextView titulo = new TextView(vista.getContext());
    		RadioGroup grupoRespuestas=new RadioGroup(vista.getContext());
    		RelativeLayout.LayoutParams params;
    		int i;
    		for(i=0; i<infoEncuesta.size(); i++){
    			infoPregunta=infoEncuesta.get(i).split("&");
    			if(i==0){
    				idEncuesta=infoPregunta[2];
    				nombre=infoPregunta[0];
    			//Titulo de encuesta
    				
    				titulo.setTextAppearance(this, R.style.textoH1);
    				titulo.setText(nombre); 
    				params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    				params.addRule(RelativeLayout.BELOW,textUser.getId());
    				titulo.setLayoutParams(params);
    				titulo.setId(Integer.parseInt("10"+i));
    				vista.addView(titulo);
    			}
			//Pregunta de encuesta
				TextView preguntaTitulo = new TextView(vista.getContext());
				preguntaTitulo.setTextAppearance(this, R.style.textoH2);
				pregunta=infoPregunta[1];
				preguntaTitulo.setText(pregunta);
				params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				if(i==0)
					params.addRule(RelativeLayout.BELOW,titulo.getId());
				else
					params.addRule(RelativeLayout.BELOW,grupoRespuestas.getId());
			    params.setMargins(0, 3, 0, 0);
			    preguntaTitulo.setLayoutParams(params);
			    preguntaTitulo.setId(Integer.parseInt("11"+i));
				vista.addView(preguntaTitulo);
			//Respuestas de encuesta
				grupoRespuestas=new RadioGroup(vista.getContext());
				grupoRespuestas.setId(Integer.parseInt("12"+i));
				params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.BELOW,preguntaTitulo.getId());
				grupoRespuestas.setLayoutParams(params);
				grupoRespuestas.setOrientation(RadioGroup.HORIZONTAL);
				Integer valorRespuesta;
				for(int j=0; j<5; j++){
					valorRespuesta=j+1;
					RadioButton rbResp=new RadioButton(vista.getContext());
					params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					rbResp.setLayoutParams(params);
					rbResp.setTextAppearance(this, R.style.texto_Checkbox);
					rbResp.setText(valorRespuesta.toString());
					rbResp.setId(Integer.parseInt("121"+j));
					grupoRespuestas.addView(rbResp);
				}
				idRespuestas.add(grupoRespuestas.getId());
				grupoRespuestas.setTag(infoPregunta[3]);
				vista.addView(grupoRespuestas);
			//fin Respuestas
    		}

		//Boton guardar
			Button guardar;
			guardar = new Button(vista.getContext());
			guardar.setText("Guardar Encuesta");
			params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,grupoRespuestas.getId());
		    params.setMargins(0, 3, 0, 0);
		    guardar.setLayoutParams(params);
		    guardar.setClickable(true);
		    guardar.setOnClickListener(this);
		    guardar.setBackgroundColor(Color.parseColor("#2d6898"));
		    guardar.setTextAppearance(this, R.style.boton);
		    vista.addView(guardar);
	    }
		
		//Error que se muestra si no se ha podido cargar la encuesta
		//vibra y muestra un Toast
	    public void err_enc(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error al cargar la encuesta", Toast.LENGTH_SHORT);
	 	    toast1.show();
	    }
	    
		//Error que se muestra si no se ha respondido a todas las preguntas de la encuesta al guardar
		//vibra y muestra un Toast
	    public void err_guardar(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Debes responder a todas las preguntas para guardar la encuesta", Toast.LENGTH_SHORT);
	 	    toast1.show();
	    }
	    
	    //Error que se muestra si no se ha podido guardar la encuesta
	    //vibra y muestra un Toast
	    public void err_insert(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error al guardar la encuesta", Toast.LENGTH_SHORT);
	 	    toast1.show();
	    }
	    
	    
	    //método que se ejecuta al pulsar el botón guardar
	    @Override
		public void onClick(View v) {
	    	Boolean error=false;
	    	for(int i=0; i<idRespuestas.size(); i++){
	    		RadioGroup grupoRespuestas=((RadioGroup)findViewById(idRespuestas.get(i)));
	    		try{
	    			String respuesta=((RadioButton)findViewById(grupoRespuestas.getCheckedRadioButtonId())).getText().toString();
	    			respuestas.add(respuesta);
	    			String idPregunta=grupoRespuestas.getTag().toString();
	    			preguntas.add(idPregunta);
	    		}catch (Exception e) {
	    			err_guardar();
	    			error=true;
	    			break;
				}	    		
	    	}
	    	if(!error){
	    		new asyncGuardar().execute();
	    	}
	    }
	    
	    
	    public void guardar_encuesta(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Encuesta guardada", Toast.LENGTH_SHORT);
	 	    toast1.show();
	    }
	    
	    class asyncGuardar extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog2 = new ProgressDialog(EncuestaActual.this);
	            pDialog2.setMessage("Guardando Encuesta....");
	            pDialog2.setIndeterminate(false);
	            pDialog2.setCancelable(false);
	            pDialog2.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				String guardada=guardarEncuesta();
				if (guardada.equals("no"))
					return "no";
				else
					return"si";
			}
	       
			/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
			 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
	        @Override
			protected void onPostExecute(String result) {
	           pDialog2.dismiss();
	           Log.e("onPostExecute=",""+result);
	           if(result.equals("si")){
	        	   guardar_encuesta();
	           }else
	        	   err_insert();
	        }
	    }
	    
	    //método que guarda las respuestas 
	    public String guardarEncuesta(){
			String guardada="si";
			for(int i=0; i<preguntas.size(); i++){
				ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
				postparameters2send.add(new BasicNameValuePair("id_pregunta",preguntas.get(i)));
				postparameters2send.add(new BasicNameValuePair("id_encuesta",idEncuesta));
				Bundle bundle=getIntent().getExtras();
				String idAct= bundle.getString("actividad");
				postparameters2send.add(new BasicNameValuePair("id_actividad",idAct));
				String idRespuesta="resp"+respuestas.get(i).toString();
				postparameters2send.add(new BasicNameValuePair("respuesta",idRespuesta));
	
				try {
					//realizamos la insercion de la encuesta
					post.getserverdata(postparameters2send,URL_guardar);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					guardada="no";
					e.printStackTrace();
				}
			}
			if(guardada.equals("si"))
					new asyncActualizar().execute();
	    	return guardada;
		}
	    
	    class asyncActualizar extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				String actualizado=actualizarEncuesta();
				if (actualizado.equals("no"))
					return "no";
				else
					return"si";
			}
	       
			/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
			 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
	        @Override
			protected void onPostExecute(String result) {
	           Log.e("onPostExecute=",""+result);
	           if(result.equals("si")){
	        	   guardar_encuesta();
	        	   lanzarIntent();
	           }else
	        	   err_insert();
	        }
	    }
	    
	  //método que actualiza que el usuario ha realizado la encuesta
	    public String actualizarEncuesta(){
			String acualizado="si";
			Bundle bundle=getIntent().getExtras();
			String usuario=bundle.getString("usuario");
			String idAct= bundle.getString("actividad");
			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("usuario",usuario));
			postparameters2send.add(new BasicNameValuePair("id_actividad",idAct));

			try {
				//realizamos la insercion de la encuesta
				post.getserverdata(postparameters2send,URL_actualizar);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				acualizado="no";
				e.printStackTrace();
			}
	    	return acualizado;
		}
	    public void lanzarIntent(){
	    	Intent in = new Intent(this,Encuestas.class);
	    	Bundle bundle=getIntent().getExtras();
	        String usuario = bundle.getString("usuario");
			in.putExtra("user", usuario);
			this.finish();
		    startActivity(in);
	    }
	}