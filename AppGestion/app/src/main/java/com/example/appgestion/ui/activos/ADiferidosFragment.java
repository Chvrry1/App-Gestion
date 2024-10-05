package com.example.appgestion.ui.activos;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;

import java.text.DecimalFormat;
import java.util.List;

import adaptadores.ActivosDiferidoAdapter;


public class ADiferidosFragment extends Fragment {

    RecyclerView mostrar_diferidos;

    TextView amortizacion_anual, amortizacion_mensual, pago_anticipado;

    private int id_empresa;
    private String nombre_empresa;
    private SharedPreferences sharedPreferences;

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = getContext().getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
        nombre_empresa = sharedPreferences.getString("empresa_nombre", "");
    }


    @Override
    public void onResume() {
        super.onResume();


        ClsActivoDiferido.readAFall(getContext(),id_empresa, new ClsActivoDiferido.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoDiferido> activoDiferidoList) {

                ActivosDiferidoAdapter adapter =new ActivosDiferidoAdapter(getContext(),activoDiferidoList,id_empresa);
                mostrar_diferidos.setAdapter(adapter);
                calcularValores(getContext(),amortizacion_anual,amortizacion_mensual,pago_anticipado,id_empresa);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ADiferidosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amortizacion_anual=view.findViewById(R.id.amortizacion_anual);
        amortizacion_mensual=view.findViewById(R.id.amortizacion_mensual);
        pago_anticipado=view.findViewById(R.id.pago_anticipado);
        mostrar_diferidos =view.findViewById(R.id.lista_diferidos);
        mostrar_diferidos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }
    public void calcularValores(Context context, TextView amortizacion_anual, TextView amortizacion_mensual, TextView pago_anticipado, int id_empresa) {
        ClsActivoDiferido.readAFall(context,id_empresa, new ClsActivoDiferido.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoDiferido> activoDiferidoList) {
                double amortizacionAnual= 0;
                double amortizacionMensual=0;
                double pagoAnticipado=0;
                for (ClsActivoDiferido activoD:activoDiferidoList){
                    double calculo = 0;
                    calculo = activoD.pago_anticipado/ Double.valueOf(activoD.vigencia);
                    amortizacionAnual += calculo;
                    amortizacionMensual += calculo/12;
                    pagoAnticipado += activoD.pago_anticipado;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                amortizacion_anual.setText("$ "+ decimalFormat.format(amortizacionAnual));
                amortizacion_mensual.setText("$ "+ decimalFormat.format(amortizacionMensual));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_diferidos, container, false);
    }
}