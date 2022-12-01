package tdtu.finalproject.appsale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;
// Cart Activity Page Cart
public class CartActivity extends AppCompatActivity {

    static TextView textSumPrice, textCartEmpty;
    Button buttonBuy, buttonChooseAnotherItem;
    ListView listviewCart;
    Toolbar toolbarCartPage;
    static CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        //init
        listviewCart = findViewById(R.id.listviewCart);
        textSumPrice = findViewById(R.id.textSumPrice);
        textCartEmpty = findViewById(R.id.textCartEmpty);
        toolbarCartPage = findViewById(R.id.toolbarCartPage);
        buttonBuy = findViewById(R.id.buttonBuy);
        buttonChooseAnotherItem = findViewById(R.id.buttonChooseAnotherItem);
        cartAdapter = new CartAdapter(CartActivity.this, MainActivity.arrayListCart);
        listviewCart.setAdapter(cartAdapter);

        EventInforItemToolBar();
        checkItemInCart();
        calculateSumPriceCart();

        // Button thanh toán chuyển sang trang đặt mua để khách hàng nhập thông tin
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.arrayListCart.size() <= 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Đặt mua sản phẩm");
                    builder.setMessage("Giỏ hàng của bạn không có sản phẩm để đặt mua");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
                else{
                    Intent intent = new Intent(CartActivity.this,InformationCustomerPage.class);
                    startActivity(intent);
                }
            }
        });
        // button tiếp tục chọn sản phẩm back về homepage
        buttonChooseAnotherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

    public static void calculateSumPriceCart() {
        long sumPrice = 0;
        for(int i = 0; i < MainActivity.arrayListCart.size(); i++){
            sumPrice += MainActivity.arrayListCart.get(i).getPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textSumPrice.setText("Price: " + decimalFormat.format(sumPrice) + "đ");
    }

    public void EventInforItemToolBar(){
        setSupportActionBar(toolbarCartPage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCartPage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void checkItemInCart(){
        if(MainActivity.arrayListCart.size() > 0){
            cartAdapter.notifyDataSetChanged();
            textCartEmpty.setVisibility(View.INVISIBLE);
            listviewCart.setVisibility(View.VISIBLE);
        }
        else{
            cartAdapter.notifyDataSetChanged();
            textCartEmpty.setVisibility(View.VISIBLE); // print Giỏ hàng k có sản phẩm
            listviewCart.setVisibility(View.INVISIBLE);

        }
    }
}