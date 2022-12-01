package tdtu.finalproject.appsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneActivity extends AppCompatActivity {
    Toolbar toolbarPhone; // tool bar in phone page
    ListView listViewPhone; // list of phone
    PhoneAdapter phoneAdapter;
    ArrayList<Item> arrayListPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        toolbarPhone = findViewById(R.id.toolbarPhonePage);
        listViewPhone = findViewById(R.id.listViewPhone);
        arrayListPhone = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(getApplicationContext(),arrayListPhone);
        listViewPhone.setAdapter(phoneAdapter);

        // event click on item in list
        listViewPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),InforItem.class);
                intent.putExtra("InforItem", arrayListPhone.get(i));
                startActivity(intent);
            }
        });

        EventPhoneToolBar();
        getType();
        getData();
    }

    public void EventPhoneToolBar(){
        setSupportActionBar(toolbarPhone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarPhone.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getType(){
        int id = getIntent().getIntExtra("idtypeitem",1);
        Log.d("idItem",id+"");
    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkgetphone, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0;
                String nameItem = "";
                int price = 0;
                String image = "";
                String decription = "";
                int idtype = 0;
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            nameItem = jsonObject.getString("nameItem");
                            price = jsonObject.getInt("price");
                            image = jsonObject.getString("image");
                            decription = jsonObject.getString("decription");
                            idtype = jsonObject.getInt("idType");
                            arrayListPhone.add(new Item(id, nameItem, price, image, decription, idtype));
                            phoneAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    // Add cart icon to toolbar use Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cartToolbar){
            Intent intent = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}