package com.example.appgestion.ui.gastos;

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

public class ClsCostosIndirectos {
    public String nombre_concepto;
    public double importe;

    public String id;


    public ClsCostosIndirectos(String descripcion, double importe, String id) {
        this.nombre_concepto = descripcion;
        this.importe = importe;
        this.id = id;
    }


    public static void readCIall(Context context,int id_empresa, final VolleyCallback callback){
        String URL ="http://192.168.0.112/app_gestion/fetchCP.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsCostosIndirectos> costosList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String nombre_concepto = gastoObject.getString("nombre_concepto");
                                double importe = gastoObject.getDouble("importe_mensual");
                                String id = gastoObject.getString("id");

                                ClsCostosIndirectos gasto = new ClsCostosIndirectos(nombre_concepto, importe,id);
                                costosList.add(gasto);
                            }
                            callback.onSuccess(costosList);
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
    public static void totalCostosIndirectos(Context context,int id_empresa, final TotalCallback callback){
        String URL ="http://192.168.0.112/app_gestion/totalCostosIndirecto.php?id="+id_empresa;

        // Hacer la solicitud HTTP para obtener el resultado de la suma de los gastos personales
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Convertir la respuesta de String a double
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        // Obtiene el valor asociado con la clave "total_CI"
                        double total_CI = jsonObject.get("total_CI").getAsDouble();
                        // Llama al método de callback onSuccess con el resultado
                        callback.onSuccess(total_CI);
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

    public interface TotalCallback {
        void onSuccess(double totalSueldo);
        void onFailure(String errorMessage);
    }

    public interface VolleyCallback {
        void onSuccess(List<ClsCostosIndirectos> gastosList);
        void onFailure(String errorMessage);
    }
}