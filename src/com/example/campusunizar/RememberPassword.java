package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE RECORDAR CONTRASEÑA
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class RememberPassword extends Activity{
	
	TextView textUser;
	String usuario;
	EditText respuestaUsuario;
	EditText etxtRespuesta;
	String respuestaBBDD;
	String valor;
	
	//Layout donde vamos a pintar los checkBox
	LinearLayout vista;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
	private ProgressDialog pDialog;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    //private ProgressDialog pDialog;
    Httppostaux post;
	
    String URL_connect;
    
	String directorio="/campusUnizar/rememberPassword_consulta.php";
	
	//Array donde vamos a guardar los datos que nos lleguen del WebService
    JSONArray jdata;
    
	
	//Comentario para probar con git
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remember_password);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		//Obtenemos datos extra pasados de la otra actividad
		Bundle bundle=getIntent().getExtras();
		
		//Texto usuario actual logueado
		usuario = bundle.getString("user");
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
	    new asyncPregunta().execute();
	};
	
	
	public Boolean consultarBBDDPregunta(){
		
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("usuario",usuario));
  		try
  		{
  			jdata= post.getserverdata(postparameters2send, URL_connect);
  			JSONObject row = jdata.getJSONObject(0);
  			
  			if (row.getString("Pregunta").equals("No Existe"))
			{
  				valor = "No existe el usuario";
			}
  			else
  			{
  				valor = "";
  			}
  			
  			//String preguntaBBDD = row.getString("Pregunta");
  			return true;
  		}
  		catch(Exception ex)
  		{
  			valor = "";
  			return false;
  		}
	}
	
	
	public void PintarPregunta() throws JSONException{
		
		vista= (LinearLayout)findViewById(R.id.LinearRememberPass);
		//String totalCreditos;
		
		JSONObject row = jdata.getJSONObject(0);
		
		String preguntaBBDD = row.getString("Pregunta");
		respuestaBBDD = row.getString("Respuesta");
		
		if((preguntaBBDD != null && preguntaBBDD.length() > 0) && (respuestaBBDD != null && respuestaBBDD.length() > 0))
		{
			TextView titulo = new TextView(vista.getContext());
			titulo.setTextAppearance(this, R.style.textoH1);
			titulo.setText("Cambio Contraseña"); 
	  		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	  		titulo.setLayoutParams(params);
	  		titulo.setId(111);
	  		vista.addView(titulo);
			
	  		TableRow tablerow1 = new TableRow(this);
			tablerow1.setId(112);
			tablerow1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
	  		
			TextView txtpregunta = new TextView(vista.getContext());
			txtpregunta.setTextAppearance(this, R.style.textoH2);
			txtpregunta.setText("Pregunta:  "); 
	  		txtpregunta.setId(111);
	  		
	  		TextView txtpreguntaBBDD = new TextView(vista.getContext());
	  		txtpreguntaBBDD.setTextAppearance(this, R.style.texto);
	  		txtpreguntaBBDD.setText(preguntaBBDD); 
	  		txtpreguntaBBDD.setId(111);

	  		
			tablerow1.addView(txtpregunta);
			tablerow1.addView(txtpreguntaBBDD);
			vista.addView(tablerow1);
	  		
	  		TableRow tablerow = new TableRow(this);
			tablerow.setId(112);
			tablerow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			

			TextView txtrespuesta = new TextView(vista.getContext());
	  		txtrespuesta.setTextAppearance(this, R.style.textoH2);
	  		txtrespuesta.setText("Respuesta: "); 
	  		txtrespuesta.setId(111);
			
			
			//Para mostrar el numero de creditos relativo a la actividad
	  		respuestaUsuario = new EditText(vista.getContext());
	  		respuestaUsuario.setTextAppearance(this, R.style.texto);
	  		respuestaUsuario.setId(113);
	  		
			
			//Para añadirlo a la vista original
			tablerow.addView(txtrespuesta);
			tablerow.addView(respuestaUsuario);
			vista.addView(tablerow);
	  		
			
			Button btnComprobarRespuesta = new Button (vista.getContext());
			btnComprobarRespuesta.setText("Comprobar");
			btnComprobarRespuesta.setTextColor(Color.parseColor("#ffffff"));
			btnComprobarRespuesta.setBackgroundColor(Color.parseColor("#2d6898"));
			LinearLayout.LayoutParams paramet=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			paramet.setMargins(0, 15, 0, 15);
			btnComprobarRespuesta.setLayoutParams(paramet);
			btnComprobarRespuesta.setClickable(true);
			btnComprobarRespuesta.setOnClickListener(new View.OnClickListener() {
						@Override
							public void onClick(View view) {	
							
								etxtRespuesta = (EditText) findViewById(respuestaUsuario.getId());
								String respuestatxt = etxtRespuesta.getText().toString();
								
								if (respuestaBBDD.toUpperCase().equals(respuestatxt.toUpperCase()))
								{
									//Se trata de la misma pregunta --> Lanzamos nueva vista para que modifique la actividad
									Intent i = new Intent(RememberPassword.this, ChangePassword.class);
									i.putExtra("user", usuario);
									startActivity(i);
									finish();
								}
								else
								{
							    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								    vibrator.vibrate(200);
								    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:La respuesta no es correcta", Toast.LENGTH_SHORT);
							 	    toast1.show();  
									
								}
						}

					});
			vista.addView(btnComprobarRespuesta); 
	  		
		}
	}
	
	//vibra y muestra un Toast
    public void err_info(String mensaje){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
	
	/*		CLASE ASYNCTASK
	 * 
	 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
	 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
	 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
	 * ademas observariamos el mensaje de que la app no responde.     
	 */
	    
	    class asyncPregunta extends AsyncTask< String, String, String > {
	    	 
	        @Override
			protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(RememberPassword.this);
	            pDialog.setMessage("Pregunta ....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (consultarBBDDPregunta()==true){    		    		
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
					try 
					{
						//String prueba = valor;
						if (valor == "")
							PintarPregunta();
						else
						{
							err_info("No existe el usuario");
							finish();
						}
						
					} 
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }else
	            {
	            	err_info("Error:Error al mostrar la pregunta");
	            	finish();
	            }
	            
			}
			
	        }	    
}