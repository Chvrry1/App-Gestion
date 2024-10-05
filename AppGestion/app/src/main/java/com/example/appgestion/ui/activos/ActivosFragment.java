package com.example.appgestion.ui.activos;

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

import adaptadores.AdaptadorTabsActivos;


public class ActivosFragment extends Fragment {


    private TabLayout tabMenu;
    private ViewPager2 tabVista;
    private AdaptadorTabsActivos adaptadorTabsActivos;
    private FloatingActionButton botonAgregar;
    private String num = "200";
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
        tabMenu = view.findViewById(R.id.tab_menu);
        tabVista = view.findViewById(R.id.tab_vista);
        botonAgregar = view.findViewById(R.id.btnAgregar);
        adaptadorTabsActivos= new AdaptadorTabsActivos(getActivity().getSupportFragmentManager());
        tabVista.setAdapter(adaptadorTabsActivos);
        // new TabLayoutMediator(tabMenu,tabVista, (tab, position) -> tab.setText("Item " + (position + 1))).attach();





        tabMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabVista.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabVista.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabMenu.getTabAt(position).select();
                botonAgregar.setOnClickListener(view1 -> {
                    if (position == 0){
                        //Snackbar.make(view1, "si funciona activos fijos",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        Intent formActivosF=new Intent(getContext(), AgregarActivosFijos.class);
                        formActivosF.putExtra("agregar_activosF", true);
                        formActivosF.putExtra("id_empresa",id_empresa);
                        startActivity(formActivosF);



                    }else {
                        //Snackbar.make(view1, "si funciona activos diferidos",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        Intent formActivosD=new Intent(getContext(), AgregarActivosDiferidos.class);
                        formActivosD.putExtra("agregar_activosD", true);
                        formActivosD.putExtra("id_empresa",id_empresa);
                        startActivity(formActivosD);
                    }


                });

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activos, container, false);

        // Inflate the layout for this fragment

    }
}