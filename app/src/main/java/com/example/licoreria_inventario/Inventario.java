package com.example.licoreria_inventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Inventario extends AppCompatActivity {
LinearLayout layoutI, layoutP;
Button addOp, cancelS, searchFechas;
Spinner optionList;
ArrayList<ListProductos> listaInventario;
ArrayList<ListProveedores> listaProveedores;
ArrayList<ListVentas> listaVentas, listaVentasDiarias;
RecyclerView recyclerInventario, recyclerProveedores;
AdaptadorListaProductos adapterI;
AdaptadorListaProveedores adapterP;
AdaptadorListaVentas adapterV;
AdaptadorListaVentasDiarias adapterVD;
ArrayList<String> provs = new ArrayList<>();
String addOption;
CardView filtro;
EditText search, fechaDesde, fechaHasta;
TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        total = findViewById(R.id.verTotal);
        searchFechas = findViewById(R.id.searchFechas);
        filtro = findViewById(R.id.filtroFecha);
        addOp = findViewById(R.id.addOp);
        fechaDesde = findViewById(R.id.fechaInputMin);
        fechaHasta = findViewById(R.id.fechaInputMax);
        cancelS = findViewById(R.id.cancelSearch);
        layoutI = findViewById(R.id.LayoutInventario);
        layoutP = findViewById(R.id.LayoutProveedores);
        optionList = findViewById(R.id.optionList);
        search = findViewById(R.id.editTextsearch);
        spinnerValues();
        listaInventario = new ArrayList<>();
        listaProveedores = new ArrayList<>();
        listaVentas = new ArrayList<>();
        listaVentasDiarias = new ArrayList<>();
        recyclerInventario = findViewById(R.id.recyclerID);
        recyclerProveedores = findViewById(R.id.recyclerProveedores);
//        layout managers
        recyclerInventario.setLayoutManager(new LinearLayoutManager(this));
        recyclerProveedores.setLayoutManager(new LinearLayoutManager(this));
//        llenar tablas
        llenarInventario("");
        llenarProveedores("");
        llenarVentas(2, "", "");
        llenarVentasDiarias();

//        adaptadores
        adapterI = new AdaptadorListaProductos(listaInventario);
        adapterP = new AdaptadorListaProveedores(listaProveedores);
        adapterV = new AdaptadorListaVentas(listaVentas);
        adapterVD = new AdaptadorListaVentasDiarias(listaVentasDiarias);
        ArrayAdapter EsAdapter = ArrayAdapter.createFromResource(Inventario.this, R.array.estado, android.R.layout.simple_spinner_item);

//        clcklisteners
        cancelS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addOption.equals("Inventario")){
                    llenarInventario("");
                }
                if (addOption.equals("Proveedores")){
                    llenarProveedores("");
                }
                search.setText("");
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)){
                    if (addOption.equals("Inventario")){
                        llenarInventario(search.getText().toString());
                        adapterI.notifyDataSetChanged();
                        recyclerInventario.setAdapter(adapterI);
                    }
                    if (addOption.equals("Proveedores")){
                        llenarProveedores(search.getText().toString());
                        adapterP.notifyDataSetChanged();
                        recyclerProveedores.setAdapter(adapterP);
                    }
                }
                return false;
            }
        });
        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioMensual(1);
            }
        });
        fechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendarioMensual(2);
            }
        });
        searchFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarVentas(2, fechaDesde.getText().toString(), fechaHasta.getText().toString());
                obtenerGanancias(2,fechaDesde.getText().toString(), fechaHasta.getText().toString());
                fechaDesde.setText("");
                fechaHasta.setText("");
            }
        });
//        Funciones del boton flotante
        addOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addOption.equals("Inventario")){
                    Intent ActR = new Intent(Inventario.this, RegistrarProducto.class);
                    startActivity(ActR);
                    finish();
                }
                if (addOption.equals("Proveedores")){
                    Intent ActR = new Intent(Inventario.this, AgregarProveedores.class);
                    startActivity(ActR);
                    finish();
                }
                if (addOption.equals("Ventas Diarias") || addOption.equals("Ventas Completas")){
                    Intent act = new Intent(getApplicationContext(), vender_productos.class);
                    startActivity(act);
                    finish();
                }
            }
        });
