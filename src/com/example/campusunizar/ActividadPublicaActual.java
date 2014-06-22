package com.example.campusunizar;
/**
 * @author María Armero, Lorena Súarez, Adrián Sánchez
 * 
 * CLASE PARA GESTIONAR LA INTERFAZ DE UNA ACTIVIDAD PÚBLICA
 */
import java.sql.Connection;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActividadPublicaActual extends Activity implements View.OnClickListener{
Connection conexionMySQL;//Variable de conexión
    
  //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
   // private ProgressDialog pDialog;
    Httppostaux post;
    
    // 	String URL_connect
    String directorio="/campusUnizar/publicActiv.php";
    String URL_connect;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_actual);
		//Manejador del envío de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		
		//Si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
		//new asynclogin().execute();  
		RelativeLayout vista = (RelativeLayout)findViewById(R.id.activ_pub_act);
		Bundle bundle=getIntent().getExtras();
		String extras= bundle.getString("actividad");
		String[] datos=extras.split("&");
		String actividad=datos[0];
	//Titulo de actividad
		TextView titulo = new TextView(vista.getContext());
		titulo.setTextAppearance(this, R.style.textoH1);
		titulo.setText(actividad); 
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		titulo.setLayoutParams(params);
		titulo.setId(111);
		vista.addView(titulo);
	//Fecha y Hora de actrividad
		TextView fechaTitulo = new TextView(vista.getContext());
		fechaTitulo.setTextAppearance(this, R.style.textoH2);
		fechaTitulo.setText("-Fecha / Hora:");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,titulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    fechaTitulo.setLayoutParams(params);
	    fechaTitulo.setId(112);
		vista.addView(fechaTitulo);
		
		String fecha=datos[1];
		String hora=datos[2];
		TextView fechaHora = new TextView(vista.getContext());
		fechaHora.setTextAppearance(this, R.style.texto);
		fechaHora.setText(fecha+" / "+hora);
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,fechaTitulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    fechaHora.setLayoutParams(params);
	    fechaHora.setId(122);
		vista.addView(fechaHora);
	//fin Fecha y Hora
	//Lugar de actrividad
		TextView LugarTitulo = new TextView(vista.getContext());
		LugarTitulo.setTextAppearance(this, R.style.textoH2);
		LugarTitulo.setText("-Lugar:");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,fechaHora.getId());
	    params.setMargins(0, 3, 0, 0);
	    LugarTitulo.setLayoutParams(params);
	    LugarTitulo.setId(113);
		vista.addView(LugarTitulo);
		
		String Sitio=datos[3];
		TextView Lugar = new TextView(vista.getContext());
		Lugar.setTextAppearance(this, R.style.texto);
		Lugar.setText(Sitio);
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,LugarTitulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    Lugar.setLayoutParams(params);
	    Lugar.setId(123);
		vista.addView(Lugar);
	//fin Lugar
	//Duracion de actrividad
		TextView TiempoTitulo = new TextView(vista.getContext());
		TiempoTitulo.setTextAppearance(this, R.style.textoH2);
		TiempoTitulo.setText("-Duración:");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,Lugar.getId());
	    params.setMargins(0, 3, 0, 0);
	    TiempoTitulo.setLayoutParams(params);
	    TiempoTitulo.setId(114);
		vista.addView(TiempoTitulo);
		
		String duracion=datos[4];
		TextView Tiempo = new TextView(vista.getContext());
		Tiempo.setTextAppearance(this, R.style.texto);
		Tiempo.setText(duracion+" Horas");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,TiempoTitulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    Tiempo.setLayoutParams(params);
	    Tiempo.setId(124);
		vista.addView(Tiempo);
	//fin Duración
	//Información de la actividad
		TextView infoTitulo = new TextView(vista.getContext());
		infoTitulo.setTextAppearance(this, R.style.textoH2);
		infoTitulo.setText("-Información:");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,Tiempo.getId());
	    params.setMargins(0, 3, 0, 0);
	    infoTitulo.setLayoutParams(params);
	    infoTitulo.setId(115);
		vista.addView(infoTitulo);
		
		String infor=datos[5];
		TextView informacion = new TextView(vista.getContext());
		informacion.setTextAppearance(this, R.style.texto);
		informacion.setText(infor);
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,infoTitulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    informacion.setLayoutParams(params);
	    informacion.setId(125);
		vista.addView(informacion);
	//fin de información
	//Créditos de la actividad
		TextView CreditosTitulo = new TextView(vista.getContext());
		CreditosTitulo.setTextAppearance(this, R.style.textoH2);
		CreditosTitulo.setText("-Créditos:");
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,informacion.getId());
	    params.setMargins(0, 3, 0, 0);
	    CreditosTitulo.setLayoutParams(params);
	    CreditosTitulo.setId(116);
		vista.addView(CreditosTitulo);
		
		String cred=datos[7].toString();
		TextView creditos = new TextView(vista.getContext());
		creditos.setTextAppearance(this, R.style.texto);
		creditos.setText(cred);
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,CreditosTitulo.getId());
	    params.setMargins(0, 3, 0, 0);
	    creditos.setLayoutParams(params);
	    creditos.setId(126);
		vista.addView(creditos);
	//fin de Créditos
		Button inscribir;
		inscribir = new Button(vista.getContext());
		inscribir.setText("Inscribirme en esta actividad");
		inscribir.setContentDescription(extras);
		params=new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW,creditos.getId());
	    params.setMargins(0, 3, 0, 0);
	    inscribir.setLayoutParams(params);
	    inscribir.setClickable(true);
	    inscribir.setOnClickListener(this);
	    inscribir.setBackgroundColor(Color.parseColor("#2d6898"));
	    inscribir.setTextAppearance(this, R.style.boton);
	    vista.addView(inscribir);
	//Volver
	    
	}
	
	public void onClick(View v) {
    	String actividad=v.getContentDescription().toString();
    	Intent in = new Intent(this,LoginUsuario.class);
		in.putExtra("actividad", actividad);
		this.finish();
	    startActivity(in);
    }
}
