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

public class AgregarActivosFijos extends AppCompatActivity {
    private ImageView atras;
    private EditText descripcion,unidad,valorUnitario, vidaUtil;
    private Button btn_agregarAF;
    private String idAF="";
    private int id_empresa;
    //"http://192.168.0.112/app_gestion/agregarAF.php"

    private static final String url = "http://192.168.0.112/app_gestion/agregarAF.php";
    private static final String url2 = "http://192.168.0.112/app_gestion/deleteAF.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_activos_fijos);


        atras=findViewById(R.id.devolver);
        descripcion=findViewById(R.id.descripcion);
        unidad = findViewById(R.id.num_unidades);
        valorUnitario=findViewById(R.id.valor_unitario);
        vidaUtil=findViewById(R.id.vida_util);
        btn_agregarAF = findViewById(R.id.btn_agregar_ActivoF);
        Button btn_editar= findViewById(R.id.btn_editar_activoF);
        ImageView btn_eliminar = findViewById(R.id.btn_eliminar_activoF);




        Intent intent = getIntent();
        id_empresa= intent.getIntExtra("id_empresa",0);
        Boolean agregar_activoF = intent.getBooleanExtra("agregar_activosF",false);
        if (agregar_activoF != true) {
            String id = intent.getStringExtra("id");
            String nombres = intent.getStringExtra("nombre");
            int unidades = intent.getIntExtra("unidades", 0);
            double valor_uni = intent.getDoubleExtra("valor_unitario",0.0);
            int vida_util =intent.getIntExtra("vida_util", 0);


            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews
            idAF = id;



            btn_editar.setVisibility(View.VISIBLE);
            btn_eliminar.setVisibility(View.VISIBLE);
            btn_agregarAF.setVisibility(View.GONE);



            descripcion.setText(nombres);
            unidad.setText(String.valueOf(unidades));
            valorUnitario.setText(String.valueOf(valor_uni));
            vidaUtil.setText(String.valueOf(vida_util));
        }


        btn_agregarAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion.getText().toString(),unidad.getText().toString(),valorUnitario.getText().toString(),vidaUtil.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    Integer unidades = Integer.valueOf(unidad.getText().toString().trim());
                    Double valor_unitario = Double.valueOf(valorUnitario.getText().toString().trim());
                    Integer vida_util = Integer.valueOf(vidaUtil.getText().toString().trim());

                    create_activo_fijo(name, unidades, valor_unitario,vida_util, id_empresa,new VolleyCallback(){

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
                Boolean validar = validar_datos(descripcion.getText().toString(),unidad.getText().toString(),valorUnitario.getText().toString(),vidaUtil.getText().toString());
                if (validar==true){
                    String name = descripcion.getText().toString().trim();
                    Integer unidades = Integer.valueOf(unidad.getText().toString().trim());
                    Double valor_unitario = Double.valueOf(valorUnitario.getText().toString().trim());
                    Integer vida_util = Integer.valueOf(vidaUtil.getText().toString().trim());

                    edit_activo_fijo(idAF,name, unidades, valor_unitario,vida_util,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Activo Editado", Toast.LENGTH_SHORT).show();
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
                Boolean validar = validar_datos(descripcion.getText().toString(),unidad.getText().toString(),valorUnitario.getText().toString(),vidaUtil.getText().toString());
                if (validar==true){
                    delete_activo_fijo(idAF,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso
                            if (success) {

                                // El quardado fue exitoso, puedes hacer algo aqui

                                Toast.makeText(getApplicationContext(), "Activo eliminado", Toast.LENGTH_SHORT).show();
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




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agregar_activos_fijos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void create_activo_fijo(final String name, final Integer unidades, final Double valor_unitario, final Integer vida_util,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("unidades", String.valueOf(unidades));
                datos.put("valor_unitario",String.valueOf(valor_unitario));
                datos.put("vida_util",String.valueOf(vida_util));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void edit_activo_fijo(final String id, final String name, final Integer unidades, final Double valor_unitario, final Integer vida_util,final int id_empresa,final VolleyCallback callback) {
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
                datos.put("unidades", String.valueOf(unidades));
                datos.put("valor_unitario",String.valueOf(valor_unitario));
                datos.put("vida_util",String.valueOf(vida_util));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void delete_activo_fijo(final String id, final int id_empresa,final VolleyCallback callback) {
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
    private Boolean validar_datos(String name, String unid, String valorUni, String vidaU){
        Boolean ingresado = true;



        if (name.isEmpty()){
            descripcion.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (unid.isEmpty()) {
            unidad.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (valorUni.isEmpty()) {
            valorUnitario.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (vidaU.isEmpty()) {
            vidaUtil.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}