//        Funciones del spiner
        optionList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Inventario")){
                    layoutI.setVisibility(View.VISIBLE);
                    layoutP.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    total.setVisibility(View.GONE);
                    cancelS.setVisibility(View.VISIBLE);
                    addOp.setText("+");
                    recyclerInventario.setAdapter(adapterI);
                    filtro.setVisibility(View.GONE);
                    addOption = parent.getItemAtPosition(position).toString();
                }
                if (parent.getItemAtPosition(position).toString().equals("Proveedores")){
                    layoutI.setVisibility(View.GONE);
                    layoutP.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    total.setVisibility(View.GONE);
                    cancelS.setVisibility(View.VISIBLE);
                    addOp.setText("+");
                    adapterP.notifyDataSetChanged();
                    filtro.setVisibility(View.GONE);
                    addOption = parent.getItemAtPosition(position).toString();
                }
                if (parent.getItemAtPosition(position).equals("Ventas Diarias")){
                    layoutI.setVisibility(View.VISIBLE);
                    layoutP.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    total.setVisibility(View.VISIBLE);
                    cancelS.setVisibility(View.GONE);
                    addOp.setText("$");
                    adapterVD.notifyDataSetChanged();
                    recyclerInventario.setAdapter(adapterVD);
                    filtro.setVisibility(View.GONE);
                    addOption = parent.getItemAtPosition(position).toString();
                    obtenerGanancias(1,"","");
                }
                if (parent.getItemAtPosition(position).equals("Ventas Completas")){
                    layoutI.setVisibility(View.VISIBLE);
                    layoutP.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    total.setVisibility(View.VISIBLE);
                    cancelS.setVisibility(View.GONE);
                    filtro.setVisibility(View.VISIBLE);
                    addOp.setText("$");
                    adapterV.notifyDataSetChanged();
                    recyclerInventario.setAdapter(adapterV);
                    addOption = parent.getItemAtPosition(position).toString();
                    llenarVentas(2,"","");
                    obtenerGanancias(2,"","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        Funciones adaptadores
        adapterI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String producto, descripcion, estado, proveedor, proveedorN;
                int cantidad, precio, tamaño, alcoholGrados, id;
                id = listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getId();
                producto =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getNombre();
                descripcion =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getDescripcion();
                estado =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getEstado();
                proveedor =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getProveedor();
                proveedorN =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getProveedorName();
                cantidad =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getCant();
                precio =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getPrecio();
                tamaño =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getTamaño();
                alcoholGrados =listaInventario.get(recyclerInventario.getChildAdapterPosition(v)).getGradosAlcohol();

//                creacion del dialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(Inventario.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialogo_inventario,null);
                builder.setView(view);
                final AlertDialog dialogInventario = builder.create();
                dialogInventario.show();
                dialogInventario.setCancelable(false);

//              Asignacion de valores a dialogo
                EditText ProductN, ProductD, ProductC, ProductPrecio, ProductT, ProductA;
                Spinner  ProductE, ProductP;
                Button btnMod, btnDel, btnC, btnConfirmI;
                btnMod = view.findViewById(R.id.btnMod);
                btnDel = view.findViewById(R.id.btnDel);
                btnC = view.findViewById(R.id.btnClose);
                btnConfirmI = view.findViewById(R.id.btnConfirmI);
                ProductN = view.findViewById(R.id.editTextProductName);
                ProductD = view.findViewById(R.id.editTextTextMultiLineProductDescription);
                ProductE = view.findViewById(R.id.spinnerEstado);
                ProductP = view.findViewById(R.id.spinnerProveedor);
                ProductC = view.findViewById(R.id.editTextNumberCantidad);
                ProductPrecio = view.findViewById(R.id.editTextNumberPrecio);
                ProductT = view.findViewById(R.id.editTextNumberSize);
                ProductA = view.findViewById(R.id.editTextNumberAlcoholGrados);
                btnConfirmI.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String found="";
                        for (int i=0; i<listaInventario.size(); i++){
                            if(listaInventario.get(i).getProveedorName().equals(ProductP.getSelectedItem().toString())){
                                found = listaInventario.get(i).getProveedor();
                            }
                        }
                        modificarProductos(id, ProductN.getText().toString(), ProductD.getText().toString(), found, Integer.parseInt(ProductC.getText().toString()),Integer.parseInt(ProductPrecio.getText().toString()), Integer.parseInt(ProductT.getText().toString()), Integer.parseInt(ProductA.getText().toString()));
                        btnConfirmI.setVisibility(View.GONE);
                        ProductN.setEnabled(false);
                        ProductD.setEnabled(false);
                        ProductP.setEnabled(false);
                        ProductC.setEnabled(false);
                        ProductPrecio.setEnabled(false);
                        ProductT.setEnabled(false);
                        ProductA.setEnabled(false);
                        dialogInventario.dismiss();
                    }
                });
                btnMod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductN.setEnabled(true);
                        ProductD.setEnabled(true);
                        ProductP.setEnabled(true);
                        ProductC.setEnabled(true);
                        ProductPrecio.setEnabled(true);
                        ProductT.setEnabled(true);
                        ProductA.setEnabled(true);
                        btnConfirmI.setVisibility(View.VISIBLE);
                    }
                });
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cambiarEstadoProductos(id, estado, cantidad);
                        dialogInventario.dismiss();
                    }
                });
                btnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInventario.dismiss();
                    }
                });


