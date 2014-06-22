package com.example.campusunizar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class AccesoPrivado extends Activity{
	String usuario;
	TextView textUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acceso_privado);
		
		//Recogemos la información de la actividad login
		Bundle extras = getIntent().getExtras();
	    usuario = extras.getString("user");
	    
	    textUser = (TextView) findViewById(R.id.text_user);
	    textUser.setText("Usuario: " + usuario);
	    
	    Button ac_privadas = (Button) findViewById(R.id.bAccesoPrivadas);
	    
	    ac_privadas.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View view) {
	          Intent i = new Intent(AccesoPrivado.this, ActividadesPrivadas.class);
	          i.putExtra("user", usuario);
	          startActivity(i);
	        }

	      });
	    
		Button insc_privadas = (Button) findViewById(R.id.bInscribirPrivadas);
			    
		insc_privadas.setOnClickListener(new View.OnClickListener() {
		
			        @Override
			        public void onClick(View view) {
			          Intent i = new Intent(AccesoPrivado.this, InscribirPrivadas.class);
			          i.putExtra("user", usuario);
			          startActivity(i);
			        }
		
			      });
		Button btEncuestas = (Button) findViewById(R.id.btEncuestas);
	    
		btEncuestas.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View view) {
	          Intent i = new Intent(AccesoPrivado.this, Encuestas.class);
	          i.putExtra("user", usuario);
	          startActivity(i);
	        }

	      });
	}
	
	public void lanzarCreditos(View view) {
	    	
	        Bundle bundle=getIntent().getExtras();
	        String usuario = bundle.getString("user");
	        Intent in = new Intent(this,Creditos.class);
	        in.putExtra("user", usuario);
	        startActivity(in);
	    }  
}