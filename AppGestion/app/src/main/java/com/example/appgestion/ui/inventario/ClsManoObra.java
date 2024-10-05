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

public class ClsManoObra {
    public String nombre;
    public double sueldo;

    public String id;

    public ClsManoObra(String nombre, double sueldo, String id) {
        this.nombre = nombre;
        this.sueldo = sueldo;
        this.id = id;

    }

    public static void readMO(Context context,int id_empresa, final VolleyCallback callback){
        String URL ="http://192.168.1.5/app_gestion/fetchMO.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsManoObra> manoObraList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String nombre = gastoObject.getString("nombre");
                                double sueldo = gastoObject.getDouble("sueldo");
                                String id = gastoObject.getString("id");
                                ClsManoObra manoObra = new ClsManoObra(nombre, sueldo,id);
                                manoObraList.add(manoObra);
                            }
                            callback.onSuccess(manoObraList);
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
        void onSuccess(List<ClsManoObra> manoObraList);
        void onFailure(String errorMessage);
    }

    public static void readTotalMO(Context context, int id_empresa, final totalMO callback) {
        String URL = "http://192.168.1.5/app_gestion/totalMO.php?id="+id_empresa;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    double total_Mano_obra = jsonObject.getDouble("total_Mano_obra");

                    callback.onSuccess(total_Mano_obra);
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
    public interface totalMO{
        void onSuccess(double totalManoObra);
        void onFailure(String errorMessage);
    }






}