//                llenado spinner de dialogo_inventario.xml
                ProductE.setAdapter(EsAdapter);
                provs.clear();
                for (int i=0; i<listaProveedores.size();i++){
                    provs.add(listaProveedores.get(i).getNombre());
                }
                ArrayAdapter PrAdapter = new ArrayAdapter(Inventario.this,  android.R.layout.simple_spinner_item, provs);
                ProductP.setAdapter(PrAdapter);
                ProductE.setEnabled(false);
                ProductP.setEnabled(false);

                int estadoInt, proveedorInt;
                if (estado.equals("Activo")){
                    estadoInt = 0;
                    btnDel.setText("Desactivar");
                }else{
                    estadoInt = 1;
                    btnDel.setText("Activar");
                }
                proveedorInt = PrAdapter.getPosition(proveedorN);

                ProductN.setText(producto);
                ProductD.setText(descripcion);
                ProductE.setSelection(estadoInt);
                ProductP.setSelection(proveedorInt);
                ProductC.setText(Integer.valueOf(cantidad).toString());
                ProductPrecio.setText(Integer.valueOf(precio).toString());
                ProductT.setText(Integer.valueOf(tamaño).toString());
                ProductA.setText(Integer.valueOf(alcoholGrados).toString());
            }
        });
        adapterP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre, telefono, id;
                id = listaProveedores.get(recyclerProveedores.getChildAdapterPosition(v)).getCc();
                nombre =listaProveedores.get(recyclerProveedores.getChildAdapterPosition(v)).getNombre();
                telefono =listaProveedores.get(recyclerProveedores.getChildAdapterPosition(v)).getTelefono();
//                creacion del dialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(Inventario.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewProveedores = inflater.inflate(R.layout.dialogo_proveedores,null);
                builder.setView(viewProveedores);
                final AlertDialog dialogProveedores = builder.create();
                dialogProveedores.show();
                dialogProveedores.setCancelable(false);

