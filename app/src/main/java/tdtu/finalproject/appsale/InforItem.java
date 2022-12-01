package tdtu.finalproject.appsale;
// Chi tiet san pham page show detail about item that selected on from list item in laptop,phone,main page
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class InforItem extends AppCompatActivity {
    Toolbar toolbarInforItem;
    ImageView imageInforItem;
    TextView nameInforItem, priceInforItem, decriptionInforItem;
    Button button;
    Spinner selectedNumberItem;
    // khai báo các biến này ở vị trí toàn cục để dễ dàng sử dụng xử lí sử kiện add cart
    int id = 0;
    String nameItem = "";
    int price = 0;
    String imageItem = "";
    String decription = "";
    int idTypeItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_item);

        toolbarInforItem = findViewById(R.id.toolbarInforItemPage);
        imageInforItem = findViewById(R.id.imageInforItem);
        nameInforItem = findViewById(R.id.nameInforItem);
        priceInforItem = findViewById(R.id.priceInforItem);
        decriptionInforItem = findViewById(R.id.decriptionInforItem);
        button = findViewById(R.id.buttonAdd);
        selectedNumberItem = findViewById(R.id.selectedNumberItem);


        EventInforItemToolBar();
        getInfor();
        selectedNumberSpinner();

        // event when click button Them Vao gio hang
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check cart is empty or not
                if(MainActivity.arrayListCart.size() > 0){ // the cart have item
                    int numberItem = Integer.parseInt(selectedNumberItem.getSelectedItem().toString()); // get number
                    boolean exits = false;
                    for(int i = 0; i<MainActivity.arrayListCart.size(); i++){
                        if(MainActivity.arrayListCart.get(i).getId() == id){ // check the item add is existed in cart or not
                            MainActivity.arrayListCart.get(i).setNumberItem(MainActivity.arrayListCart.get(i).getNumberItem() + numberItem);
                            MainActivity.arrayListCart.get(i).setPrice(MainActivity.arrayListCart.get(i).getNumberItem() * price);
                            exits = true; // the item added is exists in cart
                        }
                    }

                    if(exits == false){ // the item added is new Item it don't exists in cart
                        long priceAll = numberItem * price;
                        MainActivity.arrayListCart.add(new Cart(id, nameItem, priceAll, imageItem, numberItem));
                    }
                }
                // if cart empty
                else{
                    int numberItem = Integer.parseInt(selectedNumberItem.getSelectedItem().toString()); // get number
                    long priceAll = numberItem * price;
                    MainActivity.arrayListCart.add(new Cart(id, nameItem, priceAll, imageItem, numberItem));
                }
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public void selectedNumberSpinner() {
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item,number);
        selectedNumberItem.setAdapter(arrayAdapter);
    }

    public void getInfor(){
        Item item = (Item) getIntent().getSerializableExtra("InforItem");

        id = item.getId();
        nameItem = item.getName();
        price = item.getPrice();
        imageItem = item.getImage();
        decription = item.getDecription();
        idTypeItem = item.getIdtype();

        Picasso.with(getApplicationContext()).load(imageItem).into(imageInforItem);
        nameInforItem.setText(nameItem);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        priceInforItem.setText("Price: " + decimalFormat.format(price) + "đ");
        decriptionInforItem.setText(decription);

    }

    public void EventInforItemToolBar(){
        setSupportActionBar(toolbarInforItem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarInforItem.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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