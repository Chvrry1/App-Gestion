package com.example.appgestion.ui.activos;

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

public class AgregarActivosDiferidos extends AppCompatActivity {
    private ImageView atras;
    private EditText descripcion,valor_pago, vigencia;
    private Button btn_agregarAD;
    private String idAD="";
    private int id_empresa;
    private static final String url = "http://192.168.0.112/apis/agregarAD.php";
    private static final String url2 = "http://192.168.0.112/apis/deleteAD.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_activos_diferidos);
        atras=findViewById(R.id.devolver);
        descripcion=findViewById(R.id.descripcion_activo_diferido);
        valor_pago=findViewById(R.id.valor_pago);
        vigencia=findViewById(R.id.vigencia);
        btn_agregarAD=findViewById(R.id.btn_agregar_ActivoD);
        Button btn_editar= findViewById(R.id.btn_editar_activoD);
        ImageView btn_eliminar = findViewById(R.id.btn_eliminar_activoD);




        Intent intent = getIntent();
        id_empresa = intent.getIntExtra("id_empresa",0);
        Boolean agregar_activoD = intent.getBooleanExtra("agregar_activosD",false);
        if (agregar_activoD != true) {
            String id = intent.getStringExtra("id");
            String nombres = intent.getStringExtra("nombre");
            double pago = intent.getDoubleExtra("pago_anticipado", 0.0);
            String vigen = intent.getStringExtra("vigencia");


            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews
            idAD = id;



            btn_editar.setVisibility(View.VISIBLE);
            btn_eliminar.setVisibility(View.VISIBLE);
            btn_agregarAD.setVisibility(View.GONE);



            descripcion.setText(nombres);
            valor_pago.setText(String.valueOf(pago));
            vigencia.setText(vigen);
        }

        btn_agregarAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion.getText().toString(),valor_pago.getText().toString(),vigencia.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    Double pago = Double.valueOf(valor_pago.getText().toString().trim());
                    String vigen= vigencia.getText().toString().trim();

                    create_activo_diferido(name, pago, vigen,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {

                                // El guardado no fue exitoso, maneja este caso si es necesario

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
                Boolean validar = validar_datos(descripcion.getText().toString(),valor_pago.getText().toString(),vigencia.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    Double pago = Double.valueOf(valor_pago.getText().toString().trim());
                    String vigen= vigencia.getText().toString().trim();


                    edit_activo_diferido(idAD,name, pago, vigen,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {

                                // El guardado no fue exitoso, maneja este caso si es necesario

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
                Boolean validar = validar_datos(descripcion.getText().toString(),valor_pago.getText().toString(),vigencia.getText().toString());
                if (validar==true){
                    delete_activo_diferido(idAD, id_empresa,new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {

                                // El guardado no fue exitoso, maneja este caso si es necesario

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





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agregar_activos_diferidos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void create_activo_diferido(final String name, final Double pago, final String vigen,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("pago_anticipado", String.valueOf(pago));
                datos.put("vigencia",vigen);
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void edit_activo_diferido(final String id, final String name, final Double pago, final String vigen,final int id_empresa,final VolleyCallback callback) {
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
                datos.put("pago_anticipado", String.valueOf(pago));
                datos.put("vigencia",vigen);
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void delete_activo_diferido(final String id,final int id_empresa, final VolleyCallback callback) {
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
    private Boolean validar_datos(String name, String pago, String vigen){
        Boolean ingresado = true;

        if (name.isEmpty()){
            descripcion.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (pago.isEmpty()) {
            valor_pago.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (vigen.isEmpty()) {
            vigencia.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}