//                asignacion
                EditText Enombre, Ecedula, Etelefono;
                Button btnmod, btndel, btnc, btnconfirmP;
                Enombre = viewProveedores.findViewById(R.id.editTextNombre);
                Ecedula = viewProveedores.findViewById(R.id.editTextCedula);
                Etelefono = viewProveedores.findViewById(R.id.editTextTelefono);
                btnmod = viewProveedores.findViewById(R.id.btnModP);
                btndel = viewProveedores.findViewById(R.id.btnDelP);
                btnc = viewProveedores.findViewById(R.id.btnCloseP);
                btnconfirmP = viewProveedores.findViewById(R.id.btnConfirmP);
//                Click listeners
                btnconfirmP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modificarProveedores(Enombre.getText().toString(), Ecedula.getText().toString(), Etelefono.getText().toString());
                        dialogProveedores.dismiss();
                        btnconfirmP.setVisibility(View.GONE);
                        Enombre.setEnabled(false);
                        Etelefono.setEnabled(false);
                    }
                });
                btnmod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Enombre.setEnabled(true);
                        Etelefono.setEnabled(true);
                        btnconfirmP.setVisibility(View.VISIBLE);
                    }
                });
                btndel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarProveedor(id);
                        dialogProveedores.dismiss();
                    }
                });
                btnc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogProveedores.dismiss();
                    }
                });

                Enombre.setText(nombre);
                Ecedula.setText(id);
                Etelefono.setText(telefono);

            }
        });
        adapterV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productoV, fechaV;
                int idV, cantV, precioV, totalV;
                idV = listaVentas.get(recyclerInventario.getChildAdapterPosition(v)).getIdV();
                productoV = listaVentas.get(recyclerProveedores.getChildAdapterPosition(v)).getProductoV();
                fechaV =listaVentas.get(recyclerProveedores.getChildAdapterPosition(v)).getFechaV();
                cantV =listaVentas.get(recyclerProveedores.getChildAdapterPosition(v)).getCantV();
                totalV = listaVentas.get(recyclerInventario.getChildAdapterPosition(v)).getTotalV();
                precioV = totalV/cantV;
//                creacion del dialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(Inventario.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewVentas = inflater.inflate(R.layout.dialogo_ventas,null);
                builder.setView(viewVentas);
                final AlertDialog dialogVentas = builder.create();
                dialogVentas.show();
                dialogVentas.setCancelable(false);

//                asignacion
                EditText EproductoV, EfechaV, EcantV, EtotalV, EprecioV;
                Button btndel, btnc;
                EproductoV = viewVentas.findViewById(R.id.editTextProductoV);
                EfechaV= viewVentas.findViewById(R.id.editTextFechaV);
                EcantV = viewVentas.findViewById(R.id.editTextNumberCantidadV);
                EtotalV = viewVentas.findViewById(R.id.editTextTotalV);
                EprecioV = viewVentas.findViewById(R.id.editTextNumberPrecioV);
                btndel = viewVentas.findViewById(R.id.btnDelV);
                btnc = viewVentas.findViewById(R.id.btnCloseV);

//                Click listeners
                btndel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogVentas.dismiss();
                        eliminarVenta(idV);
                        fechaHasta.setText("");
                        fechaDesde.setText("");
                    }
                });
                btnc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogVentas.dismiss();
                    }
                });

                EproductoV.setText(productoV);
                EfechaV.setText(fechaV);
                EcantV.setText(Integer.valueOf(cantV).toString());
                EtotalV.setText(Integer.valueOf(totalV).toString());
                EprecioV.setText(Integer.valueOf(precioV).toString());

            }
        });
        adapterVD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productoV, fechaV;
                int idV, cantV, precioV, totalV;
                idV = listaVentasDiarias.get(recyclerInventario.getChildAdapterPosition(v)).getIdV();
                productoV = listaVentasDiarias.get(recyclerProveedores.getChildAdapterPosition(v)).getProductoV();
                fechaV =listaVentasDiarias.get(recyclerProveedores.getChildAdapterPosition(v)).getFechaV();
                cantV =listaVentasDiarias.get(recyclerProveedores.getChildAdapterPosition(v)).getCantV();
                totalV = listaVentasDiarias.get(recyclerInventario.getChildAdapterPosition(v)).getTotalV();
                precioV = totalV/cantV;
