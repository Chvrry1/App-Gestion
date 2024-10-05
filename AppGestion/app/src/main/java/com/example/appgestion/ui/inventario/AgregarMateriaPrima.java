package com.example.appgestion.ui.inventario;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class AgregarMateriaPrima extends AppCompatActivity {
    private ImageView atras, eliminar_mp;
    private Button agregar_materia_prima, editar_mp;
    private EditText descripcion_MP, costo_MP, unidades_MP, unida_medida_MP;
    private int id_producto;
    private int index_enviado;
    private String id_materia_prima;
    private static final String url = "http://192.168.0.112/app_gestion/agregarMP.php";
    private static final String url2 = "http://192.168.0.112/app_gestion/deleteMP.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_materia_prima);
        atras = findViewById(R.id.devolver);
        agregar_materia_prima=findViewById(R.id.btn_agregar_MP);
        descripcion_MP=findViewById(R.id.descripcion_MP);
        costo_MP=findViewById(R.id.costo_MP);
        unidades_MP=findViewById(R.id.unidades_MP);
        unida_medida_MP=findViewById(R.id.unidad_medida_MP);
        editar_mp= findViewById(R.id.btn_editar_datos_mp);
        eliminar_mp = findViewById(R.id.btn_eliminar_datos_mp);

        Intent intent = getIntent();
        id_producto= intent.getIntExtra("id_producto", 0);
        Boolean agregar_materia_p = intent.getBooleanExtra("agregar_materia_p",false);
        if (agregar_materia_p != true) {
            int index = intent.getIntExtra("index",0);
            String nombre = intent.getStringExtra("nombre");
            double costo = intent.getDoubleExtra("costo", 0.0);
            String unidad = intent.getStringExtra("unidad");
            double pro_producto= intent.getDoubleExtra("pro_producto", 0.0);
            id_materia_prima = intent.getStringExtra("id");

            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews

            editar_mp.setVisibility(View.VISIBLE);
            eliminar_mp.setVisibility(View.VISIBLE);
            agregar_materia_prima.setVisibility(View.GONE);




            descripcion_MP.setText(nombre);
            costo_MP.setText(String.valueOf(costo));
            unidades_MP.setText(String.valueOf(pro_producto));
            unida_medida_MP.setText(unidad);
            index_enviado = index;

        }





        agregar_materia_prima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MP.getText().toString(),costo_MP.getText().toString(),unida_medida_MP.getText().toString(),unidades_MP.getText().toString());
                if (validar==true){
                    if (id_producto==0){
                        SharedPreferences sharedPreferences = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
                        SharedPreferences.Editor materia_prima_datos = sharedPreferences.edit();

                        materia_prima_datos.putString("nombre", descripcion_MP.getText().toString());
                        materia_prima_datos.putString("costo",costo_MP.getText().toString());
                        materia_prima_datos.putString("unidad_medida",unida_medida_MP.getText().toString());
                        materia_prima_datos.putString("cantidad", unidades_MP.getText().toString());
                        materia_prima_datos.commit();
                        finish();
                    }else {
                        String name = descripcion_MP.getText().toString().trim();
                        Double costo = Double.valueOf(costo_MP.getText().toString().trim());
                        String unidad_med = unida_medida_MP.getText().toString().trim();
                        Double pro_producto = Double.valueOf(unidades_MP.getText().toString().trim());
                        create_materia_prima(name, costo, unidad_med, pro_producto, id_producto, new MateriaPrimaCallback() {
                            @Override
                            public void onSuccess(boolean success) {
                                Toast.makeText(getApplicationContext(), "Guardado exitoso"  , Toast.LENGTH_SHORT).show();
                                System.out.println("guardado corrcto materia prima");
                                finish();

                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                System.out.println("error al enviar materia prima");

                            }
                        });
                    }

                }

            }
        });
        editar_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MP.getText().toString(),costo_MP.getText().toString(),unida_medida_MP.getText().toString(),unidades_MP.getText().toString());
                if (validar==true){
                    if(id_producto==0){
                        SharedPreferences sharedPreferences = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
                        SharedPreferences.Editor materia_prima_datos = sharedPreferences.edit();

                        materia_prima_datos.putString("index", String.valueOf(index_enviado));
                        materia_prima_datos.putString("nombre", descripcion_MP.getText().toString());
                        materia_prima_datos.putString("costo",costo_MP.getText().toString());
                        materia_prima_datos.putString("unidad_medida",unida_medida_MP.getText().toString());
                        materia_prima_datos.putString("cantidad", unidades_MP.getText().toString());
                        materia_prima_datos.putString("accion", "edit");

                        materia_prima_datos.commit();
                        System.out.println(index_enviado);
                        finish();

                    }else {
                        String name = descripcion_MP.getText().toString().trim();
                        Double costo = Double.valueOf(costo_MP.getText().toString().trim());
                        String unidad_med = unida_medida_MP.getText().toString().trim();
                        Integer pro_producto = Integer.valueOf(unidades_MP.getText().toString().trim());


                        editar_materia_prima(id_materia_prima,name, costo, unidad_med, pro_producto,id_producto, new MateriaPrimaCallback() {
                            @Override
                            public void onSuccess(boolean success) {
                                Toast.makeText(getApplicationContext(), "Modificado con exito"  , Toast.LENGTH_SHORT).show();
                                System.out.println("Actualizo materia prima");
                                finish();

                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                System.out.println("error al enviar materia prima");

                            }
                        });



                    }

                }



            }
        });
        eliminar_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_MP.getText().toString(),costo_MP.getText().toString(),unida_medida_MP.getText().toString(),unidades_MP.getText().toString());
                if (validar==true){
                    if(id_producto==0){
                        SharedPreferences sharedPreferences = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
                        SharedPreferences.Editor materia_prima_datos = sharedPreferences.edit();

                        materia_prima_datos.putString("index", String.valueOf(index_enviado));
                        materia_prima_datos.putString("nombre", descripcion_MP.getText().toString());
                        materia_prima_datos.putString("costo",costo_MP.getText().toString());
                        materia_prima_datos.putString("unidad_medida",unida_medida_MP.getText().toString());
                        materia_prima_datos.putString("cantidad", unidades_MP.getText().toString());
                        materia_prima_datos.putString("accion", "delete");
                        materia_prima_datos.commit();
                        System.out.println(index_enviado);
                        finish();

                    }else {

                        eliminar_materia_prima(id_materia_prima,id_producto, new MateriaPrimaCallback() {
                            @Override
                            public void onSuccess(boolean success) {
                                Toast.makeText(getApplicationContext(), "Eliminado con exito"  , Toast.LENGTH_SHORT).show();
                                System.out.println("Actualizo materia prima");
                                finish();

                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                System.out.println("error al enviar materia prima");

                            }
                        });



                    }

                }



            }
        });
