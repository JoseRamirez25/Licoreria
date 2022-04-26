package com.example.licoreria_inventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorListaVentasDiarias extends RecyclerView.Adapter<AdaptadorListaVentasDiarias.HolderList> implements View.OnClickListener{

    ArrayList<ListVentas> Lista_datos;
    View.OnClickListener listener;

    public AdaptadorListaVentasDiarias(ArrayList<ListVentas> lista_datos) {
        Lista_datos = lista_datos;
    }

    @NonNull
    @Override
    public HolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_productos, null, false);
        view.setOnClickListener(this);
        return new HolderList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderList holder, int position) {
        holder.PName.setText(Lista_datos.get(position).getProductoV());
        holder.PCant.setText(String.valueOf(Lista_datos.get(position).getCantV()));
        holder.PPrice.setText(String.valueOf(Lista_datos.get(position).getTotalV()));
    }

    @Override
    public int getItemCount() {
        return Lista_datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class HolderList extends RecyclerView.ViewHolder {
        TextView PName, PCant, PPrice;
        public HolderList(@NonNull View itemView) {
            super(itemView);
            PName = itemView.findViewById(R.id.producto);
            PCant = itemView.findViewById(R.id.cantidad);
            PPrice = itemView.findViewById(R.id.precio);
        }
    }
}
