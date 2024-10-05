package com.example.appgestion.ui.empresa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appgestion.R;
import com.example.appgestion.ui.encuesta.ClsEncuesta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CrearEmpresa extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText docIdentificadorEditText;
    private EditText razonSocialEditText;
    private EditText numeroTelfEditText;
    private EditText direccionEditText;
    private EditText paisEditText;
    private EditText capitalEditText;
    private Button saveButton;

    private int userId; // Variable para almacenar el ID del usuario


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);

        // Leer el userId de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1); // -1 es el valor por defecto si no se encuentra el userId

        // Inicializar las vistas
        nombreEditText = findViewById(R.id.nombre);
        docIdentificadorEditText = findViewById(R.id.doc_identificador);
        razonSocialEditText = findViewById(R.id.razon_social);
        numeroTelfEditText = findViewById(R.id.numero_telf);
        direccionEditText = findViewById(R.id.direccion);
        paisEditText = findViewById(R.id.pais);
        capitalEditText = findViewById(R.id.capital);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreEditText.getText().toString();
                String docIdentificador = docIdentificadorEditText.getText().toString();
                String razonSocial = razonSocialEditText.getText().toString();
                String numeroTelf = numeroTelfEditText.getText().toString();
                String direccion = direccionEditText.getText().toString();
                String pais = paisEditText.getText().toString();
                String capital = capitalEditText.getText().toString();

                saveEmpresaData(nombre, docIdentificador, razonSocial, numeroTelf, direccion, pais, capital, userId);
            }
        });
    }

    private void saveEmpresaData(String nombre, String docIdentificador, String razonSocial, String numeroTelf, String direccion, String pais, String capital, int userId) {
        new SaveEmpresaDataTask().execute(nombre, docIdentificador, razonSocial, numeroTelf, direccion, pais, capital, String.valueOf(userId));
    }

    private class SaveEmpresaDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String nombre = params[0];
            String docIdentificador = params[1];
            String razonSocial = params[2];
            String numeroTelf = params[3];
            String direccion = params[4];
            String pais = params[5];
            String capital = params[6];
            String usuarioId = params[7];
            String result = "";

            try {
                URL url = new URL("http://192.168.0.112/app_gestion/agregarEM.php"); // Reemplaza con la URL de tu script PHP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "nombre=" + nombre + "&doc_identificador=" + docIdentificador + "&razon_social=" + razonSocial +
                        "&numero_telf=" + numeroTelf + "&direccion=" + direccion + "&pais=" + pais + "&capital=" + capital + "&usuario_id=" + usuarioId;

                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                result = sb.toString();
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(CrearEmpresa.this, result, Toast.LENGTH_LONG).show();

            if (result.contains("Empresa registrada correctamente")) {
                Intent intent = new Intent(CrearEmpresa.this, EmpresaActivity.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual para que no se pueda volver con el botón de retroceso
            }
        }
    }
}