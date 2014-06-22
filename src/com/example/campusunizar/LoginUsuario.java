package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE LOGIN
 */
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//Acceso a MySQl
//Paquete donde se ha creado en manejador de peticiones


public class LoginUsuario extends Activity {
	
	EditText user;
    EditText pass;
    Button blogin;
    TextView registro;
    TextView rememberPass;
    
    //realizamos una peticion y como respuesta obtenes un array JSON
	JSONArray jdata2;
	
    //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
    Httppostaux post;
    String usuario;

    // String URL_connect    
    String directorio="/campusUnizar/acces.php";
    String URL_connect;
    
    String directorioHoraConexion="/campusUnizar/registrarHoraConexion.php";
    String URL_connectRegistraHora;
    
    String directorioAlertaPendientes="/campusUnizar/alertasPendientesAviso.php";
    String URL_connectAlertasPendientes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_usuario);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		post=new Httppostaux();
		URL_connectRegistraHora= post.getURL(directorioHoraConexion);
		
		post=new Httppostaux();
		URL_connectAlertasPendientes= post.getURL(directorioAlertaPendientes);
		
		//Cogemos los datos
		user= (EditText) findViewById(R.id.edusuario);
        pass= (EditText) findViewById(R.id.edpassword);
        blogin= (Button) findViewById(R.id.bLogin);
        registro= (TextView) findViewById(R.id.txtregistro);
        rememberPass= (TextView) findViewById(R.id.txtrememberpassword);
        
        
        
      //Login button action
	   blogin.setOnClickListener(new View.OnClickListener(){
	       
	    	@Override
			public void onClick(View view){
	    		 
	    		//Extreamos datos de los EditText
	    		String usuario=user.getText().toString();
	    		String passw=pass.getText().toString();
	    		
	    		//verificamos si estan en blanco los campos de usuario y password
	    		if( checklogindata( usuario , passw )==true){
	    			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
	    			new asynclogin().execute(usuario,passw);        		               
	    		}else{
	    			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
	    			err_login();
	    		}
	    		
	    	}
	   });
	   
	   registro.setOnClickListener(new View.OnClickListener(){
	       
	    	public void onClick(View view){
	    		Intent i=new Intent(LoginUsuario.this, RegistroUsuario.class);
				startActivity(i); 
				finalizar();
 		}
	   });
	   
	   rememberPass.setOnClickListener(new View.OnClickListener(){
	       
	    	public void onClick(View view){
	    		String usuario=user.getText().toString();
	    		if (usuario != null && usuario.length() > 0)
	    		{
		    		Intent i=new Intent(LoginUsuario.this, RememberPassword.class);
		    		i.putExtra("user", usuario);
					startActivity(i); 
					finalizar();
	    		}else
	    		{
	    			Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    		    vibrator.vibrate(200);
	    		    Toast toast1 = Toast.makeText(getApplicationContext(),"Debe introducir el usuario", Toast.LENGTH_SHORT);
	    	 	    toast1.show(); 
	    			
	    		}
		}
	   });
	}
	 
	//vibra y muestra un Toast
    public void err_login(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
    
    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String username ,String password ) {
    	int logstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
		postparameters2send.add(new BasicNameValuePair("usuario",username));
		postparameters2send.add(new BasicNameValuePair("password",password));

	   //realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
	
		/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
		 * observar el progressdialog
		 * la podemos eliminar si queremos
		 */
	    SystemClock.sleep(950);
	    		
	    //si lo que obtuvimos no es null
	    	if (jdata!=null && jdata.length() > 0){
	
	    		JSONObject json_data; //creamos un objeto JSON
				try {
					json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
					 logstatus=json_data.getInt("logstatus");//accedemos al valor 
					 Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		            
	             
				//validamos el valor obtenido
	    		 if (logstatus==0){// [{"logstatus":"0"}] 
	    			 Log.e("loginstatus ", "invalido");
	    			 return false;
	    		 }
	    		 else{// [{"logstatus":"1"}]
	    			 Log.e("loginstatus ", "valido");
	    			 return true;
	    		 }
	    		 
		  }else{	//json obtenido invalido verificar parte WEB.
	    			 Log.e("JSON  ", "ERROR");
		    		return false;
		  }
	    	
	    }
	    
		//validamos si no hay ningun campo en blanco
	    public boolean checklogindata(String username ,String password ){
	    	
		    if 	(username.equals("") || password.equals("")){
		    	Log.e("Login ui", "checklogindata user or pass error");
		    return false;
		    
		    }else{
		    	
		    	return true;
		    }
    }
    
    public void finalizar(){
    	this.finish();
    }
    public boolean registraUltimaConexionEnBBDD(String username) throws JSONException{
    	/*Usuario Conectado*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
		postparameters2send.add(new BasicNameValuePair("usuario",username));

	   //realizamos una peticion y como respuesta obtenes un array JSON
  		JSONArray jdata1=post.getserverdata(postparameters2send, URL_connectRegistraHora);
  		JSONObject json_data; //creamos un objeto JSON
  		int logstatus;//Valor devuelto por el webService
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata1!=null && jdata1.length() > 0){
  			
			json_data = jdata1.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
			logstatus=json_data.getInt("logstatus");
			if(logstatus == 1)
				return true;
			else 
				return false;
    	}else
  			return false;
    
    }
    
    public void registrarUltimaConexion(String user){
    	//Registramos la ultima conexion dl usuario
    	new asyncRegistrarUltimaConexion().execute(user);
    }
    
    public void mostrarPopUp(JSONArray jdata2) throws JSONException
    {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Nuevas Alertas en las Actividades");
    	int i=0;
    	JSONObject json_data = null; //creamos un objeto JSON
    	String mensaje="";
		while(i<jdata2.length()){
			json_data = jdata2.getJSONObject(i); //leemos el primer segmento en nuestro caso el unico
			mensaje=mensaje + "*" +json_data.getString("nombre") + "\n\n";
			i=i+1;
		}
		mensaje=mensaje + "¡¡Visita el apartado de alertas para mas informacion!!";
    	alert.setMessage(mensaje);
    	alert.setCancelable(true);
    	alert.setNeutralButton("Aceptar",new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Bundle bundle=getIntent().getExtras();
        		String actividad;
        		try{
        			actividad=bundle.getString("actividad");
        		}catch(Exception e){
        			actividad="";
        		}
        		if (actividad.equals("")){
					Intent i=new Intent(LoginUsuario.this, AccesoPrivado.class);
					i.putExtra("user",usuario);
					startActivity(i);
					finalizar();
        		}else{
        			Intent in = new Intent(LoginUsuario.this,ActividadPublicaInscrita.class);
        			in.putExtra("actividad", actividad);
        			in.putExtra("user",usuario);
        			//in.putExtra("array_pendientes", jdata2);
        			finalizar();
        		    startActivity(in);
        		}
        	}
        });
    	alert.setIcon(R.drawable.ic_launcher);
        AlertDialog alert11 = alert.create();
        alert11.show();
        Log.i("PopupAlertas","Pop up correcto");
    }
    
    public boolean popUpAlertasPendientes(String user) throws JSONException{
    	/*Usuario Conectado*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     	usuario=user;
		postparameters2send.add(new BasicNameValuePair("usuario",user));

	   //realizamos una peticion y como respuesta obtenes un array JSON
  		jdata2=post.getserverdata(postparameters2send, URL_connectAlertasPendientes);
  		
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata2!=null && jdata2.length() > 0){
  			return true;
  		}else
  			return false;
    }
    
    public void alertasPendientes(String usuario){
    	new asyncAlertasPendientes().execute(usuario);
    }
    /*		CLASE ASYNCTASK
     * 
     * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
     * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
     * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
     * ademas observariamos el mensaje de que la app no responde.     
     */
        
    class asynclogin extends AsyncTask< String, String, String > {
    	 
    	String user,pass;
    	@Override
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(LoginUsuario.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		@Override
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			user=params[0];
			pass=params[1];
            
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (loginstatus(user,pass)==true){  
    			alertasPendientes(user);
    			
    			return "ok"; //login valido
    		}else{    		
    			return "err"; //login invalido     	          	  
    		}
        	
		}
	
   
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        @Override
		protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           	if (result.equals("ok")){
           		registrarUltimaConexion(user);
            }else{
            	err_login();
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
        
    class asyncAlertasPendientes extends AsyncTask< String, String, String > {
    	 
    	String user,pass;
        @Override
		protected void onPreExecute() {
        }
 
		@Override
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			user=params[0];
            
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		try {
				if (popUpAlertasPendientes(user)==true){
					return "ok"; //login valido
				}else{    		
					return "err"; //login invalido     	          	  
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return "err";
			}
        	
		}
	
   
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        @Override
		protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           	if (result.equals("ok")){
           		try {
					mostrarPopUp(jdata2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           		//Log.i("PopupAlertas","Pop up correcto");
            }else{//No hay alertas pendientes y no las tiene que mostrar
            	Bundle bundle=getIntent().getExtras();
        		String actividad;
        		try{
        			actividad=bundle.getString("actividad");
        		}catch(Exception e){
        			actividad="";
        		}
        		if (actividad.equals("")){
					Intent i=new Intent(LoginUsuario.this, AccesoPrivado.class);
					i.putExtra("user",usuario);
					startActivity(i);
					finalizar();
        		}else{
        			Intent in = new Intent(LoginUsuario.this,ActividadPublicaInscrita.class);
        			in.putExtra("actividad", actividad);
        			in.putExtra("user",usuario);
        			//in.putExtra("array_pendientes", jdata2);
        			finalizar();
        		    startActivity(in);
        		}
            }
        }
    }
    /*		CLASE ASYNCTASK     */
        
    class asyncRegistrarUltimaConexion extends AsyncTask< String, String, String > {
    	 
    	String user,pass;
        @Override
		protected void onPreExecute() {
 
        }
 
		@Override
		protected String doInBackground(String... params) {
			String username=params[0];
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		try {
				if (registraUltimaConexionEnBBDD(username)==true){
					return "ok"; //login valido
				}else{    		
					return "err"; //login invalido     	          	  
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return "err";
			}
        	
		}
	
   
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        @Override
		protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.i("onPostExecute=",""+result);
           
           	if (result.equals("ok")){
           		Log.i("HoraConexion","Guardada Correctamente");
            }else{
            	//err_login();
            }
        }
    }
}
