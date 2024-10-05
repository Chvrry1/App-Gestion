package com.example.appgestion.ui.inventario;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClsMateriaPrima {
    public String nombre;
    public double costo;

    public String unidad;
    public double pro_producto;



    public String id;

    public ClsMateriaPrima(String nombre, double costo, String unidad, double pro_producto, String id) {
        this.nombre = nombre;
        this.costo = costo;
        this.unidad = unidad;
        this.pro_producto = pro_producto;
        this.id = id;
    }



    public static void readMP(Context context,String id_producto, final VolleyCallback callback){
        String URL ="http://192.168.1.5/app_gestion/fetchMP.php?id="+id_producto;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsMateriaPrima> materiaPrimaList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String nombre = gastoObject.getString("nombre");
                                double costo = gastoObject.getDouble("costo");
                                String unidad = gastoObject.getString("unidad");
                                double pro_producto = gastoObject.getDouble("pro_producto");
                                String id = gastoObject.getString("id");
                                ClsMateriaPrima materiaPrima = new ClsMateriaPrima(nombre, costo,unidad,pro_producto,id);
                                materiaPrimaList.add(materiaPrima);
                            }
                            callback.onSuccess(materiaPrimaList);
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
        void onSuccess(List<ClsMateriaPrima> materiaPrimaList);
        void onFailure(String errorMessage);
    }
    public interface totalMPyIV {
        void onSuccess(double costoTotal, double ventasTotales);
        void onFailure(String errorMessage);
    }


    public static void readMPyIV(Context context, int id_empresa, final totalMPyIV callback) {
        String URL = "http://192.168.1.5/app_gestion/iv_mp_pe.php?id=" + id_empresa;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    double costoTotal = jsonObject.getDouble("costo_total");
                    double ventasTotales = jsonObject.getDouble("ventas_totales");

                    callback.onSuccess(costoTotal, ventasTotales);
                } catch (JSONException e) {
                    callback.onFailure(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.getMessage());
            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getPro_producto() {
        return pro_producto;
    }

    public void setPro_producto(double pro_producto) {
        this.pro_producto = pro_producto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
