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

import adaptadores.ActivosFijosAdapter;

public class AFijosFragment extends Fragment {

    RecyclerView mostrar_activos_fijos;
    TextView depreciacion_anual,depreciacion_mensual, costo_activos;

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


        ClsActivoFijo.readAFall(getContext(),id_empresa, new ClsActivoFijo.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoFijo> activoFijoList) {

                ActivosFijosAdapter adapter =new ActivosFijosAdapter(getContext(),activoFijoList,id_empresa);
                mostrar_activos_fijos.setAdapter(adapter);
                calcularValores(getContext(),depreciacion_anual,depreciacion_mensual,costo_activos,id_empresa);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AFijosFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        depreciacion_anual= view.findViewById(R.id.depreciacion_anual);
        depreciacion_mensual=view.findViewById(R.id.depreciacion_mensual);
        costo_activos =view.findViewById(R.id.costo_activos);
        mostrar_activos_fijos =view.findViewById(R.id.lista_fijos);
        //mostrar_activos_fijos.setHasFixedSize(true);
        //mostrar_activos_fijos.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        mostrar_activos_fijos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));





    }
    public void calcularValores(Context context, TextView depreciacion_anual, TextView depreciacion_mensual, TextView costo_activos, int id_empresa) {
        ClsActivoFijo.readAFall(context,id_empresa, new ClsActivoFijo.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsActivoFijo> activoFijoList) {
                double depreciciacionAnual= 0;
                double depreciacionMensual=0;
                double total_activos=0;
                for (ClsActivoFijo activoF:activoFijoList){
                    double calculo = 0;
                    calculo = (activoF.valor_unitario * activoF.unidades)/activoF.vida_util;
                    depreciciacionAnual += calculo;
                    depreciacionMensual += calculo/12;
                    total_activos += activoF.unidades * activoF.valor_unitario;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                depreciacion_anual.setText("$ "+ decimalFormat.format(depreciciacionAnual));
                depreciacion_mensual.setText("$ "+ decimalFormat.format(depreciacionMensual));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_fijos, container, false);
    }
}