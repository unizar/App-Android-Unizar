package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE ENCUESTAS POSIBLES DE REALIZAR
 */
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

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
import android.widget.TextView;
import android.widget.Toast;


public class Encuestas extends Activity implements View.OnClickListener{
	TextView textUser;
	String usuario;
	String nombreAsignatura;
	String[] datos; 
	Connection conexionMySQL;//Variable de conexión

    //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
	Httppostaux post;//Manejador de peticiones
	
		//String URL_connect
    String directorio="/campusUnizar/encuestas.php";
    String URL_connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encuestas);
		//Manejador del envío de peticiones
	    post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		//Recogemos la información de la actividad acceso privado
		Bundle extras = getIntent().getExtras();
	    usuario = extras.getString("user");
	
		textUser = (TextView) findViewById(R.id.txtUserEncuestas);
	    textUser.setText("Usuario: " + usuario);
	  		
	    new asyncbotones().execute();
	}
	//comprobamos que el usuario tiene actividades inscritas para las que hacer encuesta
	public boolean actividadesInscritas(String usuario){
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("usuario",usuario));

  		//Se realiza la petición
  		JSONArray jdata= post.getserverdata(postparameters2send, URL_connect);
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
	}
	public void creaBotones(){
		//En este caso el array de parámetros va vacio
  		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("usuario",usuario));

  		JSONArray jdata=post.getserverdata(postparameters2send,URL_connect);
		LinearLayout vista = (LinearLayout)findViewById(R.id.botonesEncuestas);
		Button act;
    	for(int i=0; i<jdata.length(); i++){
    		
			try {
				String activAct, idActiv;
				JSONObject row = jdata.getJSONObject(i);
				activAct = row.getString("nombre");
				idActiv = row.getString("id_actividad");
				Log.e("actividad" + i,activAct);//muestro por log que obtuvimos
				act = new Button(vista.getContext());
				act.setText(activAct);
				act.setContentDescription(idActiv);
				act.setTextColor(Color.parseColor("#ffffff"));
				act.setBackgroundColor(Color.parseColor("#2d6898"));
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			    params.setMargins(0, 3, 0, 0);
			    act.setLayoutParams(params);
				act.setClickable(true);
				act.setOnClickListener(this);
				vista.addView(act);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	//vibra y muestra un Toast
    public void err_encuestas(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"No hay encuestas disponibles para sus actividades", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
    
    @Override
	public void onClick(View v) {
    	String actividad=v.getContentDescription().toString();
    	Intent in = new Intent(this,EncuestaActual.class);
		in.putExtra("actividad", actividad);
		in.putExtra("usuario", usuario);
		this.finish();
	    startActivity(in);
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
            pDialog = new ProgressDialog(Encuestas.this);
            pDialog.setMessage("Asignaturas con encuesta disponible....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		@Override
		protected String doInBackground(String... params) {
			
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (actividadesInscritas(usuario)==true){    		    		
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
				creaBotones();
            }else{
            	err_encuestas();
            }
            
                									}
		
        }
}


