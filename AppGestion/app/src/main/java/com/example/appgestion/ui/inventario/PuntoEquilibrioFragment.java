package com.example.appgestion.ui.inventario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class PuntoEquilibrioFragment extends Fragment {

    TextView CostoIndirectos;
    TextView IngresoVentas;
    TextView MateriaPrima;

    TextView ManoObra;

    TextView MargenGanacia;

    TextView ValorUtilidad;
    TextView PorcentajeUtilidad;
    TextView PuntoEquilibrio;
    private int id_empresa;
    private DecimalFormat decimalFormat = new DecimalFormat("$ ###,###.##");
    private String nombre_empresa;

    @Override
    public void onResume() {
        super.onResume();

        ObtenerCostoIndirectos(getContext(), CostoIndirectos,id_empresa);
        ObtenerIVyMP(getContext(), IngresoVentas, MateriaPrima,id_empresa);
        ObtenerManoObra(getContext(),id_empresa, ManoObra);





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CostoIndirectos = view.findViewById(R.id.PuntoEquiCI);
        IngresoVentas = view.findViewById(R.id.IngresoVentas);
        MateriaPrima = view.findViewById(R.id.MateriaPrima);
        ManoObra = view.findViewById(R.id.Mano_obra);
        MargenGanacia = view.findViewById(R.id.Margen_ganacia);
        ValorUtilidad = view.findViewById(R.id.valor_utilidad);
        PorcentajeUtilidad = view.findViewById(R.id.porcentaje_Utilidad);
        PuntoEquilibrio = view.findViewById(R.id.punto_equilibrio);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        nombre_empresa = sharedPreferences.getString("empresa_nombre", "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_punto_equilibrio, container, false);
    }
    public void ObtenerCostoIndirectos(Context context, TextView costoITotal, int id_empresa) {
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


                                        String formattedTotal = String.format("$ %.2f", total);


                                        costoITotal.setText(formattedTotal);
                                        calcularUtilidad(ValorUtilidad);
                                        CalcularPorcentajeUtilidad(PorcentajeUtilidad);
                                        CalcularPuntoEquilibrio(PuntoEquilibrio);


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

    public void ObtenerIVyMP(Context context, TextView totalIV, TextView totalMP,int id_empresa){
        final Double[] ingreso_ventas = {0.0};
        final Double[] materia_prima = {0.0};


        final Double total = 0.0;

        ClsMateriaPrima.readMPyIV(context, id_empresa, new ClsMateriaPrima.totalMPyIV() {
            @Override
            public void onSuccess(double costoTotal, double ventasTotales) {
                ingreso_ventas[0] = ventasTotales;
                materia_prima[0] = costoTotal;

                String formattedTotal = String.format("$ %.2f", ingreso_ventas);
                String formattedTotal2 = String.format("%.2f", materia_prima);

                totalIV.setText(formattedTotal);
                totalMP.setText(formattedTotal2);

                calcularMargen_ganacia(MargenGanacia);

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    public void ObtenerManoObra(Context context,int id_empresa, TextView totalMo){


        ClsManoObra.readTotalMO(getContext(), id_empresa, new ClsManoObra.totalMO() {
            @Override
            public void onSuccess(double totalManoObra) {

                String formattedTotal = String.format("$ %.2f", totalManoObra);


                totalMo.setText(formattedTotal);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });


    }

    public void calcularMargen_ganacia(TextView margen) {
        try {
            String ingresoVentasString = IngresoVentas.getText().toString();
            String materiaPrimaString = MateriaPrima.getText().toString();
            String manoObraString = ManoObra.getText().toString();

            ingresoVentasString = ingresoVentasString.replace("$", "");
            materiaPrimaString = materiaPrimaString.replace("$", "");
            materiaPrimaString = materiaPrimaString.replaceAll(",","");
            manoObraString = manoObraString.replace("$", "");





            double resultado = (Double.parseDouble(ingresoVentasString) - (Double.parseDouble(materiaPrimaString) + Double.parseDouble(manoObraString))) / Double.parseDouble(ingresoVentasString);
            int margenEntero = (int) Math.round(resultado * 100);
            margen.setText(margenEntero + " %");
        } catch (ArithmeticException e) {
            margen.setText("0%");
        }
    }

    public void calcularUtilidad(TextView valorUtilidad) {
        try {
            String ingresoVentasString = IngresoVentas.getText().toString();
            String materiaPrimaString = MateriaPrima.getText().toString();
            String manoObraString = ManoObra.getText().toString();
            String CostoInderectoString = CostoIndirectos.getText().toString();

            System.out.println(ingresoVentasString);
            System.out.println(materiaPrimaString);
            System.out.println(manoObraString);
            System.out.println(CostoInderectoString);

            ingresoVentasString = ingresoVentasString.replace("$", "");
            materiaPrimaString = materiaPrimaString.replace("$", "");
            manoObraString = manoObraString.replace("$", "");
            CostoInderectoString = CostoInderectoString.replace("$", "");





            double resultado = Double.parseDouble(ingresoVentasString) - (Double.parseDouble(materiaPrimaString) + Double.parseDouble(manoObraString) + Double.parseDouble(CostoInderectoString));
            String formattedTotal = String.format("$ %.2f", resultado);

            valorUtilidad.setText(formattedTotal);
        } catch (ArithmeticException e) {
            valorUtilidad.setText("$ 0");
        }
    }

    public void CalcularPorcentajeUtilidad(TextView porcentajeUtilidad){
        try {
            String ingresoVentasString = IngresoVentas.getText().toString();
            String ValorUtilidadString = ValorUtilidad.getText().toString();


            ingresoVentasString = ingresoVentasString.replace("$", "");
            ValorUtilidadString = ValorUtilidadString.replace("$", "");






            double resultado = Double.parseDouble(ValorUtilidadString) / Double.parseDouble(ingresoVentasString);
            int margenEntero = (int) Math.round(resultado * 100);
            porcentajeUtilidad.setText(margenEntero + " %");
        } catch (ArithmeticException e) {
            porcentajeUtilidad.setText("0%");
        }
    }

    public void CalcularPuntoEquilibrio(TextView puntoEquilibrio){
        try {
            String CostoIndirectosString = CostoIndirectos.getText().toString();

            String ingresoVentasString = IngresoVentas.getText().toString();
            String materiaPrimaString = MateriaPrima.getText().toString();
            String manoObraString = ManoObra.getText().toString();

            ingresoVentasString = ingresoVentasString.replace("$", "");
            materiaPrimaString = materiaPrimaString.replace("$", "");
            manoObraString = manoObraString.replace("$", "");


            double resultado1 = (Double.parseDouble(ingresoVentasString) - (Double.parseDouble(materiaPrimaString) + Double.parseDouble(manoObraString))) / Double.parseDouble(ingresoVentasString);


            CostoIndirectosString = CostoIndirectosString.replace("$", "");


            double resultado = Double.parseDouble(CostoIndirectosString) / resultado1;
            double resultadoRedondeado = Math.round(resultado * 100.0) / 100.0;
            String formattedTotal = String.format("$ %.2f", resultadoRedondeado);


            puntoEquilibrio.setText(formattedTotal);
        } catch (ArithmeticException e) {
            puntoEquilibrio.setText("$ 0");
        }
    }
}