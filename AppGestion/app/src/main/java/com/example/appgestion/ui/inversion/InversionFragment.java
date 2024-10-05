package com.example.appgestion.ui.inversion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.AgregarActivosDiferidos;
import com.example.appgestion.ui.activos.AgregarActivosFijos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import adaptadores.AdaptadorTabsActivos;
import adaptadores.AdaptadorTabsInventario;
import adaptadores.AdaptadorTabsInversion;


public class InversionFragment extends Fragment {

    private TabLayout tab_menu;
    private AdaptadorTabsInversion adaptadorTabsInversion;
    private ViewPager2 vista_tabs;

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("se ejecuto onResume en tab");


    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("se ejecuto onStart en tab");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("se ejecuto onPause en tab");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("se ejecuto onInstance en tab");

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tab_menu = view.findViewById(R.id.tab_menu);
        vista_tabs = view.findViewById(R.id.tab_vista);
        adaptadorTabsInversion= new AdaptadorTabsInversion(getActivity().getSupportFragmentManager());
        vista_tabs.setAdapter(adaptadorTabsInversion);


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

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inversion, container, false);
    }
}