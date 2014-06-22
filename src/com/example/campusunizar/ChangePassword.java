package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE CAMBIO DE CONTRASEÑA
 */
import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class ChangePassword extends Activity {

    private Button btnGuardarNewPassword;
    private EditText txtPassword1;
    private EditText txtPassword2;

    private String usuario;
    private String password1;
    private String password2;

    
    String URL_connect;
    String directorio="/campusUnizar/change_password.php";
    Httppostaux post;
    private ProgressDialog pDialog;

    JSONArray jdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		txtPassword1 = (EditText) findViewById(R.id.edpassword);
		txtPassword2 = (EditText) findViewById(R.id.edpassword1);
		btnGuardarNewPassword = (Button) findViewById(R.id.bGuardarNewPassword);
		
		Bundle bundle=getIntent().getExtras();
        usuario = bundle.getString("user");
		
        
		btnGuardarNewPassword.setOnClickListener(new View.OnClickListener(){
		       
	    	public void onClick(View view){
	    		 
	    		//Extreamos datos de los EditText
	    		password1=txtPassword1.getText().toString();
	    		password2=txtPassword2.getText().toString();

	    		//Comprobamos que se hayan introducido las dos contraseñas
	    		if(checklogindata(password1,password2)==true){
	    				new asyncNewPassword().execute();   
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
    public boolean checklogindata(String password1 ,String password2){
    	
	    if 	(password1.equals("") || password2.equals("")){
	    	Log.e("Login ui", "checklogindata user or pass error");
	    return false;
	    
	    }else{
	    	return true;
	    }
    }
    
    public boolean guardarContraseña() throws JSONException{

    	
    	//String UPpassword1 = ;
    	if (password1.toUpperCase().equals(password2.toUpperCase()))
		{
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
 		
			postparameters2send.add(new BasicNameValuePair("newPassword",password1));
			postparameters2send.add(new BasicNameValuePair("usuario",usuario));
	
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
	    	}
	    	return false;
		}
    	else
    	{
    		return false;
    	}
    }
    
    public void FinalizarActividad()
    {
    	this.finish();
    }
    

    class asyncNewPassword extends AsyncTask< String, String, String > {
   	 
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setMessage("Registrando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			
			try
			{
				if (guardarContraseña()){    		    		
	    			return "OK"; //OK
	    		}else{    		
	    			return "fallo"; //Fallo   	          	  
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
           
           if (result.equals("OK"))
           {
        	   err_login("Contraseña cambiada correctamente");
        	   Intent i = new Intent(ChangePassword.this, LoginUsuario.class);
		       startActivity(i);
		       finish();
				
				
           }else
           {
        	   err_login("Las dos contraseñas introducidas deben ser iguales");
           }
            
		}
		
        }
    
    
    
}