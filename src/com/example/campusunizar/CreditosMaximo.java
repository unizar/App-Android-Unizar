package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE CRÉDITOS MÁXIMOS
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CreditosMaximo extends Activity{
	
	TextView textUser;
	String usuario;
	//Actividad seleccionada para ser inscrito
	String newTotalCreditos;
	EditText etxtTotalCreditos;
	EditText txtCreditos;
	
	//Layout donde vamos a pintar los checkBox
	LinearLayout vista;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
	private ProgressDialog pDialog;
	
	//Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    //private ProgressDialog pDialog;
    Httppostaux post;
	
    String URL_connectMod;
    String URL_connectCon;
    
	String directorioMod="/campusUnizar/max_creditos_modificar.php";
	String directorioCon="/campusUnizar/max_creditos_consultar.php";
	
	//Array donde vamos a guardar los datos que nos lleguen del WebService
    JSONArray jdataCon;
    JSONArray jdataMod;
    
	
	//Comentario para probar con git
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditos_maximo);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connectMod= post.getURL(directorioMod);
		URL_connectCon= post.getURL(directorioCon);
		
		//Obtenemos datos extra pasados de la otra actividad
		Bundle bundle=getIntent().getExtras();
		
		//Texto usuario actual logueado
		usuario = bundle.getString("user");
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
	    new asyncbotones().execute();
	};
	
	/*@Override
	public void onClick(View v) {
		
		etxtTotalCreditos = (EditText) findViewById(txtCreditos.getId());
		newTotalCreditos=etxtTotalCreditos.getText().toString();
		new asyncGuardarCreditos().execute();
    }*/
	
	public boolean consultarCreditos(){
		
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("User",usuario));
  		try
  		{
  			jdataCon= post.getserverdata(postparameters2send, URL_connectCon);
  			return true;
  		}
  		catch(Exception ex)
  		{
  			return false;
  		}
	}
	
	public boolean guardarCreditos(String newTotalCreditos,String usuario) throws JSONException{
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("totalCreditos",newTotalCreditos));
  		postparameters2send.add(new BasicNameValuePair("user",usuario));

  		//Se realiza la petición
  		Log.e("inscribirPrivadas","url_connect1= "+URL_connectMod);
  		jdataMod= post.getserverdata(postparameters2send, URL_connectMod);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdataMod!=null && jdataMod.length() > 0){
  			JSONObject row = jdataMod.getJSONObject(0);
  			String resultado = row.getString("Resultado");
  			if (resultado.equals("true"))
  				return true;
  			else if (resultado.equals("false"))
				return false;
  		}
  		return false;
	}
	
	
	public void PintarInformacion() throws JSONException{
		
		vista= (LinearLayout)findViewById(R.id.LinearCreditosMaximos);
		String totalCreditos;
		
		
		if (jdataCon.getString(0).equals("null"))
		{
			totalCreditos = "0";
		}
		else
		{
			totalCreditos = jdataCon.getString(0);
		}
		
		totalCreditos = totalCreditos.replace('"',' ');
		totalCreditos = totalCreditos.replaceAll(" ","");
		
  		TextView titulo = new TextView(vista.getContext());
  		titulo.setTextAppearance(this, R.style.textoH1);
  		titulo.setText("Créditos Máximos "); 
  		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
  		titulo.setLayoutParams(params);
  		titulo.setId(111);
  		vista.addView(titulo);
  		
  		//Row para mostrar titulo y creditos en una misma linea
		TableRow tablerow = new TableRow(this);
		tablerow.setId(112);
		tablerow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		
		//Para mostrar el titulo "Creditos"
		TextView lbCreditos = new TextView(vista.getContext());
		lbCreditos.setTextAppearance(this, R.style.texto);
		lbCreditos.setText("Total Creditos: ");
		lbCreditos.setId(112);
		
		//Para mostrar el numero de creditos relativo a la actividad
		txtCreditos = new EditText(vista.getContext());
		txtCreditos.setWidth(165);
		txtCreditos.setTextAppearance(this, R.style.texto);
		txtCreditos.setText(totalCreditos.toString());
		txtCreditos.setId(113);
		txtCreditos.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		//Para añadirlo a la vista original
		tablerow.addView(lbCreditos);
		tablerow.addView(txtCreditos);
		vista.addView(tablerow);
		
		Button btnMaxCreditos = new Button (vista.getContext());
		btnMaxCreditos.setText("Guardar");
		btnMaxCreditos.setTextColor(Color.parseColor("#ffffff"));
		btnMaxCreditos.setBackgroundColor(Color.parseColor("#2d6898"));
		LinearLayout.LayoutParams paramet=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		paramet.setMargins(0, 15, 0, 15);
		btnMaxCreditos.setLayoutParams(paramet);
		btnMaxCreditos.setClickable(true);
		btnMaxCreditos.setOnClickListener(new View.OnClickListener() {
					@Override
						public void onClick(View view) {	
							etxtTotalCreditos = (EditText) findViewById(txtCreditos.getId());
							newTotalCreditos=etxtTotalCreditos.getText().toString();
							newTotalCreditos = newTotalCreditos.replace('"', ' ');
							new asyncGuardarCreditos().execute();
					}

				});
		vista.addView(btnMaxCreditos); 			//
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
	            pDialog = new ProgressDialog(CreditosMaximo.this);
	            pDialog.setMessage("Créditos Maximos ....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			@Override
			protected String doInBackground(String... params) {
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (consultarCreditos()==true){    		    		
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
						PintarInformacion();
					} 
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }else
	            {
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
		    
		    class asyncGuardarCreditos extends AsyncTask< String, String, String > {
		    	 
		        @Override
				protected void onPreExecute() {
		        	//para el progress dialog
		            pDialog = new ProgressDialog(CreditosMaximo.this);
		            pDialog.setMessage("Creditos Maximos....");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(false);
		            pDialog.show();
		        }
		 
				@Override
				protected String doInBackground(String... params) {
					
					//enviamos y recibimos y analizamos los datos en segundo plano.
		    		try {
						if (guardarCreditos(newTotalCreditos,usuario)==true){    		    		
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
						Toast toast1 = Toast.makeText(getApplicationContext(),"Créditos Máximos Guardados", Toast.LENGTH_SHORT);
						toast1.show();
						
						
						
						Intent i = new Intent(CreditosMaximo.this, Creditos.class);
						i.putExtra("user", usuario);
				        startActivity(i);
				        
				        finish();
		            }else{
		            	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(200);
						Toast toast1 = Toast.makeText(getApplicationContext(),"Error de Guardado", Toast.LENGTH_SHORT);
						toast1.show();
		            }
		            
		                									}
				
		        }


	
}
