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

import adaptadores.InventarioAdapter;


public class VistaInventarioFragment extends Fragment {

    private RecyclerView lista_inventario;
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


        ClsInventario.readIN(getContext(), id_empresa,new ClsInventario.VolleyCallback() {
            @Override
            public void onSuccess(List<ClsInventario> inventarioList) {
                if (inventarioList.isEmpty()){
                    Toast.makeText(VistaInventarioFragment.this.requireContext(), "No hay productos ", Toast.LENGTH_SHORT).show();
                }else{
                    InventarioAdapter inventarioAdapter =new InventarioAdapter(getContext(),inventarioList,id_empresa);
                    lista_inventario.setAdapter(inventarioAdapter);

                }




            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(VistaInventarioFragment.this.requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lista_inventario = view.findViewById(R.id.lista_inventario);
        lista_inventario.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vista_inventario, container, false);
    }
}