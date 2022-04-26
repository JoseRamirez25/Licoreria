package com.example.licoreria_inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistrarProducto extends AppCompatActivity {
    EditText NombreP, CantP, TamañoP, GradosAl, DescripcionP, proveedoresID, PrecioP;
    Button registrar;
    ArrayList<ListProveedores> listprovs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        proveedoresID = findViewById(R.id.proveedorID);
        NombreP = findViewById(R.id.NombreP);
        CantP = findViewById(R.id.cant);
        TamañoP = findViewById(R.id.tamaño);
        GradosAl = findViewById(R.id.gradosAlcohol);
        DescripcionP = findViewById(R.id.Doc);
        PrecioP = findViewById(R.id.precio);
        registrar = findViewById(R.id.btnReg);

        listprovs = new ArrayList();
        llenarProveedores();

        registrar.setOnClickListener(new View.OnClickListener() {
            boolean found=false;
            @Override
            public void onClick(View v) {
                String nombreP = NombreP.getText().toString();
                int cantP = Integer.parseInt(CantP.getText().toString());
                int tamañoP = Integer.parseInt(TamañoP.getText().toString());
                int gradosAl = Integer.parseInt(GradosAl.getText().toString());
                int precio = Integer.parseInt(PrecioP.getText().toString());
                String descripcionP = DescripcionP.getText().toString();
                String proveedoresid = proveedoresID.getText().toString();
                for (int i=0; i<listprovs.size(); i++){
                    if (listprovs.get(i).getCc().equals(proveedoresID.getText().toString())){
                        found = true;
                    }
                }
                if (found){
                    registrarP(nombreP,cantP, precio,tamañoP,gradosAl,descripcionP,proveedoresid);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Este proveedor no se encuentra registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void llenarProveedores(){
        String URL = "http://192.168.0.16/LicoreriaDB/obtener_proveedores.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("fallo")){
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }else {
                    listprovs.clear();
                    String idP, nombreP, telefonoP;
                    try {
                        JSONArray lista = new JSONArray(response);

                        for (int i = 0; i < lista.length(); i++){
                            JSONObject productos = lista.getJSONObject(i);
                            idP = productos.getString("PrID");
                            nombreP = productos.getString("PrNombre");
                            telefonoP = productos.getString("PrTelefono");
                            listprovs.add(new ListProveedores(idP, nombreP, telefonoP));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrarProducto.this, "algo fallo "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("search", "");
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);

    }
    public void registrarP(String nombreP,int cantP, int precio, int tamañoP,int gradosAl,String descripcionP,String proveedoresid){
        String URL = "http://192.168.0.16/LicoreriaDB/agregar_producto.php";
        StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_SHORT).show();
                    proveedoresID.setText("");
                    NombreP.setText("");
                    CantP.setText("");
                    TamañoP.setText("");
                    GradosAl.setText("");
                    DescripcionP.setText("");
                    PrecioP.setText("");
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
                p.put("nombre", nombreP);
                p.put("cantidad", Integer.valueOf(cantP).toString());
                p.put("precio", Integer.valueOf(precio).toString());
                p.put("tamaño", Integer.valueOf(tamañoP).toString());
                p.put("gradosA", Integer.valueOf(gradosAl).toString());
                p.put("descripcion", descripcionP);
                p.put("proveedor", proveedoresid);
                return p;
            }
        };
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(rq);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act = new Intent(RegistrarProducto.this, Inventario.class);
        startActivity(act);
    }

}