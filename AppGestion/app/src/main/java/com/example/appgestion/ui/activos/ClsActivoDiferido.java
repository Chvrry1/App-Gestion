package com.example.appgestion.ui.activos;

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

public class ClsActivoDiferido {
    public String descripcion;
    public double pago_anticipado;
    public String vigencia;

    public String id;

    public ClsActivoDiferido(String descripcion, double pago_anticipado, String vigencia, String id) {
        this.descripcion = descripcion;
        this.pago_anticipado = pago_anticipado;
        this.vigencia = vigencia;
        this.id = id;

    }

    public static void readAFall(Context context,int id_empresa, final VolleyCallback callback){
        //"http://192.168.0.112/app_gestion/fetchAD.php?id=1"
        String URL ="http://192.168.0.112/app_gestion/fetchAD.php?id="+id_empresa;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsActivoDiferido> activoDiferidoList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String descripcion = gastoObject.getString("nombre");
                                double pago_anticipado = gastoObject.getDouble("pago_anticipado");
                                String vigencia = gastoObject.getString("vigencia");
                                String id = gastoObject.getString("id");
                                ClsActivoDiferido activoD = new ClsActivoDiferido(descripcion,pago_anticipado, vigencia, id);
                                activoDiferidoList.add(activoD);
                            }
                            callback.onSuccess(activoDiferidoList);
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
        void onSuccess(List<ClsActivoDiferido> activoDiferidoList);
        void onFailure(String errorMessage);
    }
}
