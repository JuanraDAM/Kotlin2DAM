package com.example.srodenas.simulacioncrud.Logic;

public class ClaseA {
    private int atributo1;
    private String atributo2;
    private boolean atributo3;

    // Constructor vacío
    public ClaseA() {}

    // Constructor con parámetros
    public ClaseA(int atributo1, String atributo2, boolean atributo3) {
        this.atributo1 = atributo1;
        this.atributo2 = atributo2;
        this.atributo3 = atributo3;
    }

    // Getters
    public int getAtributo1() {
        return atributo1;
    }

    public String getAtributo2() {
        return atributo2;
    }

    public boolean getAtributo3() {
        return atributo3;
    }

    // Setters
    public void setAtributo1(int atributo1) {
        this.atributo1 = atributo1;
    }

    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    public void setAtributo3(boolean atributo3) {
        this.atributo3 = atributo3;
    }

    // Método toString
    @Override
    public String toString() {
        return "ClaseA{" +
                "atributo1=" + atributo1 +
                ", atributo2='" + atributo2 + '\'' +
                ", atributo3=" + atributo3 +
                '}';
    }
}
