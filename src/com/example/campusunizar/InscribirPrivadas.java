package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE INSCRIPCIÓN EN ACTIVIDADES PRIVADAS
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class InscribirPrivadas extends Activity{
	
	TextView textUser;
	String usuario;
	//Actividad seleccionada para ser inscrito
	//int idActividad;
	
	//Layout donde vamos a pintar los checkBox
	LinearLayout layout_botones;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
	private ProgressDialog pDialog;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    //private ProgressDialog pDialog;
    Httppostaux post;
	
    String URL_connect;
    String URL_connect1;
    
	String directorio="/campusUnizar/inscribir_privadas.php";
	String directorio1="/campusUnizar/inscribir_privadas1.php";
	
	//Array donde vamos a guardar los datos que nos lleguen del WebService
    JSONArray jdata;
    JSONArray jdata1;
    
	
	//Comentario para probar con git
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscribirprivadas);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		URL_connect1= post.getURL(directorio1);
		
		//Obtenemos datos extra
		Bundle bundle=getIntent().getExtras();
		
		//Texto usuario
		usuario = bundle.getString("user");
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
	    
	    new asyncbotones().execute();
	};
	
	public boolean inscribirPrivadas(){
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("User",usuario));

  		//Se realiza la petición
  		jdata= post.getserverdata(postparameters2send, URL_connect);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
		
	}
	
	public boolean sendPeticionActividad(int idAsignatura,String usuario) throws JSONException{
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("idAsignatura",Integer.toString(idAsignatura)));
  		postparameters2send.add(new BasicNameValuePair("user",usuario));

  		//Se realiza la petición
  		Log.e("inscribirPrivadas","url_connect1= "+URL_connect1);
  		jdata1= post.getserverdata(postparameters2send, URL_connect1);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata1!=null && jdata1.length() > 0){
  			JSONObject row = jdata1.getJSONObject(0);
  			String resultado = row.getString("Resultado");
  			if (resultado.equals("true"))
  				return true;
  			else if (resultado.equals("false"))
				return false;
  		}
  		return false;
	}
	
	public void infoInscripcion(){
		
		
	}
	
	public void dibujaChecks() throws JSONException{
		layout_botones= (LinearLayout)findViewById(R.id.checkBoxInscribir);
		
		
  		if (jdata!=null && jdata.length() > 0){

  			for (int i = 0; i < jdata.length(); i++) 
  			{
  				JSONObject row = jdata.getJSONObject(i);
  				String text;
  				
			    text = row.getString("NombreAsignatura").replace("\"", "");
			    final int idActividad = row.getInt("idAsignatura");
  			    TableRow tablerow =new TableRow(this);
  			    tablerow.setId(i);
  			    tablerow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
  			    final CheckBox checkBox = new CheckBox(this);
  			    checkBox.setTextAppearance(this, R.style.texto_Checkbox);
  			    checkBox.setId(i);
  			    checkBox.setText(text);
  			    tablerow.addView(checkBox); 
  			    
  			    checkBox.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
						if(checkBox.isChecked()){
							new asyncPeticionInscripcion().execute(idActividad);
						}
					}

				});
					layout_botones.addView(tablerow);
  			}
		    
  		  }else{
  			Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
  		    vibrator.vibrate(200);
  		    Toast toast1 = Toast.makeText(getApplicationContext(),"No tienes actividades pendientes de inscribir", Toast.LENGTH_SHORT);
  	 	    toast1.show();
  		  }
  			  
		
	}
	
	//vibra y muestra un Toast
    public void err_info(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"No hay actividades Privadas pendientes", Toast.LENGTH_SHORT);
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
	            pDialog = new ProgressDialog(InscribirPrivadas.this);
	            pDialog.setMessage("Actividades sin inscribir....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (inscribirPrivadas()==true){    		    		
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
						dibujaChecks();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }else{
	            	err_info();
	            }
	         }
			
	     }
	    
	    /*		CLASE ASYNCTASK
		 * 
		 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
		 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
		 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
		 * ademas observariamos el mensaje de que la app no responde.     
		 */
		    
		    class asyncPeticionInscripcion extends AsyncTask<Integer, String, String > {
		    	 
		        @Override
				protected void onPreExecute() {
		        	//para el progress dialog
		            pDialog = new ProgressDialog(InscribirPrivadas.this);
		            pDialog.setMessage("Actividades sin inscribir....");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(false);
		            pDialog.show();
		        }
		 
				@Override
				protected String doInBackground(Integer... params) {
					
					//enviamos y recibimos y analizamos los datos en segundo plano.
		    		try {
						if (sendPeticionActividad(params[0],usuario)==true){    		    		
							return "ok"; //login valido
						}else{    		
							return "err"; //login invalido     	          	  
						}
					} catch (JSONException e) {
						e.printStackTrace();
						return "err";
					}
		        	
				}
		       
				/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
				 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
		        @Override
				protected void onPostExecute(String result) {

		           pDialog.dismiss();//ocultamos progess dialog.
		           Log.e("onPostExecute=",""+result);
		           
		           if (result.equals("ok")){
		        	   Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(200);
						Toast toast1 = Toast.makeText(getApplicationContext(),"Actividad Inscrita", Toast.LENGTH_SHORT);
						toast1.show();
						//Refrescamos la actividad para que se muestren las actividades no inscritas
						finish();
						startActivity(getIntent());
		            }else{
		            	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(200);
						Toast toast1 = Toast.makeText(getApplicationContext(),"Error de inscripción", Toast.LENGTH_SHORT);
						toast1.show();
		            }
		            
		                									}
				
		        }


	
}
