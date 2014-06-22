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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Alertas extends Activity {

	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
	Httppostaux post;//Manejador de peticiones
	
		//String URL_connect
    String directorio="/campusUnizar/alertas.php";
    String URL_connect;
    
    //Array donde vamos a guardar las asignaturas con Alertas
    JSONArray jdata;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertas);
		
		//Manejador del envío de peticiones
	    post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		//Clase ASYNCTASK que se encarga de la conexión con bbdd
		new asyncbotones().execute();
		
	}
	
	public boolean consultaActividadesAlerta(){
		//En este caso el array de parámetros va vacio
  		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("actividad","alertas"));
  		//Se realiza la petición
		//Busca las asignaturas que tienen alertas	
  		jdata= post.getserverdata(postparameters2send, URL_connect);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
		
	}
	
	public void muestraAsignaturasconAlertas(){
		LinearLayout layout_botones= (LinearLayout)findViewById(R.id.linearlayout_buttons);
		//JSONObject json_data; //creamos un objeto JSON
			int i = 0;
			
  			while (i <= jdata.length()){
  				try {
  					JSONObject row = jdata.getJSONObject(i);
  					String nombreActividad = row.getString("nombre");
  					final String id_Actividad = row.getString("id_actividad");
  					//replaceAll("\"", "");
  					//En el array hemos metido en cada indice Nombre&idAsignatura, vamos a separarlo
  					 //datos = nombreAsignatura.split("&");
  					 Button newButton = new Button(this);
  				     newButton.setText(nombreActividad);//Nombre de la actividad con Alertas
  				     newButton.setContentDescription(id_Actividad);
  				     newButton.setBackgroundColor(Color.parseColor("#2d6898"));
  				     LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
  				     params.setMargins(0, 3, 0, 0);
  				     newButton.setLayoutParams(params);
  			         newButton.setTextColor(Color.parseColor("#ffffff"));
  			         newButton.setClickable(true);
  			         final Intent in = new Intent(this,AlertasActividad.class);
  			         newButton.setOnClickListener(new View.OnClickListener() {
	
						@Override
						public void onClick(View view) {
							
							in.putExtra("idActividad", id_Actividad);
						    startActivity(in);
						}
	
					});
  					 layout_botones.addView(newButton);
  					 Log.i("Alertas",nombreActividad);//muestro por log que obtuvimos
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}	
  				i=i+1;
  			}
	}
	
	//vibra y muestra un Toast
    public void err_privadasAlertas(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"No hay alertas actualmente!!", Toast.LENGTH_SHORT);
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
	            pDialog = new ProgressDialog(Alertas.this);
	            pDialog.setMessage("Buscando Alertas....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (consultaActividadesAlerta()==true){    		    		
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
					muestraAsignaturasconAlertas();
	            }else{
	            	err_privadasAlertas();
	            }
	            
	                									}
			
	        }
}
