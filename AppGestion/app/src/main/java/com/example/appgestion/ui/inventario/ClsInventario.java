package com.example.appgestion.ui.inventario;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClsInventario {
    public String descripcion;
    public int proyeccion_venta;

    public double precio_venta;

    public String id;

    public ClsInventario(String descripcion, int proyeccion_venta, double precio_venta, String id) {
        this.descripcion = descripcion;
        this.proyeccion_venta = proyeccion_venta;
        this.precio_venta = precio_venta;
        this.id = id;

    }

    public static void readIN(Context context,int id_empresa, final VolleyCallback callback){
        String URL ="http://192.168.1.5/app_gestion/fetchIN.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsInventario> inventarioList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String descripcion = gastoObject.getString("nombre");
                                int proyeccion_venta = gastoObject.getInt("proyeccion_venta");
                                double precio_venta = gastoObject.getDouble("precio_venta");
                                String id = gastoObject.getString("id");
                                ClsInventario inventario = new ClsInventario(descripcion, proyeccion_venta,precio_venta,id);
                                inventarioList.add(inventario);
                            }
                            callback.onSuccess(inventarioList);
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



    public interface VolleyCallback {
        void onSuccess(List<ClsInventario> inventarioList);
        void onFailure(String errorMessage);
    }
}
