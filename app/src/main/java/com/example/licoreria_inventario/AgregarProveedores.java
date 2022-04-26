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

public class AgregarProveedores extends AppCompatActivity {
    EditText Eid, Enombre, Etelefono;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_proveedores);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registrar = findViewById(R.id.btnReg);
        Eid = findViewById(R.id.RdocP);
        Enombre = findViewById(R.id.RNombreP);
        Etelefono = findViewById(R.id.RphoneP);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id, nombre, telefono, URL;
                id = Eid.getText().toString();
                telefono = Etelefono.getText().toString();
                nombre = Enombre.getText().toString();
                URL = "http://localhost/LicoreriaDB/agregar_proveedor.php";
                StringRequest rq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")){
                            Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_SHORT).show();
                            Eid.setText("");
                            Etelefono.setText("");
                            Enombre.setText("");
                        }else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        p.put("id", id);
                        p.put("telefono", telefono);
                        p.put("nombre", nombre);
                        return p;
                    }
                };
                RequestQueue request = Volley.newRequestQueue(AgregarProveedores.this);
                request.add(rq);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act = new Intent(AgregarProveedores.this, Inventario.class);
        startActivity(act);
    }

}