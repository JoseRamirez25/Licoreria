package com.example.licoreria_inventario;

import com.android.volley.toolbox.StringRequest;

public class ListProveedores {

    private String cc, nombre, telefono;

    public ListProveedores(){

    }

    public ListProveedores(String cc, String nombre, String telefono) {
        this.cc = cc;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
