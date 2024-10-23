package com.example.appgestion.ui;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiService {
    private static final String BASE_URL = "http://192.168.x.x/app_gestion/";

    public void obtenerventas(Context context, int id_empresa, final VolleyCallback callback) {
        String url = BASE_URL + "obtenerVentas.php?id=" + id_empresa;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            double ventas = jsonObject.getDouble("Ventas");
                            callback.onSuccess(ventas);
                        } catch (JSONException e) {
                            callback.onError(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void obtenermateriaprima(Context context, int id_empresa, final VolleyCallback callback) {
        String url = BASE_URL + "obtenerMateriaPrima.php?id=" + id_empresa;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            double materiaprima = jsonObject.getDouble("materiaprima");
                            callback.onSuccess(materiaprima);
                        } catch (JSONException e) {
                            callback.onError(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void obtenerManoObra(Context context, int id_empresa, final VolleyCallback callback) {
        String url = BASE_URL + "obtenerManoObra.php?id=" + id_empresa;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            double manoObra = jsonObject.getDouble("manoObra");
                            callback.onSuccess(manoObra);
                        } catch (JSONException e) {
                            callback.onError(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccess(double result);
        void onError(String result);
    }
}