//                creacion del dialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(Inventario.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewVentasDiarias = inflater.inflate(R.layout.dialogo_ventas,null);
                builder.setView(viewVentasDiarias);
                final AlertDialog dialogVentasDiarias = builder.create();
                dialogVentasDiarias.show();
                dialogVentasDiarias.setCancelable(false);

//                asignacion
                EditText EproductoV, EfechaV, EcantV, EtotalV, EprecioV;
                Button btndel, btnc, btnConfirmV;
                EproductoV = viewVentasDiarias.findViewById(R.id.editTextProductoV);
                EfechaV= viewVentasDiarias.findViewById(R.id.editTextFechaV);
                EcantV = viewVentasDiarias.findViewById(R.id.editTextNumberCantidadV);
                EtotalV = viewVentasDiarias.findViewById(R.id.editTextTotalV);
                EprecioV = viewVentasDiarias.findViewById(R.id.editTextNumberPrecioV);
                btndel = viewVentasDiarias.findViewById(R.id.btnDelV);
                btnc = viewVentasDiarias.findViewById(R.id.btnCloseV);
//                Click listeners
                btndel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogVentasDiarias.dismiss();
                        eliminarVenta(idV);
                    }
                });
                btnc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogVentasDiarias.dismiss();
                    }
                });

                EproductoV.setText(productoV);
                EfechaV.setText(fechaV);
                EcantV.setText(Integer.valueOf(cantV).toString());
                EtotalV.setText(Integer.valueOf(totalV).toString());
                EprecioV.setText(Integer.valueOf(precioV).toString());

            }
        });

    }
