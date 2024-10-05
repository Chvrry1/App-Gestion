package com.example.appgestion.ui.gastos;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClsGastoPersonal {
    public String descripcion;
    public double importe;

    public String id;
    public String tipo;

    public ClsGastoPersonal(String descripcion, double importe,String id,String tipo) {
        this.descripcion = descripcion;
        this.importe = importe;
        this.id = id;
        this.tipo = tipo;

    }

    public static void readGPall(Context context,int id_empresa, final VolleyCallback callback){

        String URL ="http://192.168.1.5/app_gestion/fetchGPALL.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsGastoPersonal> gastosList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String descripcion = gastoObject.getString("nombre");
                                double importe = gastoObject.getDouble("importe");
                                String id = gastoObject.getString("id");
                                String tipo = gastoObject.getString("tipo_gasto");
                                ClsGastoPersonal gasto = new ClsGastoPersonal(descripcion, importe,id, tipo);
                                gastosList.add(gasto);
                            }
                            callback.onSuccess(gastosList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error de análisis JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.d("Error", volleyError.getMessage());
                        callback.onFailure("Error de red: " + volleyError.getMessage());
                    }
                }

        );
        requestQueue.add(jsonObjectRequest);
    }


    public static void readGPallIndirectos(Context context,int id_empresa, final VolleyCallback callback){
        String URL ="http://192.168.1.5/app_gestion/fetchGPALL.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsGastoPersonal> gastosList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String descripcion = gastoObject.getString("nombre");
                                double importe = gastoObject.getDouble("importe");
                                String id = gastoObject.getString("id");
                                String tipo = gastoObject.getString("tipo");
                                ClsGastoPersonal gasto = new ClsGastoPersonal(descripcion, importe,id,tipo);
                                gastosList.add(gasto);
                            }
                            callback.onSuccess(gastosList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error de análisis JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.d("Error", volleyError.getMessage());
                        callback.onFailure("Error de red: " + volleyError.getMessage());
                    }
                }

        );
        requestQueue.add(jsonObjectRequest);
    }

    public static void totalSueldoEmprendedor(Context context,int id_empresa, final SueldoCallback callback){
        String URL ="http://192.168.1.5/app_gestion/sueldoEmprendedor.php?id="+id_empresa;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                        double totalSueldo = jsonObject.get("total_sueldo").getAsDouble();

                        callback.onSuccess(totalSueldo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Llamar al método de callback onFailure en caso de error
                        callback.onFailure(error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de solicitudes HTTP
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public interface SueldoCallback {
        void onSuccess(double totalSueldo);
        void onFailure(String errorMessage);
    }


    public interface VolleyCallback {
        void onSuccess(List<ClsGastoPersonal> gastosList);
        void onFailure(String errorMessage);
    }


}
