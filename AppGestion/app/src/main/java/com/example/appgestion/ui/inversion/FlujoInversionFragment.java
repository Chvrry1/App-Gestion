package com.example.appgestion.ui.inversion;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;
import com.example.appgestion.ui.ApiService;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FlujoInversionFragment extends Fragment {

    private LineChart lineChart;
    private List<String> valores_x;
    private ImageView btn_agregar_inflacion, btn_agregar_tasa_libre, btn_editar_inflacion,btn_editar_tasa_libre;
    private TextView inflacion,tasa_libre_riesgo, id_porcentaje;
    private double valor_inflacion,valor_tasa_libre_riesgo;
    private int id_empresa;
    private SharedPreferences sharedPreferences;

    private TextView ingresos,egresos;


    private double costoIndirecto,totalEgreso,saldoFinal,materiaprima,manoobra,ventas,capitalRequerido;
    private static final String url = "http://192.168.1.5/app_gestion/agregarPI.php";

    private List<Double> inversiones = new ArrayList<>();
    private List<Double> recuperaciones = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("se ejecuto on Star inversion");

        sharedPreferences = getContext().getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        obtenerPorcentajes(getContext(), id_empresa, inflacion, tasa_libre_riesgo, id_porcentaje);
        valor_inflacion = Double.parseDouble(inflacion.getText().toString().replace("%", "").trim());
        valor_tasa_libre_riesgo = Double.parseDouble(tasa_libre_riesgo.getText().toString().replace("%", "").trim());


        costoIndirecto =0.0;
        totalEgreso =0.0;
        saldoFinal =0.0;
        materiaprima =0.0;
        manoobra =0.0;
        ventas =0.0;
        capitalRequerido =0.0;





    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("se ejecuto on resume inversion");
        obtenerPorcentajes(getContext(), id_empresa, inflacion, tasa_libre_riesgo, id_porcentaje);
        valor_inflacion = Double.parseDouble(inflacion.getText().toString().replace("%", "").trim());
        valor_tasa_libre_riesgo = Double.parseDouble(tasa_libre_riesgo.getText().toString().replace("%", "").trim());

        if (valor_inflacion != 0.0) {
            btn_agregar_inflacion.setVisibility(View.GONE);
            btn_editar_inflacion.setVisibility(View.VISIBLE);
        }else {
            btn_agregar_inflacion.setVisibility(View.VISIBLE);
            btn_editar_inflacion.setVisibility(View.GONE);
        }
        if (valor_tasa_libre_riesgo != 0.0) {
            btn_agregar_tasa_libre.setVisibility(View.GONE);
            btn_editar_tasa_libre.setVisibility(View.VISIBLE);
        }else {
            btn_agregar_tasa_libre.setVisibility(View.VISIBLE);
            btn_editar_tasa_libre.setVisibility(View.GONE);
        }
        costoIndirecto =0.0;
        totalEgreso =0.0;
        saldoFinal =0.0;
        materiaprima =0.0;
        manoobra =0.0;
        ventas =0.0;
        capitalRequerido =0.0;







        capitalRequerido = -InversionInicialFragment.capitalReque;


        ObtenerCostoIndirectos(getContext(), id_empresa);

        final int totalRequests = 3; // Total de solicitudes
        final AtomicInteger completedRequests = new AtomicInteger(0);

        ApiService apiService = new ApiService();

        apiService.obtenerventas(getContext(), id_empresa, new ApiService.VolleyCallback() {
            @Override
            public void onSuccess(double result) {
                ventas = result;

                System.out.println("Ventas" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }

            @Override
            public void onError(String result) {
                System.out.println("Error" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }
        });

        apiService.obtenermateriaprima(getContext(), id_empresa, new ApiService.VolleyCallback() {
            @Override
            public void onSuccess(double result) {
                materiaprima = result;
                totalEgreso += materiaprima;
                System.out.println("Materia Prima" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }

            @Override
            public void onError(String result) {
                System.out.println("Error" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }
        });

        apiService.obtenerManoObra(getContext(), id_empresa, new ApiService.VolleyCallback() {
            @Override
            public void onSuccess(double result) {
                manoobra = result;
                totalEgreso += manoobra;
                System.out.println("Mano Obra" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }

            @Override
            public void onError(String result) {
                System.out.println("Error" + result);
                checkIfAllRequestsCompleted(completedRequests.incrementAndGet(), totalRequests);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("se ejecuto on ViewCreate");
        lineChart = view.findViewById(R.id.line_chart);
        btn_agregar_inflacion = view.findViewById(R.id.agregar_inflacion);
        btn_agregar_tasa_libre = view.findViewById(R.id.agregar_tasa_libre);
        btn_editar_inflacion = view.findViewById(R.id.editar_inflacion);
        btn_editar_tasa_libre = view.findViewById(R.id.editar_tasa_libre);
        id_porcentaje = view.findViewById(R.id.id_porcentaje);
        inflacion = view.findViewById(R.id.inflacion);
        tasa_libre_riesgo = view.findViewById(R.id.tasa_libre_riesgo);
        obtenerPorcentajes(getContext(),id_empresa,inflacion,tasa_libre_riesgo,id_porcentaje);


        ingresos = view.findViewById(R.id.depreciacion_anual);
        egresos = view.findViewById(R.id.depreciacion_mensual);



        btn_agregar_inflacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInflacion(getContext(),inflacion,tasa_libre_riesgo,id_porcentaje,false);

            }
        });
        btn_editar_inflacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInflacion(getContext(),inflacion,tasa_libre_riesgo,id_porcentaje,true);
            }
        });
        btn_agregar_tasa_libre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTasaLibre(getContext(),inflacion,tasa_libre_riesgo,id_porcentaje,false);
            }
        });
        btn_editar_tasa_libre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTasaLibre(getContext(),inflacion,tasa_libre_riesgo,id_porcentaje,true);
            }
        });


        Description description = new Description();
        description.setPosition(150f,15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);

        valores_x = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            valores_x.add("Año " + i);
        }

        XAxis xAxis= lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valores_x));
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3);

        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(10000000f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setGranularity(2f);
        yAxis.setLabelCount(3);


        List<Entry> entrada1= new ArrayList<>();
        entrada1.add(new Entry(0,2000000));
        entrada1.add(new Entry(1,10000));
        entrada1.add(new Entry(2,9000000));
        entrada1.add(new Entry(3,300));
        entrada1.add(new Entry(4,30000));
        entrada1.add(new Entry(5,4000000));

        List<Entry> entrada2= new ArrayList<>();
        entrada2.add(new Entry(0,100));
        entrada2.add(new Entry(1,3000000));
        entrada2.add(new Entry(2,90000));
        entrada2.add(new Entry(3,1000000));
        entrada2.add(new Entry(4,150));
        entrada2.add(new Entry(5,2000000));






        LineDataSet dataSet1=new LineDataSet(entrada1,"prueba 1");
        dataSet1.setColor(Color.BLUE);

        LineDataSet dataSet2=new LineDataSet(entrada2,"prueba 3");
        dataSet1.setColor(Color.RED);

        LineData lineData=new LineData(dataSet1,dataSet2);
        lineChart.setData(lineData);




        lineChart.setScaleMinima(2f,0f);


        lineChart.invalidate();






    }


    private void checkIfAllRequestsCompleted(int completedRequests, int totalRequests) {
        if (completedRequests == totalRequests) {

            System.out.println("TotalEgreso: " + totalEgreso);
        }
    }

    private void inversionyrecuperar() {
        // Año 1
        inversiones.clear();
        recuperaciones.clear();
        double inversionesAno1 = capitalRequerido;
        double recuperacionAno1 = inversionesAno1 -(-saldoFinal) ;

        inversiones.add(inversionesAno1);
        recuperaciones.add(recuperacionAno1);

        System.out.println("Año 1:");
        System.out.println("Inversión: " + inversionesAno1);
        System.out.println("Recuperación de la inversión: " + recuperacionAno1);

        // Años 2 y 3
        for (int i = 1; i < 50; i++) {
            double nuevaInversion = recuperaciones.get(i - 1);
            double nuevaRecuperacion = nuevaInversion -(-saldoFinal);

            inversiones.add(nuevaInversion);
            recuperaciones.add(nuevaRecuperacion);


        }
    }

    private void actualizarGrafico() {
        // Primero, limpiar los datos existentes del gráfico
        lineChart.clear();
        List<Entry> entradaInversiones = new ArrayList<>();
        List<Entry> entradaRecuperaciones = new ArrayList<>();

        for (int i = 0; i < inversiones.size(); i++) {
            entradaInversiones.add(new Entry(i, inversiones.get(i).floatValue()));
            entradaRecuperaciones.add(new Entry(i, recuperaciones.get(i).floatValue()));
        }

        LineDataSet dataSetInversiones = new LineDataSet(entradaInversiones, "Inversiones");
        dataSetInversiones.setColor(Color.BLUE);

        LineDataSet dataSetRecuperaciones = new LineDataSet(entradaRecuperaciones, "Recuperaciones");
        dataSetRecuperaciones.setColor(Color.RED);

        LineData lineData = new LineData(dataSetInversiones, dataSetRecuperaciones);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refrescar el gráfico
    }




    public void ObtenerCostoIndirectos(Context context, int id_empresa) {
        Double[] cantidad = {0.0};
        Double[] Depreciacion = {0.0};
        Double[] Amortizacion = {0.0};
        Double[] SueldoEmprendedor = {0.0};
        final Double[] total = {0.0};
        ClsCostosIndirectos.totalCostosIndirectos(context,id_empresa, new ClsCostosIndirectos.TotalCallback() {
            @Override
            public void onSuccess(double totalCi) {
                cantidad[0] = totalCi;

                ClsGastoPersonal.totalSueldoEmprendedor(context,id_empresa, new ClsGastoPersonal.SueldoCallback() {
                    @Override
                    public void onSuccess(double totalSueldo) {
                        SueldoEmprendedor[0] = totalSueldo;

                        ClsActivoFijo.totalDepreciacion(context,id_empresa, new ClsActivoFijo.SueldoCallback() {
                            @Override
                            public void onSuccess(double total_costos_indirectos) {
                                Depreciacion[0] = total_costos_indirectos;

                                ClsActivoFijo.totalAmortizacion(context,id_empresa, new ClsActivoFijo.SueldoCallback() {
                                    @Override
                                    public void onSuccess(double total_costos_indirectos) {
                                        Amortizacion[0] = total_costos_indirectos;
                                        System.out.println(total_costos_indirectos);
                                        total[0] = cantidad[0] + SueldoEmprendedor[0] + Depreciacion[0] + Amortizacion[0];

                                        total[0] = total[0] - (Depreciacion[0] + Amortizacion[0]);



                                        costoIndirecto = total[0]*12;

                                        totalEgreso += costoIndirecto;

                                        saldoFinal = ventas - totalEgreso;

                                        String formattedTotal = String.format("$ %.2f", totalEgreso);
                                        String formattedVentas = String.format("$ %.2f", ventas);

                                        ingresos.setText(formattedVentas);
                                        egresos.setText(formattedTotal);


                                        inversionyrecuperar();
                                        actualizarGrafico();


                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flujo_inversion, container, false);
    }
    public void obtenerPorcentajes(Context context,int id_empresa, TextView inflacion, TextView tasa_libre_riesgo, TextView id_porcentaje){
        ClsPorcentajesInversion.fetchPI(getContext(), id_empresa, new ClsPorcentajesInversion.porcentajes() {
            @Override
            public void onSuccess(ClsPorcentajesInversion porcentajes) {
                String id = porcentajes.id;
                String formattedInflacion = String.format("%.1f %%", porcentajes.inflacion);
                String formattedTasaLibre = String.format("%.1f %%", porcentajes.tasa_libre_riesgo);

                id_porcentaje.setText(id);
                inflacion.setText(formattedInflacion);
                tasa_libre_riesgo.setText(formattedTasaLibre);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }
    private void dialogInflacion(Context context, TextView inflacion, TextView tasa_libre_riesgo,TextView id_porcentaje,Boolean editar){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View viewDialog= LayoutInflater.from(context).inflate(R.layout.activity_agregar_porcentajes,null);

        EditText editTextInflacion = viewDialog.findViewById(R.id.inflacion);
        EditText editTextTasaLibreRiesgo = viewDialog.findViewById(R.id.tasa_libre_riesgo);
        LinearLayout contenedorTasaLibre = viewDialog.findViewById(R.id.contenedor_tasa_libre_riesgo);
        String idPorcentaje = id_porcentaje.getText().toString();

        contenedorTasaLibre.setVisibility(View.GONE);
        if (editar == true){
            editTextInflacion.setText(inflacion.getText().toString().replace("%","").trim());
        }

        if (tasa_libre_riesgo.getText().toString().isEmpty()){
            editTextTasaLibreRiesgo.setText("0");
        }else {
            editTextTasaLibreRiesgo.setText(tasa_libre_riesgo.getText().toString());
        }

        alertDialogBuilder.setView(viewDialog)
                .setCancelable(false)
                .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        System.out.println("si funciona");
                        if (editar == true || idPorcentaje != ""){
                            System.out.println("id valor "+idPorcentaje);
                            Double valor_inflacion = Double.parseDouble(editTextInflacion.getText().toString().trim());
                            Double valor_tasa_libre = Double.parseDouble(editTextTasaLibreRiesgo.getText().toString().replace("%", "").trim());
                            editar_porcentajes(id_porcentaje.getText().toString(),valor_inflacion,valor_tasa_libre, id_empresa, new VolleyCallback(){
                                @Override
                                public void onSuccess(boolean success) {
                                    if (success) {
                                        obtenerPorcentajes(getContext(),id_empresa,inflacion,tasa_libre_riesgo,id_porcentaje);
                                        if (valor_inflacion == 0){
                                            btn_agregar_inflacion.setVisibility(View.VISIBLE);
                                            btn_editar_inflacion.setVisibility(View.GONE);
                                        }else {
                                            btn_agregar_inflacion.setVisibility(View.GONE);
                                            btn_editar_inflacion.setVisibility(View.VISIBLE);
                                        }



                                        Toast.makeText(getContext(), "valor editado", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    System.out.println("error al agregar"+errorMessage);

                                    // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();
                                }
                            });

                        }else {
                            Double valor_inflacion = Double.parseDouble(editTextInflacion.getText().toString().trim());
                            Double valor_tasa_libre = Double.parseDouble(editTextTasaLibreRiesgo.getText().toString().replace("%", "").trim());
                            agregar_porcentajes(valor_inflacion,valor_tasa_libre, id_empresa, new VolleyCallback(){
                                @Override
                                public void onSuccess(boolean success) {
                                    if (success) {
                                        obtenerPorcentajes(getContext(),id_empresa,inflacion,tasa_libre_riesgo,id_porcentaje);
                                        btn_agregar_inflacion.setVisibility(View.GONE);
                                        btn_editar_inflacion.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    System.out.println("error al agregar"+errorMessage);

                                    // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();
                                }
                            });

                        }

                    }
                })
                .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();

    }
    private void dialogTasaLibre(Context context, TextView inflacion, TextView tasa_libre_riesgo,TextView id_porcentaje,Boolean editar){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View viewDialog= LayoutInflater.from(context).inflate(R.layout.activity_agregar_porcentajes,null);

        EditText editTextInflacion = viewDialog.findViewById(R.id.inflacion);
        EditText editTextTasaLibreRiesgo = viewDialog.findViewById(R.id.tasa_libre_riesgo);
        LinearLayout contenedorInflacion = viewDialog.findViewById(R.id.contenedor_inflacion);
        String idPorcentaje = id_porcentaje.getText().toString();

        contenedorInflacion.setVisibility(View.GONE);
        if (editar == true){
            editTextTasaLibreRiesgo.setText(tasa_libre_riesgo.getText().toString().replace("%","").trim());
        }

        if (inflacion.getText().toString().isEmpty()){
            editTextInflacion.setText("0");
        }else {
            editTextInflacion.setText(inflacion.getText().toString());
        }

        alertDialogBuilder.setView(viewDialog)
                //.setMessage("¿Esta seguro de eliminar este producto?")
                .setCancelable(false)
                .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        System.out.println("si funciona");
                        if (editar == true || idPorcentaje != ""){
                            System.out.println("id valor "+idPorcentaje);
                            Double valor_inflacion = Double.parseDouble(editTextInflacion.getText().toString().trim().replace("%", "").trim());
                            Double valor_tasa_libre = Double.parseDouble(editTextTasaLibreRiesgo.getText().toString().trim());
                            editar_porcentajes(id_porcentaje.getText().toString(),valor_inflacion,valor_tasa_libre, id_empresa, new VolleyCallback(){
                                @Override
                                public void onSuccess(boolean success) {
                                    if (success) {
                                        obtenerPorcentajes(getContext(),id_empresa,inflacion,tasa_libre_riesgo,id_porcentaje);
                                        if (valor_tasa_libre == 0){
                                            btn_agregar_tasa_libre.setVisibility(View.VISIBLE);
                                            btn_editar_tasa_libre.setVisibility(View.GONE);
                                        }else {
                                            btn_agregar_tasa_libre.setVisibility(View.GONE);
                                            btn_editar_tasa_libre.setVisibility(View.VISIBLE);
                                        }



                                        Toast.makeText(getContext(), "valor editado", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    System.out.println("error al agregar"+errorMessage);

                                    // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();
                                }
                            });

                        }else {
                            Double valor_inflacion = Double.parseDouble(editTextInflacion.getText().toString().replace("%", "").trim());
                            Double valor_tasa_libre = Double.parseDouble(editTextTasaLibreRiesgo.getText().toString().trim());
                            agregar_porcentajes(valor_inflacion,valor_tasa_libre, id_empresa, new VolleyCallback(){
                                @Override
                                public void onSuccess(boolean success) {
                                    if (success) {
                                        obtenerPorcentajes(getContext(),id_empresa,inflacion,tasa_libre_riesgo,id_porcentaje);
                                        btn_agregar_tasa_libre.setVisibility(View.GONE);
                                        btn_editar_tasa_libre.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    System.out.println("error al agregar"+errorMessage);

                                    // Aqui manejas el caso en el que ocurrió un error al quardar Toast.makeText(getContext(), errorMessage, Toast.LENGTH SHORT).show();
                                }
                            });

                        }

                    }
                })
                .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();

    }
    private void agregar_porcentajes(final Double inflacion, final Double tasa_libre_riesgo,final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("inflacion", String.valueOf(inflacion));
                datos.put("tasa_libre_riesgo",String.valueOf(tasa_libre_riesgo));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }
    private void editar_porcentajes(final String id, final Double inflacion, final Double tasa_libre_riesgo,final int id_empresa, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("inflacion", String.valueOf(inflacion));
                datos.put("tasa_libre_riesgo",String.valueOf(tasa_libre_riesgo));
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }

}
/*

        int size = 0;
        List<Entry> entrada1= new ArrayList<>();
        List<Entry> entrada2= new ArrayList<>();
        double valores1=2;
        double valores2=0;

        for (int i=0;i<100;i++){
            entrada1.add(new Entry(i, (float) valores1));
            entrada2.add(new Entry(i, (float) valores2));
            valores1=valores1+1;
            valores2+=1;
            size+=1;

        }


 */