//   llenado de recycleViews
    public void llenarInventario(String busqueda){
        String URL = "http://localhost/LicoreriaDB/obtener_inventario.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Inventario.this, "algo fallo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametro = new HashMap<>();
                parametro.put("search", busqueda);
                return parametro;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);

    }
    public void llenarProveedores(String busqueda){
        String URL = "http://localhost/LicoreriaDB/obtener_proveedores.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaProveedores.clear();
                String idP, nombreP, telefonoP;
                try {
                    JSONArray lista = new JSONArray(response);

                    for (int i = 0; i < lista.length(); i++){
                        JSONObject productos = lista.getJSONObject(i);
                        idP = productos.getString("PrID");
                        nombreP = productos.getString("PrNombre");
                        telefonoP = productos.getString("PrTelefono");
                        listaProveedores.add(new ListProveedores(idP, nombreP, telefonoP));
                        adapterI.notifyDataSetChanged();
                    }
                    recyclerProveedores.setAdapter(adapterP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Inventario.this, "algo fallo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametro = new HashMap<>();
                parametro.put("search", busqueda);
                return parametro;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);

    }
    public void llenarVentas(int op, String limiteIn, String limiteS){
        String URL = "http://localhost/LicoreriaDB/obtener_ventas.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("fallo")){
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (op==1){
                        listaVentasDiarias.clear();
                    }else{
                        listaVentas.clear();
                    }
                    String nombreV, fechaV;
                    int cantV,totalV,idV;
                    try {
                        JSONArray lista = new JSONArray(response);

                        for (int i = 0; i < lista.length(); i++){
                            JSONObject productos = lista.getJSONObject(i);
                            idV = productos.getInt("idV");
                            fechaV = productos.getString("fechaV");
                            nombreV = productos.getString("ProductoV");
                            cantV = productos.getInt("CantidadV");
                            totalV = productos.getInt("Total");
                            if(op == 2){
                                listaVentas.add(new ListVentas(nombreV, fechaV, idV, cantV, totalV));
                                adapterV.notifyDataSetChanged();
                            }
                            if(op == 1){
                                listaVentasDiarias.add(new ListVentas(nombreV, fechaV, idV, cantV, totalV));
                                adapterVD.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Inventario.this, "algo fallo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("op", Integer.valueOf(op).toString());
                p.put("limiteIn", limiteIn);
                p.put("limiteS", limiteS);
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);
    }
    public void llenarVentasDiarias(){
        llenarVentas(1, "", "");
    }
    public void obtenerGanancias(int op, String limiteIn, String limiteS){
        String URL = "http://localhost/LicoreriaDB/obtener_ganancias.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("fallo")){
                    if (response.equals("")){
                        total.setText("No hubieron ventas");
                        recyclerInventario.setVisibility(View.GONE);
                    }else{
                        total.setText("Total: "+response);
                        recyclerInventario.setVisibility(View.VISIBLE);

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("op", Integer.valueOf(op).toString());
                p.put("limiteIn", limiteIn);
                p.put("limiteS", limiteS);
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);
    }

//    consultas crud
    public void cambiarEstadoProductos(int id, String estado, int cantidad){
        String URL = "http://localhost/LicoreriaDB/desactivar_producto.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Activado")||response.equals("Desactivado")){
                    Toast.makeText(Inventario.this, response, Toast.LENGTH_SHORT).show();
                    llenarInventario("");
                }else{
                    Toast.makeText(Inventario.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Inventario.this, error.toString(), Toast.LENGTH_SHORT);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Integer.valueOf(id).toString());
                parametros.put("estado", estado);
                parametros.put("cantidad", Integer.valueOf(cantidad).toString());
                return parametros;
            }
        };
        RequestQueue request= Volley.newRequestQueue(this);
        request.add(rq);
    }
    public void eliminarProveedor(String Ecedula){
        String URL = "http://localhost/LicoreriaDB/eliminar_proveedor.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(Inventario.this, "Proveedor eliminado", Toast.LENGTH_SHORT).show();
                    llenarProveedores("");
                }else{
                    Toast.makeText(Inventario.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Inventario.this, error.toString(), Toast.LENGTH_SHORT);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", Ecedula);
                return parametros;
            }
        };
        RequestQueue request= Volley.newRequestQueue(this);
        request.add(rq);
    }
    public void eliminarVenta(int idV){
        String URL = "http://localhost/LicoreriaDB/eliminar_venta.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Venta eliminada", Toast.LENGTH_SHORT).show();
                    llenarVentas(2, "", "");
                    llenarVentasDiarias();
                    llenarInventario("");
                }else{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("id", Integer.valueOf(idV).toString());
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);
    }
    public void modificarProductos(int id, String producto, String descripcion, String proveedor, int cantidad, int precio, int tamaño, int alcoholGrados){
        String URL = "http://localhost/LicoreriaDB/modificar_producto.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Datos actualizados", Toast.LENGTH_LONG).show();
                    llenarInventario("");
                }
                else{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("id", Integer.valueOf(id).toString());
                p.put("producto", producto);
                p.put("descripcion", descripcion);
                p.put("proveedor", proveedor);
                p.put("cantidad", Integer.valueOf(cantidad).toString());
                p.put("precio", Integer.valueOf(precio).toString());
                p.put("tamaño", Integer.valueOf(tamaño).toString());
                p.put("alcoholGrados", Integer.valueOf(alcoholGrados).toString());
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);
    }
    public void modificarProveedores(String Enombre, String Ecedula, String Etelefono){
        String URL = "http://localhost/LicoreriaDB/modificar_proveedor.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Modificado con exito", Toast.LENGTH_SHORT).show();
                    llenarProveedores("");
                }else{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("id", Ecedula);
                p.put("nombre", Enombre);
                p.put("telefono", Etelefono);
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);
    }

//    otras funciones
    public void spinnerValues(){
        ArrayAdapter ArAdapter = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
        optionList.setAdapter(ArAdapter);
    }
    public void mostrarCalendarioMensual(int op){
        Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(Inventario.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = "" + year + "/" + (month+1) + "/" + dayOfMonth;
                if(op==1){
                    fechaDesde.setText(fecha);
                }
                else{
                    fechaHasta.setText(fecha);
                }
            }
        },año, mes, d);
        pickerDialog.show();
    }
}