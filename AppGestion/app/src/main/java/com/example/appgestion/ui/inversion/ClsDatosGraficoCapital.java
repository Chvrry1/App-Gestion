package com.example.appgestion.ui.inversion;

public class ClsDatosGraficoCapital {
    private String etiqueta;
    private double valor;

    public ClsDatosGraficoCapital(String etiqueta, double valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
