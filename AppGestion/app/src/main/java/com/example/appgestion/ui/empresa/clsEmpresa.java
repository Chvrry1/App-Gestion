package com.example.appgestion.ui.empresa;

import java.util.ArrayList;
import java.util.List;

public class clsEmpresa {

    private int id;
    private String nombre;
    private double capital;

    private static List<clsEmpresa> listaEmpresas = new ArrayList<>();


    public clsEmpresa(int id, String nombre, double capital) {
        this.id = id;
        this.nombre = nombre;
        this.capital = capital;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCapital() {
        return capital;
    }
    public static void agregarEmpresa(clsEmpresa empresa) {
        listaEmpresas.add(empresa);
    }

    // Método para obtener la lista de todas las empresas
    public static List<clsEmpresa> obtenerListaEmpresas() {
        return listaEmpresas;
    }


}