/*
        agregar_materia_prima.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String name = descripcion_MP.getText().toString().trim();
                Double costo = Double.valueOf(costo_MP.getText().toString().trim());
                String unidad = unida_medida_MP.getText().toString().trim();
                Integer pro_producto = Integer.valueOf(unidades_MP.getText().toString().trim());

                create_MP(name, costo, unidad,pro_producto, new VolleyCallback(){

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




        });

*/

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });











        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agregar_materia_prima), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void create_materia_prima(final String name, final Double costo , final String unidad_medida, final Double pro_producto, final Integer id_producto ,final MateriaPrimaCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {

            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("costo", String.valueOf(costo));
                datos.put("unidad",unidad_medida);
                datos.put("pro_producto",String.valueOf(pro_producto));
                datos.put("id_producto",String.valueOf(id_producto));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void editar_materia_prima(final String id,final String name, final Double costo , final String unidad_medida, final Integer pro_producto, final Integer id_producto ,final MateriaPrimaCallback callback) {
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
                datos.put("costo", String.valueOf(costo));
                datos.put("unidad",unidad_medida);
                datos.put("pro_producto",String.valueOf(pro_producto));
                datos.put("id_producto",String.valueOf(id_producto));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void eliminar_materia_prima(final String id, final Integer id_producto ,final MateriaPrimaCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url2, response -> {

            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("id_producto",String.valueOf(id_producto));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private Boolean validar_datos(String descripcion, String costo,String unidad_med, String unidades){
        Boolean ingresado = true;

        if (descripcion.isEmpty()){
            descripcion_MP.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (costo.isEmpty()) {
            costo_MP.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (unidad_med.isEmpty()) {
            unida_medida_MP.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (unidades.isEmpty()) {
            unidades_MP.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface MateriaPrimaCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
    /*
    private void create_MP(final String name, final Double costo, final String unidad,final Integer pro_producto, final AgregarMateriaPrima.VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("costo", String.valueOf(costo));
                datos.put("unidad",unidad);
                datos.put("pro_producto",String.valueOf(pro_producto));
                datos.put("id_empresa","1");
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
    */

}