package com.example.appgestion.ui.inversion;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.ClsActivoDiferido;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;
import com.example.appgestion.ui.inventario.ClsManoObra;
import com.example.appgestion.ui.inventario.ClsMateriaPrima;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InversionInicialFragment extends Fragment {
    private TextView capital_inicial,total_activo_fijo,total_diferido, capital_trabajo_mensual,capital_trabajo_trimestral,capital_trabajo,capital_requerido,inversion_inicial,inversion_requerida;
    private int id_empresa;
    private String capital;
    private TextView capital_costos_indirectos, capital_mano_obra, capital_materia_prima;
    private BarChart barChartMensual,barChartTrimestral;
    private List<String> etiquetas_x_mensual;
    private List<ClsDatosGraficoCapital> valores_x_mensual = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    public static double capitalReque;


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ejecuto onstar");
        sharedPreferences = getContext().getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        capital = sharedPreferences.getString("capital", "");








    }


    @Override
    public void onResume() {
        super.onResume();

        System.out.println("ejecuto onresume");

        capital_inicial.setText(capital);
        totalActivoFijo(getContext(),total_activo_fijo,id_empresa);
        totalActivosDiferidos(getContext(),total_diferido,id_empresa);
        ObtenerCostoIndirectos(getContext(),capital_costos_indirectos,id_empresa);
        ObtenerManoObra(getContext(),capital_mano_obra, id_empresa);
        ObtenerIVyMP(getContext(),capital_materia_prima,id_empresa);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("ejecuto oncreateview");
        capital_inicial = view.findViewById(R.id.capital);
        total_activo_fijo = view.findViewById(R.id.total_activo_fijo);
        total_diferido = view.findViewById(R.id.pago_anticipado);
        capital_trabajo_mensual=view.findViewById(R.id.capital_trabajo_mensual);
        capital_trabajo_trimestral=view.findViewById(R.id.capital_trabajo_trimestral);
        capital_costos_indirectos =view.findViewById(R.id.capital_costos_indirectos);
        capital_mano_obra =view.findViewById(R.id.capital_mano_obra);
        capital_materia_prima = view.findViewById(R.id.capital_materia_prima);
        capital_trabajo = view.findViewById(R.id.capital_trabajo);
        capital_requerido = view.findViewById(R.id.capital_requerido);
        inversion_inicial=view.findViewById(R.id.inversion_inicial);
        inversion_requerida =view.findViewById(R.id.inversion_requerida);
        barChartMensual = view.findViewById(R.id.grefico_mensual);
        barChartTrimestral = view.findViewById(R.id.grafico_trimestral);







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("ejecuto oncreate");
        return inflater.inflate(R.layout.fragment_inversion_inicial, container, false);
    }
    public void ObtenerCostoIndirectos(Context context, TextView capital_costos_indirectos, int id_empresa) {
        Double[] cantidad = {0.0};
        Double[] Depreciacion = {0.0};
        Double[] Amortizacion = {0.0};
        Double[] SueldoEmprendedor = {0.0};

        final Double[] total = {0.0};
        final Double[] costos_indirectos_cap= {0.0};


        ClsCostosIndirectos.totalCostosIndirectos(context, id_empresa,new ClsCostosIndirectos.TotalCallback() {
            @Override
            public void onSuccess(double totalCi) {
                cantidad[0] = totalCi;
                System.out.println(cantidad[0]);

                ClsGastoPersonal.totalSueldoEmprendedor(context,id_empresa,new ClsGastoPersonal.SueldoCallback() {
                    @Override
                    public void onSuccess(double totalSueldo) {
                        SueldoEmprendedor[0] = totalSueldo;
                        System.out.println(totalSueldo);

                        ClsActivoFijo.totalDepreciacion(context,id_empresa, new ClsActivoFijo.SueldoCallback() {
                            @Override
                            public void onSuccess(double total_costos_indirectos) {
                                Depreciacion[0] = total_costos_indirectos;
                                System.out.println(total_costos_indirectos);

                                ClsActivoFijo.totalAmortizacion(context,id_empresa, new ClsActivoFijo.SueldoCallback() {
                                    @Override
                                    public void onSuccess(double total_costos_indirectos) {
                                        Amortizacion[0] = total_costos_indirectos;
                                        System.out.println(total_costos_indirectos);
                                        total[0] = cantidad[0] + SueldoEmprendedor[0] + Depreciacion[0] + Amortizacion[0];
                                        System.out.println(total[0]);
                                        costos_indirectos_cap[0] = total[0]-Amortizacion[0]-Depreciacion[0];

                                        System.out.println(costos_indirectos_cap);

                                        //costos_indirectos_capital = String.format("$ %.2f", costos_indirectos_cap);
                                        String valor_costos = String.format("%.2f", costos_indirectos_cap);

                                        capital_costos_indirectos.setText(valor_costos);
                                        ClsDatosGraficoCapital clsDatosGraficoCapital = new ClsDatosGraficoCapital("costos indirectos",Double.parseDouble(capital_costos_indirectos.getText().toString()));
                                        valores_x_mensual.add(clsDatosGraficoCapital);
                                        calcular_capital(capital_trabajo_mensual,capital_trabajo_trimestral,capital_trabajo,capital_requerido);
                                        calcular_inversion(inversion_inicial,inversion_requerida,capital_requerido);
                                        create_grafico_mensual(barChartMensual);
                                        create_grafico_trimestral(barChartTrimestral);




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
    public void ObtenerManoObra(Context context,TextView capital_mano_obra, int id_empresa){


        ClsManoObra.readTotalMO(getContext(), id_empresa, new ClsManoObra.totalMO() {
            @Override
            public void onSuccess(double totalManoObra) {

                String formattedTotal = String.format("%.2f", totalManoObra);
                //mano_obra =formattedTotal;
                capital_mano_obra.setText(formattedTotal);
                ClsDatosGraficoCapital clsDatosGraficoCapital = new ClsDatosGraficoCapital("Mano de obra",Double.parseDouble(capital_mano_obra.getText().toString()));
                valores_x_mensual.add(clsDatosGraficoCapital);

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });


    }
    public void ObtenerIVyMP(Context context,TextView capital_materia_prima,int id_empresa){
        final Double[] ingreso_ventas = {0.0};
        final Double[] materia_prima = {0.0};


        final Double total = 0.0;




        ClsMateriaPrima.readMPyIV(context, id_empresa, new ClsMateriaPrima.totalMPyIV() {

            @Override
            public void onSuccess(double costoTotal, double ventasTotales) {
                ingreso_ventas[0] = ventasTotales;
                materia_prima[0] = costoTotal;

                String formattedTotal = String.format("$ %.2f", ingreso_ventas);
                String valor_materia = String.format("%.2f", materia_prima);


                //materia_prima_total=valor_materia;
                capital_materia_prima.setText(valor_materia);
                ClsDatosGraficoCapital clsDatosGraficoCapital = new ClsDatosGraficoCapital("Materia prima",Double.parseDouble(capital_materia_prima.getText().toString()));
                valores_x_mensual.add(clsDatosGraficoCapital);
                //calcular_capital(capital_trabajo_mensual,capital_trabajo_trimestral);



            }

            @Override
            public void onFailure(String errorMessage) {

            }

        });


    }
    public boolean isValidDouble(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void calcular_capital(TextView capital_trabajo_mensual, TextView capital_trabajo_trimestral, TextView capital_trabajo, TextView capital_requerido) {
        try {
            String capitalCostosIndirectos= capital_costos_indirectos.getText().toString();
            String capitalManoObra = capital_mano_obra.getText().toString();
            String capitalMateriaPrima = capital_materia_prima.getText().toString();
            //String inversionRequerida = inversion_requerida.getText().toString();


            //inversionRequerida = inversionRequerida.replace("$", "");
            //capitalDisponible = capitalDisponible.replace("$", "");
            double capitalMensual = 0.0;
            double capitalTrimestral = 0.0;

            if (isValidDouble(capitalCostosIndirectos)) {
                capitalMensual += Double.parseDouble(capitalCostosIndirectos);
                capitalTrimestral += Double.parseDouble(capitalCostosIndirectos) * 3;
            }
            if (isValidDouble(capitalManoObra)) {
                capitalMensual += Double.parseDouble(capitalManoObra);
                capitalTrimestral += Double.parseDouble(capitalManoObra) * 3;
            }
            if (isValidDouble(capitalMateriaPrima)) {
                capitalMensual += Double.parseDouble(capitalMateriaPrima);
                capitalTrimestral += Double.parseDouble(capitalMateriaPrima) * 3;
            }



            //double capitalMensual = Double.parseDouble(capitalCostosIndirectos) + Double.parseDouble(capitalManoObra) + Double.parseDouble(capitalMateriaPrima);
            //double capitalTrimestral = Double.parseDouble(capitalCostosIndirectos)*3 + Double.parseDouble(capitalManoObra)*3 + Double.parseDouble(capitalMateriaPrima)*3;
            //double capitalRequerido = Double.parseDouble(inversionRequerida);
            String formattedMensual = String.format("$ %.2f", capitalMensual);
            String formattedTrimestral = String.format("$ %.2f", capitalTrimestral);
            //String formattedCapitalRequerido = String.format("$ %.2f", capitalRequerido);

            capital_trabajo_mensual.setText(formattedMensual);
            capital_trabajo_trimestral.setText(formattedTrimestral);
            capital_trabajo.setText(formattedTrimestral);
            //capital_requerido.setText(formattedCapitalRequerido);
        } catch (ArithmeticException e) {
            capital_trabajo_mensual.setText("0-");
        }
    }
    public void calcular_inversion(TextView inversion_incial,TextView inversion_requerida, TextView capital_requerido) {
        try {
            String totalActivosFijos = total_activo_fijo.getText().toString().replace("$", "").trim();
            String totalActivosDiferidos = total_diferido.getText().toString().replace("$", "").trim();
            String capitalTrabajo = capital_trabajo.getText().toString().replace("$", "").trim();
            String capitalInicial = capital_inicial.getText().toString().replace("$", "").trim();

            double inversionInicial = 0.0;
            double inversionRequerida = 0.0;
            double capitalRequerido = 0.0;

            if (isValidDouble(totalActivosFijos)) {
                inversionInicial += Double.parseDouble(totalActivosFijos);
            }
            if (isValidDouble(totalActivosDiferidos)) {
                inversionInicial += Double.parseDouble(totalActivosDiferidos);
            }
            if (isValidDouble(capitalTrabajo)) {
                inversionRequerida = inversionInicial + Double.parseDouble(capitalTrabajo);
            }
            if (isValidDouble(capitalInicial)) {
                capitalRequerido = inversionRequerida - Double.parseDouble(capitalInicial);
            }

            String formattedInversionInicial = String.format("$ %.2f", inversionInicial);
            String formattedInversionRequerida = String.format("$ %.2f", inversionRequerida);
            String formattedCapitalRequerido = String.format("$ %.2f", capitalRequerido);

            inversion_incial.setText(formattedInversionInicial);
            inversion_requerida.setText(formattedInversionRequerida);
            capital_requerido.setText(formattedCapitalRequerido);
        } catch (NumberFormatException e) {
            System.out.println("error en la fiuncion de inversion "+e);
            capital_trabajo_mensual.setText("0-");
        }
    }


    public void create_grafico_mensual(BarChart grafico_mensual) {
        try {
            grafico_mensual.getAxisRight().setDrawLabels(false);
            //etiquetas_x_mensual = Arrays.asList("costos indirectos", "mano de obra", "materia prima");
            ArrayList<BarEntry> entriesMensual = new ArrayList<>();

            etiquetas_x_mensual = new ArrayList<>();
            for (int i=0; i<valores_x_mensual.size();i++){
                ClsDatosGraficoCapital clsDatosGraficoCapital = valores_x_mensual.get(i);
                entriesMensual.add(new BarEntry(i, (float) clsDatosGraficoCapital.getValor()));
                etiquetas_x_mensual.add(clsDatosGraficoCapital.getEtiqueta());
            }


            YAxis yAxis = barChartMensual.getAxisLeft();
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);

            BarDataSet barDataSet = new BarDataSet(entriesMensual,"");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextSize(12f);

            BarData barData = new BarData(barDataSet);
            barChartMensual.setData(barData);

            barChartMensual.getDescription().setEnabled(false);
            barChartMensual.invalidate();

            barChartMensual.getXAxis().setValueFormatter(new IndexAxisValueFormatter(etiquetas_x_mensual));
            barChartMensual.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChartMensual.getXAxis().setGranularity(1f);
            barChartMensual.getXAxis().setGranularityEnabled(true);

            barChartMensual.animateXY(1000,1000);



        } catch (Exception e) {
            System.out.println("error en el grafico"+e);

        }
    }
    public void create_grafico_trimestral(BarChart grafico_trimestral) {
        try {
            grafico_trimestral.getAxisRight().setDrawLabels(false);
            //etiquetas_x_mensual = Arrays.asList("costos indirectos", "mano de obra", "materia prima");
            ArrayList<BarEntry> entriesTrimestral = new ArrayList<>();

            etiquetas_x_mensual = new ArrayList<>();
            for (int i=0; i<valores_x_mensual.size();i++){
                ClsDatosGraficoCapital clsDatosGraficoCapital = valores_x_mensual.get(i);
                entriesTrimestral.add(new BarEntry(i, (float) clsDatosGraficoCapital.getValor()*3));
                etiquetas_x_mensual.add(clsDatosGraficoCapital.getEtiqueta());
            }


            YAxis yAxis = barChartTrimestral.getAxisLeft();
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);

            BarDataSet barDataSet = new BarDataSet(entriesTrimestral,"");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextSize(12f);

            BarData barData = new BarData(barDataSet);
            barChartTrimestral.setData(barData);

            barChartTrimestral.getDescription().setEnabled(false);
            barChartTrimestral.invalidate();

            barChartTrimestral.getXAxis().setValueFormatter(new IndexAxisValueFormatter(etiquetas_x_mensual));
            barChartTrimestral.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChartTrimestral.getXAxis().setGranularity(1f);
            barChartTrimestral.getXAxis().setGranularityEnabled(true);

            barChartMensual.animateXY(1000,1000);

            etiquetas_x_mensual.clear();
            valores_x_mensual.clear();

        } catch (Exception e) {
            System.out.println("error en el grafico"+e);

        }
    }



    public void totalActivoFijo(Context context, TextView costo_activos, int id_empresa) {
        ClsActivoFijo.readAFall(context,id_empresa, new ClsActivoFijo.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoFijo> activoFijoList) {


                double total_activos=0;
                for (ClsActivoFijo activoF:activoFijoList){
                    total_activos += activoF.unidades * activoF.valor_unitario;
                }

                //DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                costo_activos.setText("$ "+decimalFormat.format(total_activos));
                //double total = totalNecesarios +totalNecesarios;
                //int porcentajeNecesarios = (int) ((totalNecesarios/total)*100);
                //int porcentajeNoNecesarios= (int) ((totalNoNecesario/total)*100);

            }



            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }
    public void totalActivosDiferidos(Context context,TextView pago_anticipado, int id_empresa) {
        ClsActivoDiferido.readAFall(context,id_empresa, new ClsActivoDiferido.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoDiferido> activoDiferidoList) {
                double pagoAnticipado=0;
                for (ClsActivoDiferido activoD:activoDiferidoList){
                    pagoAnticipado += activoD.pago_anticipado;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                pago_anticipado.setText("$ "+decimalFormat.format(pagoAnticipado));
                //double total = totalNecesarios +totalNecesarios;
                //int porcentajeNecesarios = (int) ((totalNecesarios/total)*100);
                //int porcentajeNoNecesarios= (int) ((totalNoNecesario/total)*100);

            }



            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }




}