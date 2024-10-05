package com.example.appgestion.ui.inventario;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;

import java.util.List;

import adaptadores.ManoObraAdapter;


public class ManoObraFragment extends Fragment {
    private RecyclerView mostrar_mano_obra;
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

    public void onResume() {
        super.onResume();


        ClsManoObra.readMO(getContext(),id_empresa, new ClsManoObra.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsManoObra> manoObraList) {


                ManoObraAdapter manoObraAdapter =new ManoObraAdapter(getContext(),manoObraList,id_empresa);
                mostrar_mano_obra.setAdapter(manoObraAdapter);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ManoObraFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostrar_mano_obra = view.findViewById(R.id.lista_mano_obra);
        mostrar_mano_obra.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mano_obra, container, false);
    }
}