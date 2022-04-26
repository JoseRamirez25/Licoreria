package com.example.licoreria_inventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class vender_productos extends AppCompatActivity {
    ArrayList<ListProductos> listaInventario;
    RecyclerView recyclervender;
    AdaptadorListaProductos adapterI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_productos);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listaInventario = new ArrayList<>();
        recyclervender = findViewById(R.id.recyclerVender);
        recyclervender.setLayoutManager(new LinearLayoutManager(this));
        adapterI = new AdaptadorListaProductos(listaInventario);
        llenarInventario("");

//        clcklisteners
        adapterI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cantidadVenta, total;
                String producto, proveedor;
                Button btnVender;
                int cantidad, precio, tamaño, alcoholGrados, id;
                id = listaInventario.get(recyclervender.getChildAdapterPosition(v)).getId();
                producto =listaInventario.get(recyclervender.getChildAdapterPosition(v)).getNombre();
                cantidad =listaInventario.get(recyclervender.getChildAdapterPosition(v)).getCant();
                precio =listaInventario.get(recyclervender.getChildAdapterPosition(v)).getPrecio();
                tamaño =listaInventario.get(recyclervender.getChildAdapterPosition(v)).getTamaño();
                alcoholGrados =listaInventario.get(recyclervender.getChildAdapterPosition(v)).getGradosAlcohol();
                proveedor = listaInventario.get(recyclervender.getChildAdapterPosition(v)).getProveedorName();

//                creacion del dialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(vender_productos.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialogo_vender,null);
                cantidadVenta = view.findViewById(R.id.cantidadAVender);
                total = view.findViewById(R.id.total);
                btnVender = view.findViewById(R.id.btnVender);
                cantidadVenta.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)){
                            int cantV = Integer.parseInt(cantidadVenta.getText().toString()), totalV;
                            if (cantV<=cantidad){
                                totalV = cantV * precio;
                                total.setText(Integer.valueOf(totalV).toString());
                                btnVender.setEnabled(true);
                                if ((cantidad-Integer.parseInt(cantidadVenta.getText().toString()))<10){
                                    mostrarDialog(Integer.parseInt(cantidadVenta.getText().toString()), cantidad, proveedor);
                                }
                            }else{
                                cantidadVenta.setText("");
                                Toast.makeText(vender_productos.this, "Esta excediendo la cantidad del producto", Toast.LENGTH_SHORT).show();
                                total.setText("");
                            }
                        }
                        return false;
                    }
                });
                builder.setView(view);
                final AlertDialog dialogVender = builder.create();
                dialogVender.show();
                dialogVender.setCancelable(false);

//              Asignacion de valores a dialogo
                EditText ProductN, ProductC, ProductPrecio, ProductT, ProductA;
                Button btnCancelar;
                btnCancelar = view.findViewById(R.id.btnCancelar);
                ProductN = view.findViewById(R.id.editTextProductName);
                ProductC = view.findViewById(R.id.editTextNumberCantidad);
                ProductPrecio = view.findViewById(R.id.editTextNumberPrecio);
                ProductT = view.findViewById(R.id.editTextNumberSize);
                ProductA = view.findViewById(R.id.editTextNumberAlcoholGrados);

                btnVender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cantidadVenta.getText().toString().isEmpty()){
                            Toast.makeText(vender_productos.this, "Debe agregar una cantidad a vender", Toast.LENGTH_SHORT).show();
                        }else {
                            vender(id, Integer.parseInt(cantidadVenta.getText().toString()), precio);
                            dialogVender.dismiss();
                        }
                    }
                });
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogVender.dismiss();
                    }
                });

                ProductN.setText(producto);
                ProductC.setText(Integer.valueOf(cantidad).toString());
                ProductPrecio.setText(Integer.valueOf(precio).toString());
                ProductT.setText(Integer.valueOf(tamaño).toString());
                ProductA.setText(Integer.valueOf(alcoholGrados).toString());
            }
        });

    }
    //   llenado de recycleViews
    public void llenarInventario(String search){
        String URL = "http://localhost/LicoreriaDB/obtener_inventario_vender.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("fallo")){
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
                else{
                    listaInventario.clear();
                    String nombreP, descripcionP, estadoP, proveedorP, proveedorN;
                    int cantP, idP, precioP, tamañoP, gradosAlcoholP;
                    try {
                        JSONArray lista = new JSONArray(response);

                        for (int i = 0; i < lista.length(); i++){
                            JSONObject productos = lista.getJSONObject(i);
                            idP = productos.getInt("id");
                            nombreP = productos.getString("producto");
                            descripcionP = productos.getString("descripcion");
                            estadoP = productos.getString("estado");
                            proveedorP = productos.getString("proveedor");
                            proveedorN = productos.getString("proveedorName");
                            cantP = productos.getInt("cantidad");
                            precioP = productos.getInt("precio");
                            tamañoP = productos.getInt("size");
                            gradosAlcoholP = productos.getInt("gradosAlcohol");
                            listaInventario.add(new ListProductos(nombreP, descripcionP, estadoP, proveedorP, proveedorN, idP, cantP, precioP, tamañoP, gradosAlcoholP));
                            adapterI.notifyDataSetChanged();
                        }
                        recyclervender.setAdapter(adapterI);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(vender_productos.this, "algo fallo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("search", search);
                return parametros;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);

    }
    public void vender(int id, int cantidad, int precio){
        String URL = "http://localhost/LicoreriaDB/vender.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(vender_productos.this, "Vendido", Toast.LENGTH_SHORT).show();
                    llenarInventario("");
                }
                else{
                    Toast.makeText(vender_productos.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(vender_productos.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Integer.valueOf(id).toString());
                parametros.put("cantidad", Integer.valueOf(cantidad).toString());
                parametros.put("precio", Integer.valueOf(precio).toString());
                return parametros;
            }
        };
        RequestQueue request = Volley.newRequestQueue(vender_productos.this);
        request.add(rq);
    }
    public void mostrarDialog(int newCant, int OldCant, String proveedor){
        String mensaje = "Tan solo quedaran "+(OldCant-newCant)+" de este producto," +
                " por favor contacte con el proveedor "+proveedor;
        AlertDialog.Builder builder = new AlertDialog.Builder(vender_productos.this);
        builder.setTitle("ADVERTENCIA!");
        builder.setMessage(mensaje).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act = new Intent(vender_productos.this, Inventario.class);
        startActivity(act);
    }
}