package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ PRINCIPAL
 */
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
//Acceso a MySQl
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import test.CampusUnizar.library.Httppostaux;

public class MainActivity extends Activity {
	
	Connection conexionMySQL;//Variable de conexión
	Httppostaux post;//Manejador de peticiones
	
	//String URL_connect
	String directorio="/campusUnizar/alertasPendientesAviso.php";
	String URL_connect;
	
	//Array donde vamos a guardar las alertas pendientes
    JSONArray jdata;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void lanzarLogin(View view) {
        Intent in = new Intent(this,LoginUsuario.class);
        startActivity(in);
    }  
    
    public void mostrarAlertas(View view){
    	Intent in = new Intent(this,Alertas.class);
        startActivity(in);
    }
    
    public void lanzarPublicas(View view) {
        Intent in = new Intent(this,ActividadesPublicas.class);
        startActivity(in);
    }  
    
    public void salir(View view) {
        finish();
    } 
    
    public boolean consultaAlertasPendientes(){
    	
    	//Calculamos la fecha actual de sistema
    	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd",new Locale("es_ES"));
        Calendar cal = new GregorianCalendar();
        java.util.Date date = cal.getTime();
        String formatteDate = formato.format(date);
        
    	//En este caso el array de parámetros va vacio
  		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("FechaActual",formatteDate));
  		
  		//Se realiza la petición de alertas pendientes desde la ultima conexion
  		jdata= post.getserverdata(postparameters2send, URL_connect);
  		
  	//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
    	
    }
}
