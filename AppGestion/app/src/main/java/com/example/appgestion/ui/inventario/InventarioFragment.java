package com.example.appgestion.ui.inventario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appgestion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import adaptadores.AdaptadorTabsInventario;


public class InventarioFragment extends Fragment {


    private TabLayout tab_menu;
    private FloatingActionButton btn_agregar;
    private AdaptadorTabsInventario adaptadorTabsInventario;
    private ViewPager2 vista_tabs;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tab_menu = view.findViewById(R.id.tab_menu);
        btn_agregar=view.findViewById(R.id.btnAgregar);
        vista_tabs = view.findViewById(R.id.tab_vista);
        adaptadorTabsInventario= new AdaptadorTabsInventario(getActivity().getSupportFragmentManager());
        vista_tabs.setAdapter(adaptadorTabsInventario);

        tab_menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                vista_tabs.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vista_tabs.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tab_menu.getTabAt(position).select();
                if (position == 2){
                    btn_agregar.setVisibility(view.INVISIBLE);

                }else {
                    btn_agregar.setVisibility(view.VISIBLE);
                }
                btn_agregar.setOnClickListener(view1 -> {
                    switch (position){
                        case 0:
                            //Snackbar.make(view1, "si funciona inventario", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                            Intent formProductos=new Intent(getContext(), AgregarProductos.class);
                            formProductos.putExtra("id_empresa",id_empresa);
                            startActivity(formProductos);
                            break;
                        case 1:
                            //Snackbar.make(view1, "si funciona mano de obra",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                            Intent formManoO=new Intent(getContext(), AgregarManoObra.class);
                            formManoO.putExtra("id_empresa",id_empresa);
                            formManoO.putExtra("agregar_MO", true);
                            startActivity(formManoO);
                            break;
                    }

                });



            }



        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventario, container, false);
    }
}