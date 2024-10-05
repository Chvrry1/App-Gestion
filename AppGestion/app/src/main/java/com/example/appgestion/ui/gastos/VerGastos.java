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

import adaptadores.GastosAdapter;

public class VerGastos extends AppCompatActivity {
    private RecyclerView ver_gastos;
    private ImageView atras;
    private List<ClsGastoPersonal>listGastosOrdenada=new ArrayList<>();
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
        listGastosOrdenada.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClsGastoPersonal.readGPall(this,id_empresa, new ClsGastoPersonal.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsGastoPersonal> gastosList) {

                ordenarGastosMenorAMayor(gastosList);
                for (int i =gastosList.size()-1;i>=0;i--){
                    ClsGastoPersonal gastoPer = gastosList.get(i);
                    listGastosOrdenada.add(gastoPer);

                }


                GastosAdapter adapter =new GastosAdapter(VerGastos.this,listGastosOrdenada,id_empresa);
                ver_gastos.setAdapter(adapter);

                System.out.println("importe ordenado"+listGastosOrdenada);



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
        setContentView(R.layout.activity_ver_gastos);
        ver_gastos=findViewById(R.id.lista_gastos_personales);
        atras = findViewById(R.id.devolver);



        ver_gastos.setHasFixedSize(true);
        ver_gastos.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));




        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ver_gastos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void ordenarGastosMenorAMayor(List<ClsGastoPersonal>gastosList){
        Collections.sort(gastosList, new Comparator<ClsGastoPersonal>() {
            @Override
            public int compare(ClsGastoPersonal p1, ClsGastoPersonal p2) {
                return new Double(p1.importe).compareTo(new Double(p2.importe));
            }
        });
    }
}