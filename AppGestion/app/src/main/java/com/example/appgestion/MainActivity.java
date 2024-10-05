package com.example.appgestion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appgestion.ui.data.ExcelDataLoader;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.appgestion.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Toolbar toolbar;
    ActivityResultLauncher<String> fileChooserLauncher;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private MenuItem importExcelMenuItem;
    private ExcelDataLoader excelDataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int empresaId = intent.getIntExtra("empresa_id", -1);

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



        if (empresaId != -1) {
            excelDataLoader = new ExcelDataLoader(this, executor, handler, empresaId);
            fileChooserLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            excelDataLoader.readExcelFile(uri, importExcelMenuItem);
                        }
                    }
            );
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu,menu);
        importExcelMenuItem = menu.findItem(R.id.importar_excel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.usuario) {
            System.out.println("funciona usuario");
        } else if (item.getItemId() == R.id.encuesta) {
            System.out.println("funciona encuesta");
            Intent encuesta = new Intent(this, EncuestaActivity.class);
            startActivity(encuesta);
        } else if (item.getItemId() == R.id.importar_excel) {
            openFileChooser();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void openFileChooser() {
        try {
            fileChooserLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } catch (Exception ex) {
            Toast.makeText(this, "Por favor instala un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}