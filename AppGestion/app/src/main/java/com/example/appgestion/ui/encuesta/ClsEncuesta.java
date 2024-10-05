package com.example.appgestion.ui.encuesta;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClsEncuesta {
    private int id;
    private String pregunta;
    private String valoracion;

    public ClsEncuesta(int id, String pregunta, String valoracion) {
        this.id = id;
        this.pregunta = pregunta;
        this.valoracion = valoracion;
    }
    public static void readEC(Context context, int usuario_id, final VolleyCallback callback){

        String URL ="http://192.168.0.112/app_gestion/fetchEC.php?id="+usuario_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<ClsEncuesta> encuestaList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject gastoObject = jsonArray.getJSONObject(i);
                                String pregunta = gastoObject.getString("pregunta");
                                String valoracion = gastoObject.getString("valoracion");
                                int id = gastoObject.getInt("id");
                                ClsEncuesta encuesta = new ClsEncuesta(id, pregunta,valoracion);
                                encuestaList.add(encuesta);
                            }
                            callback.onSuccess(encuestaList);
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
    public interface VolleyCallback {
        void onSuccess(List<ClsEncuesta> encuestaList);
        void onFailure(String errorMessage);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }
}
