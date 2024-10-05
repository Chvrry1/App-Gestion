package com.example.appgestion.ui.gastos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class AgregarGastos extends AppCompatActivity {

    private ImageView atras;
    private Spinner tipo;
    private String idGP="";
    private EditText descripcion, costo;
    private Button btn_agregar;
    private int id_empresa;
    private static final String url = "http://192.168.1.5/app_gestion/agregarGP.php";

    private static final String URL2 = "http://192.168.1.5/app_gestion/deleteGP.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_gastos);
        /*
        sharedPreferences = getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        String nombre_empresa = sharedPreferences.getString("empresa_nombre", "");

         */



        //asignar parametros de la vista a las variables

        atras = (ImageView) findViewById(R.id.devolver);
        tipo = findViewById(R.id.seleccion_prioridad);
        descripcion = findViewById(R.id.descripcion);
        costo= findViewById(R.id.valor);
        btn_agregar=findViewById(R.id.btn_agregar_gasto);
        Button btn_editar= findViewById(R.id.btn_edigar_datos_gp);
        ImageView btn_eliminar = findViewById(R.id.btn_eliminar_datos_gp);


        //seleccionar item de el spinner

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.prioridad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapter);





        Intent intent = getIntent();
        id_empresa=intent.getIntExtra("id_empresa",0);
        Boolean agregar_gastosP = intent.getBooleanExtra("agregar_gastosP",false);

        if (agregar_gastosP != true) {
            String id = intent.getStringExtra("id");
            String nombres = intent.getStringExtra("nombre");
            double importes = intent.getDoubleExtra("importe",0.0);
            CharSequence tipos = intent.getCharSequenceExtra("tipo");
            System.out.println(tipos);

            // Usa los datos como desees, por ejemplo, estableciéndolos en TextViews
            idGP = id;

            for (int i = 0; i < adapter.getCount(); i++) {
                System.out.println(adapter.getItem(i));

                // Verifica si el elemento actual es igual a 'tipos'
                if ( adapter.getItem(i).equals(tipos)) {
                    System.out.println(tipos);

                    tipo.setSelection(i);
                    break; // Rompe el bucle una vez que se haya encontrado la coincidencia
                }


            }

            btn_editar.setVisibility(View.VISIBLE);
            btn_eliminar.setVisibility(View.VISIBLE);
            btn_agregar.setVisibility(View.GONE);


/*

            if (agregar_gastosP == true){
                btn_editar.setVisibility(View.GONE);
                btn_eliminar.setVisibility(View.GONE);


            }else {
                btn_editar.setVisibility(View.VISIBLE);
                btn_eliminar.setVisibility(View.VISIBLE);
                btn_agregar.setVisibility(View.GONE);
            }

 */




            descripcion.setText(nombres);
            costo.setText(String.valueOf(importes));
            /*textViewTipo.setText(tipo);*/
        }





        //agregar datos a la base de datos

        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(id_empresa);
                try {
                    Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                    if(validar==true){
                        String name = descripcion.getText().toString().trim();
                        double importe = Double.parseDouble(costo.getText().toString().trim());
                        String tipo_gasto = tipo.getItemAtPosition(tipo.getSelectedItemPosition()).toString();
                        create_gatos_persona(name, importe, tipo_gasto, id_empresa, new VolleyCallback() {

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

                    }else {
                        Toast.makeText(getApplicationContext(), "Ingrese los datos para continuar", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    System.out.println("error en el vacio"+e);
                }




            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                if(validar==true){
                    String name = descripcion.getText().toString().trim();
                    double importe = Double.parseDouble(costo.getText().toString().trim());
                    String tipos = tipo.getItemAtPosition(tipo.getSelectedItemPosition()).toString();

                    editar_gasto_persona(idGP,name,importe,tipos,id_empresa,new VolleyCallback(){
                        @Override
                        public void onSuccess(boolean success) {
                            // Aquí manejas el caso en el que el guardado fue exitoso
                            if (success) {
                                // El guardado fue exitoso, puedes hacer algo aquí
                                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();

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


        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validar = validar_datos(descripcion.getText().toString(), costo.getText().toString());
                if(validar==true){
                    eliminar_gasto_persona(idGP,id_empresa,new VolleyCallback(){
                        @Override
                        public void onSuccess(boolean success) {
                            // Aquí manejas el caso en el que el guardado fue exitoso
                            if (success) {
                                // El guardado fue exitoso, puedes hacer algo aquí
                                Toast.makeText(getApplicationContext(), "Gasto Eliminado", Toast.LENGTH_SHORT).show();

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


        //retroceder a la interfaz principal

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void create_gatos_persona(final String name, final Double importe, final String tipo_gasto,final int id_empresa, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("importe", String.valueOf(importe));
                datos.put("tipo_gasto",tipo_gasto);
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void editar_gasto_persona(final String id, final String name, final Double importe, final String tipo,final int id_empresa, final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    // La solicitud se ha realizado con éxito, llama al método onSuccess del callback con true
                    callback.onSuccess(true);
                },
                volleyError -> {
                    // Ha ocurrido un error en la solicitud, llama al método onFailure del callback con false y el mensaje de error
                    callback.onFailure("Error al enviar la solicitud: " + volleyError.getMessage());
                }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id); // Agregar el ID del gasto personal a editar
                params.put("nombre", name);
                params.put("importe", String.valueOf(importe));
                params.put("tipo_gasto", tipo);
                params.put("id_empresa", String.valueOf(id_empresa));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void eliminar_gasto_persona(final String id,final int id_empresa, final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                response -> {
                    // La solicitud se ha realizado con éxito, llama al método onSuccess del callback con true
                    callback.onSuccess(true);
                },
                volleyError -> {
                    // Ha ocurrido un error en la solicitud, llama al método onFailure del callback con false y el mensaje de error
                    callback.onFailure("Error al enviar la solicitud: " + volleyError.getMessage());
                }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id); // Agregar el ID del gasto personal a editar
                params.put("id_empresa", String.valueOf(id_empresa));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private Boolean validar_datos(String name, String importe){
        Boolean ingresado = true;
        String impor = String.valueOf(importe);


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

