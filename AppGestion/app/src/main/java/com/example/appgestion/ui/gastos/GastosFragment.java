package com.example.appgestion.ui.gastos;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adaptadores.CostosAdapter;
import adaptadores.GastosAdapter;
import android.util.Log;
public class GastosFragment extends Fragment {

    private Button btn_gastos,btn_costos,btn_verGastos,btn_verCostos;
    private ProgressBar necesarios, noNecesarios;
    private TextView sueldo,depreciacion,amortizacion,costoIndrectoTotal,gasto_sin_datos,costo_sin_datos;
    private RecyclerView mostrar, mostrar_costos;
    private int id_empresa;
    private String nombre_empresa;
    private List<ClsGastoPersonal> listGastosOrdenada= new ArrayList<>();
    private List<ClsCostosIndirectos>listCostosOrdenados=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = getContext().getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        nombre_empresa = sharedPreferences.getString("empresa_nombre", "");
    }

    @Override
    public void onPause() {
        super.onPause();
        listGastosOrdenada.clear();
        listCostosOrdenados.clear();
        System.out.println("se ejecuto onpause de gastos");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("se ejecuto ondestroy de gastos");
    }

    @Override
    public void onResume() {
        super.onResume();
        //agregar gastos personales
        calcularValores(getContext(),necesarios,noNecesarios,sueldo,id_empresa);
        ObtenerCostoIndirectos(getContext(),costoIndrectoTotal,depreciacion,amortizacion,id_empresa);
        ClsGastoPersonal.readGPall(getContext(),id_empresa, new ClsGastoPersonal.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsGastoPersonal> gastosList) {
                ordenarGastosMenorAMayor(gastosList);
                if (gastosList.size()>=5){
                    for (int i =gastosList.size()-1;i>=gastosList.size()-5;i--){
                        ClsGastoPersonal gastoPer = gastosList.get(i);
                        listGastosOrdenada.add(gastoPer);

                    }
                }else {
                    for (int i =gastosList.size()-1;i>=0;i--){
                        ClsGastoPersonal gastoPer = gastosList.get(i);
                        listGastosOrdenada.add(gastoPer);

                    }

                }
                GastosAdapter adapter =new GastosAdapter(getContext(),listGastosOrdenada,id_empresa);
                mostrar.setAdapter(adapter);
                mostrar.setVisibility(View.VISIBLE);
                gasto_sin_datos.setVisibility(View.GONE);

                System.out.println("importe ordenado"+listGastosOrdenada);






            }

            @Override
            public void onFailure(String errorMessage) {
                gasto_sin_datos.setVisibility(View.VISIBLE);
                mostrar.setVisibility(View.GONE);
                sueldo.setText("");
                necesarios.setProgress(0);
                noNecesarios.setProgress(0);
                //Toast.makeText(GastosFragment.this.requireContext(), "Error de gastos: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

        //agregar costos indirectos

        ClsCostosIndirectos.readCIall(getContext(),id_empresa, new ClsCostosIndirectos.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsCostosIndirectos> indirectosList) {
                ordenarCostosMenorAMayor(indirectosList);
                if (indirectosList.size()>=5){
                    for (int i =indirectosList.size()-1;i>=indirectosList.size()-5;i--){
                        ClsCostosIndirectos costosIndirectos = indirectosList.get(i);
                        listCostosOrdenados.add(costosIndirectos);

                    }
                }else {
                    for (int i =indirectosList.size()-1;i>=0;i--){
                        ClsCostosIndirectos costosIndirectos = indirectosList.get(i);
                        listCostosOrdenados.add(costosIndirectos);

                    }

                }
                CostosAdapter costosAdapter = new CostosAdapter(getContext(),listCostosOrdenados,id_empresa);
                mostrar_costos.setAdapter(costosAdapter);
                mostrar_costos.setVisibility(View.VISIBLE);
                costo_sin_datos.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(String errorMessage) {
                costo_sin_datos.setVisibility(View.VISIBLE);
                mostrar_costos.setVisibility(View.GONE);
                //Toast.makeText(GastosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        //

        btn_gastos=view.findViewById(R.id.btn_gastos);
        btn_costos=view.findViewById(R.id.btn_costos);
        btn_verGastos = view.findViewById(R.id.btn_ver_todos_gastos);
        btn_verCostos = view.findViewById(R.id.btn_ver_todos_costos);
        necesarios=view.findViewById(R.id.necesarios);
        noNecesarios = view.findViewById(R.id.noNecesarios);
        sueldo = view.findViewById(R.id.sueldo);
        gasto_sin_datos = view.findViewById(R.id.gasto_sin_datos);
        costo_sin_datos=view.findViewById(R.id.costo_sin_datos);
        mostrar = view.findViewById(R.id.mostrar);
        mostrar_costos = view.findViewById(R.id.mostrar_costos);
        depreciacion = view.findViewById(R.id.depreciacion);
        amortizacion = view.findViewById(R.id.amortizacion);
        costoIndrectoTotal = view.findViewById(R.id.txt_costo_indirecto_total);




        mostrar.setHasFixedSize(true);
        mostrar_costos.setHasFixedSize(true);

        mostrar.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        mostrar_costos.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));






        btn_gastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formGastosP=new Intent(getContext(),AgregarGastos.class);
                formGastosP.putExtra("agregar_gastosP", true);
                formGastosP.putExtra("id_empresa",id_empresa);
                System.out.println(id_empresa);
                startActivity(formGastosP);

                //Snackbar.make(view, "si funciona el boton",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });
        btn_costos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formCostos=new Intent(getContext(),AgregarCostos.class);
                formCostos.putExtra("agregar_costo", true);
                formCostos.putExtra("id_empresa",id_empresa);
                startActivity(formCostos);

                //Snackbar.make(view, "si funciona el boton",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });
        btn_verGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verGastos=new Intent(getContext(),VerGastos.class);
                startActivity(verGastos);

            }
        });
        btn_verCostos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verCostos=new Intent(getContext(),VerCostos.class);
                startActivity(verCostos);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gastos, container, false);
    }
    private void ordenarGastosMenorAMayor(List<ClsGastoPersonal>gastosList){
        //ordenar lista
        Collections.sort(gastosList, new Comparator<ClsGastoPersonal>() {
            @Override
            public int compare(ClsGastoPersonal p1, ClsGastoPersonal p2) {
                return new Double(p1.importe).compareTo(new Double(p2.importe));
            }
        });
    }
    private void ordenarCostosMenorAMayor(List<ClsCostosIndirectos>costosIndirectos){
        Collections.sort(costosIndirectos, new Comparator<ClsCostosIndirectos>() {
            @Override
            public int compare(ClsCostosIndirectos p1, ClsCostosIndirectos p2) {
                return new Double(p1.importe).compareTo(new Double(p2.importe));
            }
        });
    }


    public void calcularValores(Context context, ProgressBar necesarios, ProgressBar noNecesarios, TextView sueldo, int id_empresa) {
        ClsGastoPersonal.readGPall(context,id_empresa, new ClsGastoPersonal.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsGastoPersonal> gastosList) {
                double totalNecesarios= 0;
                double totalNoNecesario=0;
                for (ClsGastoPersonal gasto:gastosList){
                    if (gasto.tipo.equals("Necesario")){
                        totalNecesarios += gasto.importe;
                    }else {
                        totalNoNecesario += gasto.importe;
                    }
                }
                sueldo.setText("$ "+decimalFormat.format(totalNecesarios));
                double total = totalNecesarios +totalNecesarios;
                int porcentajeNecesarios = (int) ((totalNecesarios/total)*100);
                int porcentajeNoNecesarios= (int) ((totalNoNecesario/total)*100);
                necesarios.setProgress(porcentajeNecesarios);
                noNecesarios.setProgress(porcentajeNoNecesarios);
            }



            @Override
            public void onFailure(String errorMessage) {


            }
        });

    }
    public void ObtenerCostoIndirectos(Context context,TextView costoITotal,TextView depreciacion,TextView amortizacion, int id_empresa) {
        Double[] cantidad = {0.0};
        Double[] Depreciacion = {0.0};
        Double[] Amortizacion = {0.0};
        Double[] SueldoEmprendedor = {0.0};

        final Double[] total = {0.0};


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
                                        Double totalCostos = total[0];
                                        Double valorDepreciacion= Depreciacion[0];
                                        Double valorAmortizacion=Amortizacion[0];



                                        //String formattedTotal = String.format("$ %.2f",total);
                                        //String formattedDepreciacion = String.format("$ %.2f", Depreciacion);
                                        //String formattedAmortizacion = String.format("$ %.2f", Amortizacion);

                                        costoITotal.setText(decimalFormat.format(totalCostos));
                                        depreciacion.setText(decimalFormat.format(valorDepreciacion));
                                        amortizacion.setText(decimalFormat.format(valorAmortizacion));
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
}