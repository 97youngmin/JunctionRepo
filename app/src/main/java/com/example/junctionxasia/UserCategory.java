package com.example.junctionxasia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserCategory extends AppCompatActivity {

    private static final String URL_DATA = "http://10.0.2.2:5000/food";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

//        for(int i =0 ; i<=10; i++){
//            ListItem listItem = new ListItem(
//                    "hi"+ (i+1),
//                    "HALLO",
//                    "22",
//                    "11",
//                    "20/60",
//                    "sfdfsdfdsfsdfd",
//                    "jpeg",
//                    "52 Bukit Batok",
//                    "655552",
//                    132.000,
//                    134.555,
//                    1.50,
//                    5
//            );
//            listItems.add(listItem);
//        }
//        adapter = new MyAdapter(listItems, this);
//        recyclerView.setAdapter(adapter);
        loadRecyclerViewData();


    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        Log.d("MyApp", URL_DATA);
        try {
            Intent intent = getIntent();
            String cat = intent.getStringExtra("category");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("location", "2 Adam Rd, 289877");
            jsonBody.put("category", cat);
//            final String mRequestBody = jsonBody.toString();
            Log.d("MyApp", String.valueOf(jsonBody));
            CustomJsonArrayRequest req = new CustomJsonArrayRequest(Request.Method.POST, URL_DATA, jsonBody, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressDialog.dismiss();
                    try{
                        // Loop through the array elements
                        for(int i=0; i<response.length();i++){
                            Log.d("MyApp", String.valueOf(response));
                            // Get current json object
                            JSONObject res = response.getJSONObject(i);
                            ListItem item = new ListItem(
                                    res.getString("name"),
                                    res.getString("category"),
                                    res.getString("closing"),
                                    res.getString("company"),
                                    res.getString("expiry"),
                                    res.getString("id"),
                                    res.getString("image"),
                                    res.getString("location"),
                                    res.getString("phone"),
                                    res.getDouble("lat"),
                                    res.getDouble("long"),
                                    res.getDouble("price"),
                                    res.getInt("quantity")
                            );
                            listItems.add(item);
                        }
                        adapter = new MyAdapter(listItems, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            progressDialog.dismiss();
                            Log.d("MyApp", String.valueOf(error));
                            Toast.makeText(UserCategory.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            requestQueue.add(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}