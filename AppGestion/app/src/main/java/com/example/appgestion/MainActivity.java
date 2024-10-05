package com.example.appgestion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appgestion.ui.inventario.AgregarProductos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appgestion.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import adaptadores.AdaptadorTabsActivos;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_gastos, R.id.navigation_activos, R.id.navigation_inventario, R.id.navigation_inversion)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_inversion && menuItem.isChecked()){
                    menuItem.setChecked(false);
                }
                navController.navigate(menuItem.getItemId());
                return true;
            }
        });




        SharedPreferences sharedPreferences = getSharedPreferences("materia_prima_temporal", MODE_PRIVATE);
        SharedPreferences.Editor materia_prima_datos = sharedPreferences.edit();
        materia_prima_datos.clear();
        materia_prima_datos.apply();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.usuario){
            System.out.println("funciona usuario");

        }
        if (item.getItemId()==R.id.encuesta){
            System.out.println("funciona encuesta");
            Intent encuesta=new Intent(this, EncuestaActivity.class);
            startActivity(encuesta);

        }
        return super.onOptionsItemSelected(item);

    }
}