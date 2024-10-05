package com.example.appgestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.ui.encuesta.ClsEncuesta;
import com.example.appgestion.ui.gastos.AgregarGastos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuestaActivity extends AppCompatActivity {
    private LinearLayout encuesta_enviada;
    private ScrollView mostrar_encuesta;
    private TextView preguntaUno,preguntaDos,preguntaTres,preguntaCuatro, preguntaCinco;
    private TextView corregir_respuestas_preguntaTres,corregir_respuesta_preguntaCuatro;
    private RadioGroup respuesta_preguntaUno,respuesta_preguntaDos, respuesta_preguntaCinco;
    private RadioButton respuestaUno_preguntaTres,respuestaDos_preguntaTres,respuestaTres_preguntaTres,respuestaCuatro_preguntaTres,respuestaCinco_preguntaTres,respuestaSeis_preguntaTres,respuestaSiete_preguntaTres;
    private RadioButton respuestaUno_preguntaCuatro, respuestaDos_preguntaCuatro,respuestaTres_preguntaCuatro,respuestaCuatro_preguntaCuatro,respuestaCinco_preguntaCuatro;
    private ImageView atras;
    private List<ClsEncuesta>encuestaList = new ArrayList<>();
    private int idUsuario;
    private Button btn_enviar,btn_generar_encuesta_nueva;
    private static final String url = "http://192.168.1.5/app_gestion/agregarEC.php";
    private static final String URL2 = "http://192.168.1.5/app_gestion/deleteEC.php";

    @Override
    protected void onResume() {
        super.onResume();
        ClsEncuesta.readEC(this,idUsuario, new ClsEncuesta.VolleyCallback() {

            @Override
            public void onSuccess(List<ClsEncuesta> encuesta) {
                if (encuesta.isEmpty()){
                    encuesta_enviada.setVisibility(View.GONE);
                    mostrar_encuesta.setVisibility(View.VISIBLE);

                }else {
                    encuesta_enviada.setVisibility(View.VISIBLE);
                    mostrar_encuesta.setVisibility(View.GONE);
                    encuestaList = encuesta;
                    System.out.println("la lista encuesta esta llena");
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplication(), "lista vacia" + errorMessage, Toast.LENGTH_SHORT).show();
                //Toast.makeText(GastosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encuesta);
        corregir_respuestas_preguntaTres = findViewById(R.id.restaurar_respuesta_pregunta3);
        corregir_respuesta_preguntaCuatro = findViewById(R.id.restaurar_respuesta_pregunta4);
        atras = findViewById(R.id.devolver);
        preguntaUno = findViewById(R.id.pregunta1);
        preguntaDos = findViewById(R.id.pregunta2);
        preguntaTres = findViewById(R.id.pregunta3);
        preguntaCuatro = findViewById(R.id.pregunta4);
        preguntaCinco = findViewById(R.id.pregunta5);
        respuesta_preguntaUno = findViewById(R.id.opciones_pregunta_uno);
        respuesta_preguntaDos = findViewById(R.id.opciones_pregunta_dos);
        respuesta_preguntaCinco = findViewById(R.id.opciones_pregunta_cinco);


        //radioButtons para obtener la respuesta a la pregunta 3
        respuestaUno_preguntaTres = findViewById(R.id.registro_usuario_pregunta3);
        respuestaDos_preguntaTres = findViewById(R.id.registro_empresa_pregunta3);
        respuestaTres_preguntaTres = findViewById(R.id.gastos_pregunta3);
        respuestaCuatro_preguntaTres = findViewById(R.id.activos_pregunta3);
        respuestaCinco_preguntaTres =findViewById(R.id.inventario_pregunta3);
        respuestaSeis_preguntaTres = findViewById(R.id.inversion_pregunta3);
        respuestaSiete_preguntaTres =findViewById(R.id.ninguno_pregunta3);

        //radioButtons para obtener la respuesta a la pregunta 4
        respuestaUno_preguntaCuatro = findViewById(R.id.gastos_pregunta4);
        respuestaDos_preguntaCuatro = findViewById(R.id.activos_pregunta4);
        respuestaTres_preguntaCuatro = findViewById(R.id.inventario_pregunta4);
        respuestaCuatro_preguntaCuatro = findViewById(R.id.inversion_pregunta4);
        respuestaCinco_preguntaCuatro = findViewById(R.id.ninguno_pregunta4);






        btn_enviar = findViewById(R.id.btn_enviar);
        btn_generar_encuesta_nueva = findViewById(R.id.btn_crear_encuesta);
        encuesta_enviada = findViewById(R.id.respuesta_enviada);
        mostrar_encuesta = findViewById(R.id.mostrar_encuesta);

        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("userId", -1);





        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                respuestasPreguntas(encuestaList);
                System.out.println(encuestaList.size());
                for (ClsEncuesta clsEncuesta:encuestaList){
                    crear_preguntas(clsEncuesta.getPregunta(),clsEncuesta.getValoracion() , idUsuario, new VolleyCallback() {
                        @Override
                        public void onSuccess(boolean success) {
                            // Aqui manejas el caso en el que el guardado fue exitoso

                        }

                        @Override

                        public void onFailure(String errorMessage) {
                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }
                finish();




            }
        });
        btn_generar_encuesta_nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(encuestaList.size());
                System.out.println(idUsuario);
                for (ClsEncuesta clsEncuesta:encuestaList){
                    System.out.println(clsEncuesta.getId());
                    eliminar_encuesta(String.valueOf(clsEncuesta.getId()), idUsuario, new VolleyCallback() {
                        @Override
                        public void onSuccess(boolean success) {

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            System.out.println("no se pudo eliminar "+errorMessage);

                        }
                    });
                }
                Intent nuevaEncuesta = new Intent(getApplicationContext(), EncuestaActivity.class);
                startActivity(nuevaEncuesta);
                finish();
            }
        });

        corregir_respuestas_preguntaTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurarRespuestasPreguntaTres();
            }
        });
        corregir_respuesta_preguntaCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {restaurarRespuestasPreguntaCuatro();}
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });









        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private String radioGroupSeleccionado(RadioGroup radioGroup){
        int idRadioButton = radioGroup.getCheckedRadioButtonId();
        if (idRadioButton != -1){
            RadioButton radioButton = findViewById(idRadioButton);
            String valor = radioButton.getText().toString();
            return valor;
        }else {
            return "vacio";
        }

    }


    private void respuestasPreguntas(List<ClsEncuesta>encuestaList){
        for (int i =0; i<=4;i++){
            if (i == 0){
                String pregunta = preguntaUno.getText().toString();
                String valoracion = radioGroupSeleccionado(respuesta_preguntaUno);
                ClsEncuesta clsEncuesta = new ClsEncuesta(0,pregunta,valoracion);
                encuestaList.add(clsEncuesta);

            }
            if (i == 1){
                String pregunta = preguntaDos.getText().toString();
                String valoracion = radioGroupSeleccionado(respuesta_preguntaDos);
                ClsEncuesta clsEncuesta = new ClsEncuesta(0,pregunta,valoracion);
                encuestaList.add(clsEncuesta);

            }
            if (i == 2){
                String pregunta = preguntaTres.getText().toString();
                String valoracion = radioButtonSeleccionadosPreguntaTres();
                ClsEncuesta clsEncuesta = new ClsEncuesta(0,pregunta,valoracion);
                encuestaList.add(clsEncuesta);
                System.out.println(valoracion);

            }
            if (i == 3){
                String pregunta = preguntaCuatro.getText().toString();
                String valoracion = radioButtonSeleccionadosPreguntaCuatro();
                ClsEncuesta clsEncuesta = new ClsEncuesta(0,pregunta,valoracion);
                encuestaList.add(clsEncuesta);
                System.out.println(valoracion);

            }
            if (i == 4){
                String pregunta = preguntaCinco.getText().toString();
                String valoracion = radioGroupSeleccionado(respuesta_preguntaCinco);
                ClsEncuesta clsEncuesta = new ClsEncuesta(0,pregunta,valoracion);
                encuestaList.add(clsEncuesta);

            }

        }

    }

    private void crear_preguntas(final String pregunta, final String valoracion,final int usuario_id, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("pregunta",pregunta);
                datos.put("valoracion",valoracion);
                datos.put("usuario_id", String.valueOf(usuario_id));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void eliminar_encuesta(final String id,final int usuario_id, final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                response -> {
                    // La solicitud se ha realizado con éxito, llama al método onSuccess del callback con true
                    callback.onSuccess(true);
                },
                volleyError -> {
                    // Ha ocurrido un error en la solicitud, llama al método onFailure del callback con false y el mensaje de error
                    callback.onFailure("Error al enviar la solicitud: " + volleyError.getMessage());
                }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id); // Agregar el ID del gasto personal a editar
                params.put("usuario_id", String.valueOf(usuario_id));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private String radioButtonSeleccionadosPreguntaTres(){
        String valor = "";
        if (respuestaUno_preguntaTres.isChecked()){
            valor += respuestaUno_preguntaTres.getText().toString()+",";
        }
        if (respuestaDos_preguntaTres.isChecked()){
            valor += respuestaDos_preguntaTres.getText().toString()+",";
        }
        if (respuestaTres_preguntaTres.isChecked()){
            valor += respuestaTres_preguntaTres.getText().toString()+",";
        }
        if (respuestaCuatro_preguntaTres.isChecked()){
            valor += respuestaCuatro_preguntaTres.getText().toString()+",";
        }
        if (respuestaCinco_preguntaTres.isChecked()){
            valor += respuestaCinco_preguntaTres.getText().toString()+",";
        }
        if (respuestaSeis_preguntaTres.isChecked()){
            valor += respuestaSeis_preguntaTres.getText().toString()+",";
        }
        if (respuestaSiete_preguntaTres.isChecked()){
            valor += respuestaSiete_preguntaTres.getText().toString()+",";
        }
        if (valor == ""){
            valor += "No respondio";
        }
        return valor;

    }
    private void restaurarRespuestasPreguntaTres(){
        respuestaUno_preguntaTres.setChecked(false);
        respuestaDos_preguntaTres.setChecked(false);
        respuestaTres_preguntaTres.setChecked(false);
        respuestaCuatro_preguntaTres.setChecked(false);
        respuestaCinco_preguntaTres.setChecked(false);
        respuestaSeis_preguntaTres.setChecked(false);
        respuestaSiete_preguntaTres.setChecked(false);
    }
    private String radioButtonSeleccionadosPreguntaCuatro(){
        String valor = "";
        if (respuestaUno_preguntaCuatro.isChecked()){
            valor += respuestaUno_preguntaCuatro.getText().toString()+",";
        }
        if (respuestaDos_preguntaCuatro.isChecked()){
            valor += respuestaDos_preguntaCuatro.getText().toString()+",";
        }
        if (respuestaTres_preguntaCuatro.isChecked()){
            valor += respuestaTres_preguntaCuatro.getText().toString()+",";
        }
        if (respuestaCuatro_preguntaCuatro.isChecked()){
            valor += respuestaCuatro_preguntaCuatro.getText().toString()+",";
        }
        if (valor == ""){
            valor += "No respondio";
        }
        return valor;

    }
    private void restaurarRespuestasPreguntaCuatro(){
        respuestaUno_preguntaCuatro.setChecked(false);
        respuestaDos_preguntaCuatro.setChecked(false);
        respuestaTres_preguntaCuatro.setChecked(false);
        respuestaCuatro_preguntaCuatro.setChecked(false);
        respuestaCinco_preguntaCuatro.setChecked(false);
    }

    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}