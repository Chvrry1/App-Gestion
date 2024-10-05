package com.example.appgestion.ui.activos;

import android.content.Context;
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

public class ClsActivoFijo {
    public String descripcion;
    public int unidades;

    public double valor_unitario;
    public int vida_util;

    public String id;

    public ClsActivoFijo(String descripcion, int unidades, double valor_unitario, int vida_util, String id) {
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.valor_unitario = valor_unitario;
        this.vida_util = vida_util;
        this.id = id;

    }

    public static void readAFall(Context context,int id_empresa,final VolleyCallback callback){
        //http://192.168.1.5/app_gestion/fetchAF.php?id=1
        String URL ="http://192.168.1.5/app_gestion/fetchAF.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsActivoFijo> activoFijoList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String descripcion = gastoObject.getString("nombre");
                                int unidades = gastoObject.getInt("unidades");
                                double valor_unitario = gastoObject.getDouble("valor_unitario");
                                int vida_util = gastoObject.getInt("vida_util");
                                String id = gastoObject.getString("id");
                                ClsActivoFijo activoF = new ClsActivoFijo(descripcion, unidades,valor_unitario, vida_util, id);
                                activoFijoList.add(activoF);
                            }
                            callback.onSuccess(activoFijoList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error de análisis JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error", volleyError.getMessage());
                        callback.onFailure("Error de red: " + volleyError.getMessage());
                    }
                }

        );
        requestQueue.add(jsonObjectRequest);
    }
    public static void totalDepreciacion(Context context, int id_empresa,final SueldoCallback callback){
        String URL ="http://192.168.1.5/app_gestion/totalDepreciacion.php?id="+id_empresa;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                        double total_costos_indirectos = jsonObject.get("total_costos_indirectos").getAsDouble();

                        callback.onSuccess(total_costos_indirectos);
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

    public static void totalAmortizacion(Context context, int id_empresa, final SueldoCallback callback){
        String URL ="http://192.168.1.5/app_gestion/totalAmortizacion.php?id="+id_empresa;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                        double total_amortizacion = jsonObject.get("total_amortizacion").getAsDouble();

                        callback.onSuccess(total_amortizacion);
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
        void onSuccess(double total_costos_indirectos);
        void onFailure(String errorMessage);
    }



    public interface VolleyCallback {
        void onSuccess(List<ClsActivoFijo> activoFijoList);
        void onFailure(String errorMessage);
    }
}
