package com.example.appgestion.ui.empresa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;
import com.example.appgestion.VolleySingleton;
import com.example.appgestion.ui.encuesta.ClsEncuesta;
import com.example.appgestion.ui.gastos.AgregarGastos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adaptadores.AdatadorEmpresas;

public class EmpresaActivity extends AppCompatActivity {

    private RecyclerView listaEmpresa;
    private AdatadorEmpresas empresaAdapter ;
    private int idUsuario;
    private static final String url = "http://192.168.0.112/app_gestion/agregarEC.php";

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("se ejecuto onstar" + idUsuario);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("userId", -1);


        // Realizar la solicitud HTTP para obtener las empresas asociadas al usuario
        obtenerEmpresas(this,idUsuario);
        listaEmpresa = findViewById(R.id.listaEmpresa);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        listaEmpresa.setLayoutManager(gridLayoutManager);


    }

    private void obtenerEmpresas(Context context,int idUsuario) {
        // Construir la URL de la solicitud
        String URL = "http://192.168.0.112/app_gestion/fetchEM.php";

        // Crear un objeto JSONObject para enviar los datos de la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", idUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar la solicitud HTTP
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta JSON
                        try {
                            List<clsEmpresa> empresasList = new ArrayList<>();
                            if (response.has("empresas")) {
                                JSONArray empresasJSON = response.getJSONArray("empresas");
                                for (int i = 0; i < empresasJSON.length(); i++) {
                                    JSONObject empresaJSON = empresasJSON.getJSONObject(i);
                                    int id = empresaJSON.getInt("id");
                                    String nombre = empresaJSON.getString("nombre");
                                    double capital = empresaJSON.getDouble("capital");
                                    clsEmpresa empresa = new clsEmpresa(id, nombre, capital);
                                    empresasList.add(empresa);
                                }
                            }
                            // Actualizar el adaptador con la lista de empresas
                            empresaAdapter = new AdatadorEmpresas(context, empresasList);
                            listaEmpresa.setAdapter(empresaAdapter);
                            empresaAdapter.notifyDataSetChanged();

                            if (empresasList.isEmpty()) {
                                Toast.makeText(EmpresaActivity.this, "No se encontraron empresas asociadas", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud HTTP
                        Toast.makeText(EmpresaActivity.this, "Error al obtener empresas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
    private void crear_preguntas(final String pregunta, final int valoracion,final int usuario_id, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("pregunta",pregunta);
                datos.put("valoracion", String.valueOf(valoracion));
                datos.put("usuario_id", String.valueOf(usuario_id));
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


}
/*
        System.out.println("se ejecuto onresume" + idUsuario);
        ClsEncuesta.readEC(this,idUsuario, new ClsEncuesta.VolleyCallback() {

            @Override
            public void onSuccess(List<ClsEncuesta> encuestaList) {
                if (encuestaList.isEmpty()){
                    String [] preguntas = {"¿La aplicación es fácil de usar?","¿La información mostrada en la aplicación es fácil de entender?"};
                    for (int i=0 ; i<preguntas.length;i++){
                        crear_preguntas(preguntas[i], 0, idUsuario, new VolleyCallback() {
                            @Override
                            public void onSuccess(boolean success) {
                                // Aqui manejas el caso en el que el guardado fue exitoso

                            }

                            @Override

                            public void onFailure(String errorMessage) {
                                // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                            }

                        });
                    }


                }else {
                    System.out.println("la lista encuesta esta llena");
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplication(), "lista vacia" + errorMessage, Toast.LENGTH_SHORT).show();
                //Toast.makeText(GastosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

         */