package com.example.appgestion.ui.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;

import java.util.HashMap;
import java.util.Map;

public class AgregarManoObra extends AppCompatActivity {

    private ImageView atras;
    private Button agregar_MO;
    private EditText descripcion_MO, sueldo_mensual;
    private String idMO="";
    private int id_empresa;
    private static final String url = "http://192.168.1.5/app_gestion/agregarMO.php";
    private static final String url2 = "http://192.168.1.5/app_gestion/deleteMO.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_mano_obra);
        atras=findViewById(R.id.devolver);
        agregar_MO=findViewById(R.id.btn_agregar_MO);
        descripcion_MO=findViewById(R.id.descripcion_mano_obra);
        sueldo_mensual=findViewById(R.id.sueldo_mensual);
        Button btn_editar= findViewById(R.id.btn_editar_MO);
        ImageView btn_eliminar = findViewById(R.id.btn_eliminar_MO);




        Intent intent = getIntent();
        id_empresa = intent.getIntExtra("id_empresa",0);
        Boolean agregar_mano_obra = intent.getBooleanExtra("agregar_MO",false);
        if (agregar_mano_obra != true) {
            String id = intent.getStringExtra("id");
            String nombre = intent.getStringExtra("nombre");
            double sueldo = intent.getDoubleExtra("sueldo",0.0);


            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews
            idMO = id;



            btn_editar.setVisibility(View.VISIBLE);
            btn_eliminar.setVisibility(View.VISIBLE);
            agregar_MO.setVisibility(View.GONE);



            descripcion_MO.setText(nombre);
            sueldo_mensual.setText(String.valueOf(sueldo));

        }


        agregar_MO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MO.getText().toString(),sueldo_mensual.getText().toString());
                if (validar==true){
                    String name = descripcion_MO.getText().toString().trim();
                    Double sueldo = Double.valueOf(sueldo_mensual.getText().toString().trim());

                    create_mano_obra(name, sueldo, id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            if (success) {

                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {


                            }
                        }


                        @Override

                        public void onFailure(String errorMessage) {

                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }

            }




        });
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MO.getText().toString(),sueldo_mensual.getText().toString());
                if (validar==true){
                    String name = descripcion_MO.getText().toString().trim();
                    Double sueldo = Double.valueOf(sueldo_mensual.getText().toString().trim());

                    edit_mano_obra(idMO,name, sueldo,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            if (success) {

                                Toast.makeText(getApplicationContext(), "Mano de obra modificada", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {


                            }
                        }


                        @Override

                        public void onFailure(String errorMessage) {

                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }

            }




        });
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MO.getText().toString(),sueldo_mensual.getText().toString());
                if (validar==true){
                    delete_mano_obra(idMO,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            if (success) {

                                Toast.makeText(getApplicationContext(), "Mano de obra eliminada", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {


                            }
                        }


                        @Override

                        public void onFailure(String errorMessage) {

                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }


            }




        });



        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agregar_mano_obra), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void create_mano_obra(final String name, final Double sueldo,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("sueldo",String.valueOf(sueldo));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void edit_mano_obra(final String id, final String name, final Double sueldo,final int id_empresa, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("nombre",name);
                datos.put("sueldo",String.valueOf(sueldo));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void delete_mano_obra(final String id,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url2, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private Boolean validar_datos(String descripcion, String sueldo){
        Boolean ingresado = true;


        if (descripcion.isEmpty()){
            descripcion_MO.setError("Ingrese los datos solicitados");
            ingresado = false;


        }
        if (sueldo.isEmpty()) {
            sueldo_mensual.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}