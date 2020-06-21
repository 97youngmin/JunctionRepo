package com.example.junctionxasia;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UploadFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadfood);

        final EditText dishName = findViewById(R.id.dishname);
        final EditText expiry = findViewById(R.id.expiry);
        final EditText price = findViewById(R.id.price);
        final EditText quantity = findViewById(R.id.quantity);
        Button button = findViewById(R.id.button_upload);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadFood.this, SupplierMainActivity.class);
                startActivity(intent);
                RequestQueue queue = Volley.newRequestQueue(UploadFood.this);
                String url ="http://10.0.2.2:5000/";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(),"Upload Success !",Toast.LENGTH_LONG).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"Upload failed :(",Toast.LENGTH_LONG).show();
                        Log.i("upload",dishName.getText().toString());
                        Log.i("upload",expiry.getText().toString());
                        Log.i("upload","test");
                        Log.i("upload",price.getText().toString());
                        Log.i("upload",quantity.getText().toString());
                        Log.d("MyApp", String.valueOf(error));





                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("name",dishName.getText().toString());
                        params.put("expiry",expiry.getText().toString());
                        params.put("image", "test");
                        params.put("price",price.getText().toString());
                        params.put("quantity",quantity.getText().toString());
                        params.put("company","Karu's");

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });

    }
}