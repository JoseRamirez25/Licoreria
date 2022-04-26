package com.example.licoreria_inventario;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorListaProductos extends RecyclerView.Adapter<AdaptadorListaProductos.HolderList> implements View.OnClickListener{

    ArrayList<ListProductos> Lista_datos;
    private View.OnClickListener listener;

    public AdaptadorListaProductos(ArrayList<ListProductos> lista_datos) {
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
        holder.PName.setText(Lista_datos.get(position).getNombre());
        holder.PCant.setText(String.valueOf(Lista_datos.get(position).getCant()));
        holder.PPrice.setText(String.valueOf(Lista_datos.get(position).getPrecio()));
        if (Lista_datos.get(position).getEstado().equals("Inactivo")){
            holder.productosCV.setBackgroundResource(R.color.gris);
        }
        else{
            holder.productosCV.setBackgroundResource(R.color.white);
        }
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
        CardView productosCV;
        public HolderList(@NonNull View itemView) {
            super(itemView);
            PName = itemView.findViewById(R.id.producto);
            PCant = itemView.findViewById(R.id.cantidad);
            PPrice = itemView.findViewById(R.id.precio);
            productosCV = itemView.findViewById(R.id.cardViewProductos);
        }
    }
}
