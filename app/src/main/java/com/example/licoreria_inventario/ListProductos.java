package com.example.licoreria_inventario;

public class ListProductos {

    private String nombre, descripcion, estado, proveedor, proveedorName;
    private int id, cant, precio, tamaño, gradosAlcohol;

    public ListProductos(){

    }

    public ListProductos(String nombre, String descripcion, String estado, String proveedor, String proveedorName, int id, int cant, int precio, int tamaño, int gradosAlcohol) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.proveedor = proveedor;
        this.proveedorName = proveedorName;
        this.cant = cant;
        this.precio = precio;
        this.tamaño = tamaño;
        this.gradosAlcohol = gradosAlcohol;
    }

    public String getProveedorName() {
        return proveedorName;
    }

    public void setProveedorName(String proveedorName) {
        this.proveedorName = proveedorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public int getGradosAlcohol() {
        return gradosAlcohol;
    }

    public void setGradosAlcohol(int gradosAlcohol) {
        this.gradosAlcohol = gradosAlcohol;
    }
}
