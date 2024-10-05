package com.example.appgestion.ui.inversion;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.ui.inventario.ClsManoObra;

import org.json.JSONException;
import org.json.JSONObject;

public class ClsPorcentajesInversion {
    public double inflacion;
    public double tasa_libre_riesgo;

    public String id;

    public ClsPorcentajesInversion(double inflacion, double tasa_libre_riesgo, String id) {
        this.inflacion = inflacion;
        this.tasa_libre_riesgo = tasa_libre_riesgo;
        this.id = id;
    }
    public static void fetchPI(Context context, int id_empresa, final porcentajes callback) {
        String URL = "http://192.168.1.5/app_gestion/fetchPI.php?id="+id_empresa;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    double inflacion = jsonObject.getDouble("inflacion");
                    double tasa_libre_riesgo = jsonObject.getDouble("tasa_libre_riesgo");
                    String id = jsonObject.getString("id");
                    ClsPorcentajesInversion porcentajes = new ClsPorcentajesInversion(inflacion, tasa_libre_riesgo,id);
                    System.out.println(inflacion);

                    callback.onSuccess(porcentajes);
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
    public interface porcentajes{
        void onSuccess(ClsPorcentajesInversion porcentajes);
        void onFailure(String errorMessage);
    }
}
