package com.example.appgestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appgestion.ui.empresa.EmpresaActivity;
import com.example.appgestion.ui.encuesta.ClsEncuesta;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;
import com.example.appgestion.ui.usuario.CUsuarioActivity;
import com.example.appgestion.ui.usuario.clsUsuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import adaptadores.CostosAdapter;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button nextButton2 =  findViewById(R.id.btn_iniciar_sesion);
        final TextInputLayout cod_uni_input = findViewById(R.id.Input_Cod_Uni_Login);
        final TextInputEditText cod_uni = findViewById(R.id.Edit_Cod_Uni_Login);

        final TextInputLayout pass_Input = findViewById(R.id.input_password_login);
        final TextInputEditText pass= findViewById(R.id.Edit_password_login);
        TextView crearUser = findViewById(R.id.btn_Crear_Cuenta_activity);


        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion(cod_uni.getText().toString(),pass.getText().toString());
            }
        });

        crearUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }
    private void iniciarSesion(String usuario, String contraseña) {
        clsUsuario.IniciarSesion(this, usuario, contraseña, new clsUsuario.UsuarioCallback() {
            @Override
            public void onSuccess(int userId, String username) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
                    // Obtener un editor para modificar las preferencias compartidas
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // Guardar el ID del usuario en las preferencias compartidas
                    editor.putInt("userId", userId);

                editor.apply(); // Aplicar los cambios
                Intent intent = new Intent(LoginActivity.this, EmpresaActivity.class);

                startActivity(intent);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el fallo en la obtención de la lista de empresas
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                System.out.println(errorMessage);
            }
        });
    }
    private void verificarEncuesta(int userId){
        ClsEncuesta.readEC(getApplicationContext(),userId, new ClsEncuesta.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsEncuesta> encuestaList) {
               if (encuestaList.isEmpty()){
                   System.out.println("la lista encuesta esta vacia");
               }else {
                   System.out.println("la lista encuesta esta llena");
               }

            }

            @Override
            public void onFailure(String errorMessage) {
                //Toast.makeText(GastosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

    }

}