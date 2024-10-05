package com.example.appgestion.ui.gastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;

import java.util.HashMap;
import java.util.Map;

public class AgregarCostos extends AppCompatActivity {

    private ImageView atras;
    private EditText descripcion, costo;
    private String idCI="";

    private Button btn_costos;
    private int id_empresa;

    private static final String url = "http://192.168.0.112/app_gestion/agregarCP.php";
    private static final String url2 = "http://192.168.0.112/app_gestion/deleteCP.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_costos);
        atras = findViewById(R.id.devolver);
        descripcion = findViewById(R.id.descripcion_costo);
        costo = findViewById(R.id.costo);
        btn_costos = findViewById(R.id.btn_agregar_costo);
        Button btn_editar_costo= findViewById(R.id.btn_editar_costo);
        ImageView btn_eliminar_costo= findViewById(R.id.btn_eliminar_costo);


        Intent intent = getIntent();
        id_empresa=intent.getIntExtra("id_empresa",0);
        Boolean agregar_costoI = intent.getBooleanExtra("agregar_costo",false);
        if (agregar_costoI != true) {
            String id = intent.getStringExtra("id");
            String nombre = intent.getStringExtra("nombre");
            double importes = intent.getDoubleExtra("importe", 0.0);


            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews
            idCI = id;



            btn_editar_costo.setVisibility(View.VISIBLE);
            btn_eliminar_costo.setVisibility(View.VISIBLE);
            btn_costos.setVisibility(View.GONE);



            descripcion.setText(nombre);
            costo.setText(String.valueOf(importes));
            /*textViewTipo.setText(tipo);*/
        }




        btn_costos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    String importe = costo.getText().toString().trim();

                    create_costos_indirectos(name, Double.valueOf(importe), id_empresa,new VolleyCallback() {

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {

                                // El guardado no fue exitoso, maneja este caso si es necesario
                                Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();

                            }
                        }


                        @Override

                        public void onFailure(String errorMessage) {

                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();

                }


            }
        });
        btn_editar_costo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    Double importe = Double.valueOf(costo.getText().toString().trim());

                    editar_costo_indirecto(idCI,name,importe,id_empresa,new VolleyCallback(){
                        @Override
                        public void onSuccess(boolean success) {
                            // Aquí manejas el caso en el que el guardado fue exitoso
                            if (success) {
                                // El guardado fue exitoso, puedes hacer algo aquí
                                Toast.makeText(getApplicationContext(), "Editado exitoso", Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                // El guardado no fue exitoso, maneja este caso si es necesario
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Aquí manejas el caso en el que ocurrió un error al guardar
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
        btn_eliminar_costo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                if (validar==true){
                    eliminar_costo_indirecto(idCI,id_empresa,new VolleyCallback(){
                        @Override
                        public void onSuccess(boolean success) {
                            // Aquí manejas el caso en el que el guardado fue exitoso
                            if (success) {
                                // El guardado fue exitoso, puedes hacer algo aquí
                                Toast.makeText(getApplicationContext(), "eliminado exitoso", Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                // El guardado no fue exitoso, maneja este caso si es necesario
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Aquí manejas el caso en el que ocurrió un error al guardar
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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

    }
    private void create_costos_indirectos(final String name, final Double importe, final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre_concepto",name);
                datos.put("importe_mensual", String.valueOf(importe));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void editar_costo_indirecto(final String id,final String name, final Double importe, final int id_empresa, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("nombre_concepto",name);
                datos.put("importe_mensual", String.valueOf(importe));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void eliminar_costo_indirecto(final String id,final int id_empresa, final VolleyCallback callback) {
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
    private Boolean validar_datos(String name, String importe){
        Boolean ingresado = true;


        if (name.isEmpty()){
            descripcion.setError("Ingrese los datos solicitados");
            ingresado = false;


        }
        if (importe.isEmpty()) {
            costo.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}