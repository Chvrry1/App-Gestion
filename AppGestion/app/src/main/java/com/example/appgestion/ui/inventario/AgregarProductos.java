package com.example.appgestion.ui.inventario;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adaptadores.MateriaPrimaAdapter;

public class AgregarProductos extends AppCompatActivity {

    private ImageView atras, agregar_materiaP,eliminar_producto;
    private Button agregar_producto,editar_producto;
    private EditText num_unidades, descripcion_producto, precio_venta;

    private RecyclerView lista_materia_prima;
    private ClsMateriaPrima clsMateriaPrima;
    List<ClsMateriaPrima>materia_temporal= new ArrayList<>();

    String nombre;
    int proyeccion_venta,id_producto,id_empresa;
    double precio_vent;

    private static final String url = "http://192.168.0.112/app_gestion/agregarIN.php";
    private static final String url2 = "http://192.168.0.112/app_gestion/agregarMP.php";

    private static final String url3 = "http://192.168.0.112/app_gestion/deleteIN.php";

    @Override
    protected void onStop() {
        super.onStop();
        clearPreferences();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (id_producto != 0){
            agregar_producto.setVisibility(View.GONE);
            editar_producto.setVisibility(View.VISIBLE);
            eliminar_producto.setVisibility(View.VISIBLE);

            descripcion_producto.setText(nombre);
            num_unidades.setText(String.valueOf(proyeccion_venta));
            precio_venta.setText(String.valueOf(precio_vent));



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //verificar si existe un producto o no
        if (id_producto==0){
            //se traen los datos de la materia prima
            SharedPreferences sharedPref = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
            String index = sharedPref.getString("index", "");
            String nombre = sharedPref.getString("nombre", "");
            String costo = sharedPref.getString("costo", "");
            String unidad_medida = sharedPref.getString("unidad_medida", "");
            String cantidad = sharedPref.getString("cantidad", "");
            String accion = sharedPref.getString("accion", "");

            if(sharedPref.getAll().isEmpty()){
                System.out.println("Esta vacio");
            } else {
                //se agrega la materia prima a la lista temporal de materias primas
                if (index == ""){
                    clsMateriaPrima = new ClsMateriaPrima(nombre,Double.valueOf(costo),unidad_medida,Double.valueOf(cantidad),"");
                    materia_temporal.add(clsMateriaPrima);
                    System.out.println(materia_temporal);
                    MateriaPrimaAdapter inventarioAdapter =new MateriaPrimaAdapter(this,materia_temporal,id_producto);
                    lista_materia_prima.setAdapter(inventarioAdapter);
                    clearPreferences();

                }else {
                    switch (accion){
                        case "edit":
                            try {
                                //se modifica la materia prima en la lista temporal
                                for (int i=0; i<materia_temporal.size();i++){
                                    if (i == Integer.valueOf(index)){
                                        ClsMateriaPrima objetoModificar = materia_temporal.get(i);
                                        System.out.println(objetoModificar.nombre+ "--"+nombre);
                                        System.out.println(objetoModificar.costo+ "--"+costo);
                                        System.out.println(objetoModificar.unidad+ "--"+unidad_medida);
                                        System.out.println(objetoModificar.pro_producto+ "--"+cantidad);

                                        objetoModificar.setNombre(nombre);
                                        objetoModificar.setCosto(Double.parseDouble(costo));
                                        objetoModificar.setUnidad(unidad_medida);
                                        objetoModificar.setPro_producto(Integer.parseInt(cantidad));

                                    }

                                }
                                MateriaPrimaAdapter inventarioAdapter =new MateriaPrimaAdapter(this,materia_temporal,id_producto);
                                lista_materia_prima.setAdapter(inventarioAdapter);
                                clearPreferences();
                                break;

                            }catch (NumberFormatException e){
                                System.out.println("no se modifico "+e);
                                System.out.println(index);
                                break;


                            }
                        case "delete":
                            try {
                                //se elimina la materia prima en la lista temporal
                                for (int i=0; i<materia_temporal.size();i++){
                                    if (i == Integer.valueOf(index)){
                                        materia_temporal.remove(i);

                                    }

                                }
                                MateriaPrimaAdapter inventarioAdapter =new MateriaPrimaAdapter(this,materia_temporal,id_producto);
                                lista_materia_prima.setAdapter(inventarioAdapter);
                                clearPreferences();
                                break;

                            }catch (NumberFormatException e){
                                System.out.println("no se modifico "+e);
                                System.out.println(index);
                                break;


                            }
                    }



                }



            }

        }else {
            //se muestra la materia prima si ya existe un producto
            ClsMateriaPrima.readMP(this, String.valueOf(id_producto),new ClsMateriaPrima.VolleyCallback() {
                @Override
                public void onSuccess(List<ClsMateriaPrima> materiaPrimaList) {

                    MateriaPrimaAdapter materiaPrimaAdapter =new MateriaPrimaAdapter(AgregarProductos.this,materiaPrimaList,id_producto);
                    lista_materia_prima.setAdapter(materiaPrimaAdapter);


                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AgregarProductos.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                }
            });

        }

        // Setting the fetched data in the EditTexts


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_productos);
        Intent intent = getIntent();

        descripcion_producto = findViewById(R.id.descripcion);
        num_unidades = findViewById(R.id.num_unidades);
        precio_venta= findViewById(R.id.precio_venta);
        atras=findViewById(R.id.devolver);
        agregar_producto= findViewById(R.id.btn_agregar_producto);
        editar_producto =findViewById(R.id.btn_editar_producto);
        eliminar_producto=findViewById(R.id.btn_eliminar_producto);
        agregar_materiaP=findViewById(R.id.agregar_materiaP);

        //datos enviados al seleccionar producto
        id_producto = intent.getIntExtra("id_producto",0);
        id_empresa = intent.getIntExtra("id_empresa",0);
        nombre = intent.getStringExtra("nombre");
        proyeccion_venta = intent.getIntExtra("proyeccion_venta",0);
        precio_vent = intent.getDoubleExtra("precio_venta", 0.0);

        //mostrar materia prima
        lista_materia_prima = findViewById(R.id.lista_materia_prima);
        lista_materia_prima.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));







        agregar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_producto.getText().toString(),num_unidades.getText().toString(),precio_venta.getText().toString());
                if (id_producto==0 && validar==true){
                    String name = descripcion_producto.getText().toString().trim();
                    Integer proyeccion_venta = Integer.valueOf(num_unidades.getText().toString().trim());
                    Double precio_vent = Double.valueOf(precio_venta.getText().toString().trim());


                    //agregar el producto y traer la id del producto creado.

                    create_producto(name, proyeccion_venta, precio_vent,id_empresa, new VolleyCallback(){

                        @Override

                        public void onSuccess(boolean success,int producto_id) {
                            if (success) {
                                System.out.println(materia_temporal.size());

                                //agregar la lista de la materia prima a la base de datos

                                for (int i=0; i<materia_temporal.size();i++){
                                    ClsMateriaPrima materiaP = materia_temporal.get(i);
                                    create_materia_prima(materiaP.nombre, materiaP.costo, materiaP.unidad, materiaP.pro_producto, producto_id, new MateriaPrimaCallback() {
                                        @Override
                                        public void onSuccess(boolean success) {
                                            Toast.makeText(getApplicationContext(), "Materia prima guardada con exito"  , Toast.LENGTH_SHORT).show();
                                            System.out.println("guardado correcto materia prima");
                                            finish();

                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {
                                            System.out.println("error al enviar materia prima");

                                        }
                                    });


                                }
                                finish();

                                //System.out.println(producto_id);


                            } else {

                            }
                        }


                        @Override

                        public void onFailure(String errorMessage) {

                            // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();

                        }

                    });

                }

            }

        });

        editar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_producto.getText().toString(),num_unidades.getText().toString(),precio_venta.getText().toString());
                if (validar==true){
                    String name = descripcion_producto.getText().toString().trim();
                    Integer proyeccion_venta = Integer.valueOf(num_unidades.getText().toString().trim());
                    Double precio_vent = Double.valueOf(precio_venta.getText().toString().trim());

                    editar_producto(String.valueOf(id_producto),name,proyeccion_venta,precio_vent,id_empresa,new DefaultCallback(){
                        @Override
                        public void onSuccess(boolean success) {
                            // Aquí manejas el caso en el que el guardado fue exitoso
                            if (success) {
                                // El guardado fue exitoso, puedes hacer algo aquí
                                Toast.makeText(getApplicationContext(), "El producto se ha modificado correctamente", Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                // El guardado no fue exitoso, maneja este caso si es necesario
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Aquí manejas el caso en el que ocurrió un error al guardar
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }
        });

        eliminar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validar = validar_datos(descripcion_producto.getText().toString(),num_unidades.getText().toString(),precio_venta.getText().toString());
                if (validar==true){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AgregarProductos.this);

                    alertDialogBuilder.setTitle("Eliminar Producto "+descripcion_producto.getText());

                    alertDialogBuilder
                            .setMessage("¿Esta seguro de eliminar este producto?")
                            .setCancelable(false)
                            .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    eliminar_prod(String.valueOf(id_producto),id_empresa, new DefaultCallback() {
                                        @Override
                                        public void onSuccess(boolean success) {
                                            Toast.makeText(getApplicationContext(), "Producto Eliminado", Toast.LENGTH_SHORT).show();

                                            finish();

                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {

                                        }
                                    });

                                    //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                                }
                            })
                            .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            }).create().show();

                }

            }
        });



        agregar_materiaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formMP=new Intent(getApplicationContext(), AgregarMateriaPrima.class);
                formMP.putExtra("agregar_materia_p", true);
                formMP.putExtra("id_producto", id_producto);
                startActivity(formMP);


            }
        });



        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agregar_productos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void create_producto(final String name, final Integer proyeccion_venta, final Double precio_venta,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {

            try {
                JSONObject jsonObject= new JSONObject(response);
                int productId = jsonObject.getInt("id_producto");
                callback.onSuccess(true, productId);

            } catch (JSONException e) {
                callback.onFailure("Error en la respuesta del servidor: " + e.getMessage());
            }




        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("proyeccion_venta", String.valueOf(proyeccion_venta));
                datos.put("precio_venta",String.valueOf(precio_venta));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void create_materia_prima(final String name, final Double costo , final String unidad_medida, final Double pro_producto, final Integer id_producto ,final MateriaPrimaCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url2, response -> {

            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("nombre",name);
                datos.put("costo", String.valueOf(costo));
                datos.put("unidad",unidad_medida);
                datos.put("pro_producto",String.valueOf(pro_producto));
                datos.put("id_producto",String.valueOf(id_producto));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void editar_producto(final String id, final String name, final Integer proyeccion_venta, final Double precio_venta,final int id_empresa, final DefaultCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
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
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("nombre",name);
                datos.put("proyeccion_venta", String.valueOf(proyeccion_venta));
                datos.put("precio_venta",String.valueOf(precio_venta));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void eliminar_prod(final String id_producto,final int id_empresa, final DefaultCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url3,
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
                params.put("id", id_producto); // Agregar el ID del gasto personal a editar
                params.put("id_empresa", String.valueOf(id_empresa));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private Boolean validar_datos(String descripcion, String unidades,String precio){
        Boolean ingresado = true;


        if (descripcion.isEmpty()){
            descripcion_producto.setError("Ingrese los datos solicitados");
            ingresado = false;

        }
        if (unidades.isEmpty()) {
            num_unidades.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        if (precio.isEmpty()) {
            precio_venta.setError("Ingrese los datos solicitados");
            ingresado = false;
        }
        return ingresado;


    }
    public interface VolleyCallback {
        void onSuccess(boolean success ,int productId);
        void onFailure(String errorMessage);
    }
    public interface DefaultCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
    public interface MateriaPrimaCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
    private void clearPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
        SharedPreferences.Editor materia_prima_datos = sharedPreferences.edit();
        materia_prima_datos.clear();
        materia_prima_datos.apply();

    }
}