package com.example.appgestion.ui.usuario;

import android.content.Context;
import android.telecom.Call;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class clsUsuario {
    public static void IniciarSesion(final Context context, final String username, final String password, final UsuarioCallback callback) {
        String URL = "http://192.168.1.5/app_gestion/validarUser.php";

        // Crear un objeto JSON con los datos de usuario y contraseña
        JSONObject postData = new JSONObject();
        try {
            postData.put("user", username); // Usar "user" en lugar de "username"
            postData.put("contraseña", password); // Usar "contraseña" en lugar de "password"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("mensaje") && response.getString("mensaje").equals("Inicio de sesión exitoso")) {
                        // Obtener el objeto de usuario de la respuesta
                        JSONObject usuarioObject = response.getJSONObject("usuario");
                        // Extraer los datos de usuario
                        int id = usuarioObject.getInt("id");
                        String username = usuarioObject.getString("username");
                        // Llamar al método onSuccess con los datos del usuario
                        callback.onSuccess(id, username);
                    } else {
                        // Manejar el caso en que el inicio de sesión falla
                        callback.onFailure("Usuario o contraseña incorrectos");
                    }
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

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public interface UsuarioCallback {
        void onSuccess(int userId, String username);
        void onFailure(String errorMessage);
    }
}
