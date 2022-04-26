package com.example.licoreria_inventario;

public class ListVentas {

    private String productoV, fechaV;
    private int idV, cantV, totalV;

    public ListVentas(){

    }

    public ListVentas(String productoV, String fechaV, int idV, int cantV, int totalV) {
        this.fechaV = fechaV;
        this.productoV = productoV;
        this.idV = idV;
        this.cantV = cantV;
        this.totalV = totalV;
    }

    public String getFechaV() {
        return fechaV;
    }

    public void setFechaV(String fechaV) {
        this.fechaV = fechaV;
    }

    public int getIdV() {
        return idV;
    }

    public void setIdV(int idV) {
        this.idV = idV;
    }

    public String getProductoV() {
        return productoV;
    }

    public void setProductoV(String productoV) {
        this.productoV = productoV;
    }

    public int getCantV() {
        return cantV;
    }

    public void setCantV(int cantV) {
        this.cantV = cantV;
    }

    public int getTotalV() {
        return totalV;
    }

    public void setTotalV(int totalV) {
        this.totalV = totalV;
    }
}
