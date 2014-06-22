package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE REGISTRO DE USUARIOS
 */
import java.util.ArrayList;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.CampusUnizar.library.Httppostaux;

public class RegistroUsuario extends Activity {

    private Button btnRegistro;
    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtUsuario;
    private EditText txtPassword;
    private EditText txtMail;
    private EditText txtDni;
    private EditText txtPreguntaSeg;
    private EditText txtRespuestaSeg;
    
    private String nombre;
    private String apellidos;
    private String usuario;
    private String password;
    private String dni;
    private String email;
    private String preguntaSeg;
    private String respuestaSeg;
    
    String URL_connect;
    String directorio="/campusUnizar/adduser.php";
    Httppostaux post;
    private ProgressDialog pDialog;

    JSONArray jdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_new_usuario);
		
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		txtNombre = (EditText) findViewById(R.id.txtNewNombre);
		txtApellidos = (EditText) findViewById(R.id.txtNewApellidos);
		txtUsuario = (EditText) findViewById(R.id.txtNewUsuario);
		txtPassword = (EditText) findViewById(R.id.txtNewPassword);
		txtMail = (EditText) findViewById(R.id.txtNewEmail);
		txtDni = (EditText) findViewById(R.id.txtNewDni);
		txtPreguntaSeg = (EditText) findViewById(R.id.txtPreguntaSeg);
		txtRespuestaSeg = (EditText) findViewById(R.id.txtRespuestaSeg);
		btnRegistro = (Button) findViewById(R.id.btnRegister);
		
		btnRegistro.setOnClickListener(new View.OnClickListener(){
		       
	    	public void onClick(View view){
	    		 
	    		//Extreamos datos de los EditText
	    		nombre=txtNombre.getText().toString();
	    		apellidos=txtApellidos.getText().toString();
	    		usuario=txtUsuario.getText().toString();
	    		password=txtPassword.getText().toString();
	    		dni=txtDni.getText().toString();
	    		email=txtMail.getText().toString();
	    		preguntaSeg=txtPreguntaSeg.getText().toString();
	    		respuestaSeg=txtRespuestaSeg.getText().toString();
	    		
	    		//verificamos si estan en blanco los campos de usuario y password
	    		if(checklogindata(nombre,apellidos,usuario, password,dni,email,preguntaSeg,respuestaSeg)==true){
	    			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
	    			new asyncRegistro().execute();        		               
	    		}else{
	    			//No ha insertado alguno de los campos obligatorios -->ERROR
	    			err_login("Todos los campos son obligatorios.");
	    		}
	    		
	    	}
	   });
		
	}
	
	//vibra y muestra un Toast, en el caso de error o falta de datos
    public void err_login(String mensajeError){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),mensajeError, Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
	
	//validamos si ha insertado todos los campos
    public boolean checklogindata(String nombre ,String apellidos ,String usuario, String password, String dni, String email,String pregunta, String respuesta){
    	
	    if 	(nombre.equals("") || apellidos.equals("") || usuario.equals("") || password.equals("") || dni.equals("") || email.equals("")
	    		|| pregunta.equals("") || respuesta.equals("")){
	    	Log.e("Registro ui", "No todos los campos completos");
	    return false;
	    
	    }else{
	    	return true;
	    }
    }
    
    public boolean registro() throws JSONException{

		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
 		
		postparameters2send.add(new BasicNameValuePair("nombre",nombre));
		postparameters2send.add(new BasicNameValuePair("apellidos",apellidos));
		postparameters2send.add(new BasicNameValuePair("usuario",usuario));
		postparameters2send.add(new BasicNameValuePair("password",password));
		postparameters2send.add(new BasicNameValuePair("dni",dni));
		postparameters2send.add(new BasicNameValuePair("email",email));
		postparameters2send.add(new BasicNameValuePair("pregunta",preguntaSeg));
		postparameters2send.add(new BasicNameValuePair("respuesta",respuestaSeg));


	   //realizamos una peticion y como respuesta obtenes un array JSON
  		jdata=post.getserverdata(postparameters2send, URL_connect);

	    SystemClock.sleep(950);
	    		
	    //si lo que obtuvimos no es null
    	if (jdata!=null && jdata.length() > 0){

    		JSONObject row = jdata.getJSONObject(0);
  			String resultado = row.getString("Resultado");
  			if (resultado.equals("true"))
  				return true;
  			else if (resultado.equals("false"))
				return false;
  			else if (resultado.equals("Existe"))
  				return false;
    	} return false;
    }
    
    public void FinalizarActividad()
    {
    	this.finish();
    }
    

    class asyncRegistro extends AsyncTask< String, String, String > {
   	 
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(RegistroUsuario.this);
            pDialog.setMessage("Registrando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			
			try
			{
				if (registro()==true){    		    		
	    			return "registroOK"; //registro OK
	    		}else{    		
	    			return "falloRegistro"; //Fallo en el registro  	          	  
	    		}
				
			}
			catch(JSONException e)
			{
				e.printStackTrace();
				return "err";
			}
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("registroOK"))
           {
				FinalizarActividad();
				err_login("Usuario dado de alta");
				
           }else
           {
        	   err_login("Fallo en el Registro");
           }
            
		}
		
        }
    
    
    
}
