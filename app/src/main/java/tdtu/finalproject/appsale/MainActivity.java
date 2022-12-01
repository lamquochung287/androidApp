package tdtu.finalproject.appsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper; // chạy quảng cáo
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView; // menu bên trái list gồm: trang chủ, phone, laptop, other
    DrawerLayout drawerLayout;

    //2 dòng dưới để lưu 3 loại của item: phone, laptop, other show trên listview menu của toolbar
    ArrayList<ItemType> arrayListType;
    ItemTypeAdapter itemTypeAdapter;

    //2 dòng dưới để lưu tất data của sản phẩm show listview tất cả sản phẩm đang bán
    ArrayList<Item> arrayListItem;
    ItemAdapter itemAdapter;

    public static ArrayList<Cart> arrayListCart; // đồng nhất mảng lưu data trong giỏ hàng tránh việc mất data đã lưu vào giỏ hàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewfilpper);
        recyclerView = findViewById(R.id.recyclerview);
        navigationView = findViewById(R.id.navigationview);
        listView = findViewById(R.id.listview);
        drawerLayout = findViewById(R.id.drawerlayout);

        // menu listview
        arrayListType = new ArrayList<>();
        itemTypeAdapter = new ItemTypeAdapter(arrayListType, getApplicationContext());
        listView.setAdapter(itemTypeAdapter);
        arrayListType.add(0,new ItemType(0, "Home Page","https://cdn-icons-png.flaticon.com/512/25/25694.png"));

        //show all item below textView Tat ca san pham
        arrayListItem = new ArrayList<>();
        itemAdapter = new ItemAdapter(arrayListItem, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(itemAdapter);

        //Cart

        if(arrayListCart != null){
            // number in cart # 0 to make sure data in cart have saved before don't lost
            //nếu trong giỏ hàng có chứa sản phẩm k tạo mới arraylist tránh mất dữ liệu item đã được thêm vào

        }
        else{
            arrayListCart = new ArrayList<>(); // create array to save data when customer add item to cart
        }

        // kiểm tra có kết nối internet hay k để thực hiện, k có intert show toast và kết thúc app
        if(CheckInternet.wifiConnected(getApplicationContext())){
            ActionBar();
            ActionViewFlipper();// advertisement
            getItemType(); // item type list(homepage, phone,laptop) in menu
            getItem(); // all item show below textview in homepage
            selectedItemMenu(); // catch event click on menu list view
        }
        else{
            Toast.makeText(getApplicationContext(), "Kiem tra ket noi wifi",Toast.LENGTH_SHORT).show();
            finish();
        }




    }

    public void ActionBar(){ // dùng để set icon menu trên toolbar + khi ấn icon menu trên toolbar show list view từ trái sang
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
    }

    public void ActionViewFlipper(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        arrayList.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        for(int i = 0; i <arrayList.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(arrayList.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        // thực hiện tự chuyển động đổi băng rôn trong 3s
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
    }

    // hàm này dùng để lấy 3 loại item: phone, laptop, other để show lên listview của icon menu
    public void getItemType(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkgettype, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0;
                String nameType = "";
                String image = "";
                if(response != null){
                    for(int i = 0; i <response.length(); i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            nameType = jsonObject.getString("typeItem");
                            image = jsonObject.getString("image");
                            arrayListType.add(new ItemType(id,nameType,image));
                            itemTypeAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    // lấy tất cả sản phẩm đang có show dưới dòng textview Tất cả sản phẩm đang được bán
    public void getItem() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkgetitem, new Response.Listener<JSONArray>() {
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
                            idtype = jsonObject.getInt("idItem");
                            arrayListItem.add(new Item(id, nameItem, price, image, decription, idtype));
                            itemAdapter.notifyDataSetChanged();
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

    // thực hiện xử lí sự kiện chuyển trang ở listview của menu khi click vào icon menu trên toolbar
    public void selectedItemMenu(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){ // click homepage
                    if(CheckInternet.wifiConnected(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this, MainActivity.class); // click button homepage in menu go to homepage
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Kiem tra ket noi internet",Toast.LENGTH_SHORT).show();
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if(i == 1){ // click Phone
                    Intent intent = new Intent(MainActivity.this, PhoneActivity.class); // click button homepage in menu go to homepage
                    intent.putExtra("idtypeitem",1);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if(i == 2){ // click Laptop
                    Intent intent = new Intent(MainActivity.this, LaptopActivity.class); // click button homepage in menu go to homepage
                    intent.putExtra("idtypeitem",arrayListType.get(i).getId());
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if(i == 3){ // click Other item
                    Intent intent = new Intent(MainActivity.this, OtherItemActivity.class); // click button homepage in menu go to homepage
                    intent.putExtra("idtypeitem",arrayListType.get(i).getId());
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

            }
        });
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