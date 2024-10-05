package com.example.appgestion.ui.gastos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adaptadores.CostosAdapter;
import adaptadores.GastosAdapter;

public class VerCostos extends AppCompatActivity {
    private RecyclerView ver_costos_indirectos;
    private ImageView atras;
    private List<ClsCostosIndirectos> listCostosOrdenada=new ArrayList<>();
    private int id_empresa;
    private SharedPreferences sharedPreferences;

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = this.getSharedPreferences("EmpresaPrefs", MODE_PRIVATE);
        id_empresa = sharedPreferences.getInt("empresa_id", 0);
    }
    @Override
    protected void onPause() {
        super.onPause();
        listCostosOrdenada.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClsCostosIndirectos.readCIall(this,id_empresa, new ClsCostosIndirectos.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsCostosIndirectos> costosIndirectos) {

                ordenarCostosMenorAMayor(costosIndirectos);
                for (int i =costosIndirectos.size()-1;i>=0;i--){
                    ClsCostosIndirectos costoInd = costosIndirectos.get(i);
                    listCostosOrdenada.add(costoInd);

                }


                CostosAdapter adapter =new CostosAdapter(VerCostos.this,listCostosOrdenada,id_empresa);
                ver_costos_indirectos.setAdapter(adapter);

                System.out.println("importe ordenado"+listCostosOrdenada);



            }

            @Override
            public void onFailure(String errorMessage) {
                //Toast.makeText(GastosFragment.this.requireContext(), "Error de gastos: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_costos);

        ver_costos_indirectos=findViewById(R.id.lista_costos_indirectos);
        atras = findViewById(R.id.devolver);



        ver_costos_indirectos.setHasFixedSize(true);
        ver_costos_indirectos.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));




        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ver_costos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
}