package com.example.appgestion.ui.usuario;

import android.content.Intent;
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

import com.example.appgestion.LoginActivity;
import com.example.appgestion.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CUsuarioActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                saveUserData(username, password);
            }
        });
    }

    private void saveUserData(String username, String password) {
        new SaveUserDataTask().execute(username, password);
    }

    private class SaveUserDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String result = "";

            try {
                URL url = new URL("http://192.168.1.5/app_gestion/agregarUS.php"); // Reemplaza con la URL de tu script PHP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "username=" + username + "&password=" + password;

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
            Toast.makeText(CUsuarioActivity.this, result, Toast.LENGTH_LONG).show();


            if (result.contains("Usuario registrado correctamente")) {
                Intent intent = new Intent(CUsuarioActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        }
    }
}