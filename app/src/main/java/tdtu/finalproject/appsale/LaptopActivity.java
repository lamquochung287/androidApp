package tdtu.finalproject.appsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LaptopActivity extends AppCompatActivity {
    Toolbar toolbarLaptop; // tool bar in phone page
    ListView listViewLaptop; // list of phone
    LaptopAdapter laptopAdapter;
    ArrayList<Item> arrayListLaptop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        toolbarLaptop = findViewById(R.id.toolbarLaptopPage);
        listViewLaptop = findViewById(R.id.listViewLaptop);
        arrayListLaptop = new ArrayList<>();
        laptopAdapter = new LaptopAdapter(getApplicationContext(),arrayListLaptop);
        listViewLaptop.setAdapter(laptopAdapter);
        //event selected item in list
        listViewLaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),InforItem.class);
                intent.putExtra("InforItem", arrayListLaptop.get(i));
                startActivity(intent);
            }
        });
        EventLaptopToolBar();
        getData();
    }

    public void EventLaptopToolBar(){
        setSupportActionBar(toolbarLaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkgetlaptop, new Response.Listener<JSONArray>() {
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
                            arrayListLaptop.add(new Item(id, nameItem, price, image, decription, idtype));
                            laptopAdapter.notifyDataSetChanged();
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