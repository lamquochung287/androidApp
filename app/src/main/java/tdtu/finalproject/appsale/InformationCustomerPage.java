package tdtu.finalproject.appsale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

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

import java.util.HashMap;
import java.util.Map;
// Information Customer Page Activity
public class InformationCustomerPage extends AppCompatActivity {
    Toolbar toolbarInforPage;
    EditText inputName, inputEmail, inputPhoneNumber, inputAdress;
    Button buttonCancel, buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_customer_page);

        inputName = findViewById(R.id.inputNameCustomer);
        inputEmail = findViewById(R.id.inputEmailCustomer);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumberCustomer);
        inputAdress = findViewById(R.id.inputPhoneNumberCustomer);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        toolbarInforPage = findViewById(R.id.toolbarInforCustomerPage);

        eventToolBar();
        //back to Cart Page if customer click on cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationCustomerPage.this, CartActivity.class);
                startActivity(intent);
            }
        });
        //send the information of customer and all item in cart to server
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input from the form in page
                final String nameCustom = inputName.getText().toString();
                final String email = inputEmail.getText().toString();
                final String phone = inputPhoneNumber.getText().toString();
                final String address = inputAdress.getText().toString();

                //check all input is empty?
                if(nameCustom.isEmpty() && email.isEmpty() && phone.isEmpty() && address.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationCustomerPage.this);
                    builder.setTitle("Dữ liệu khách hàng");
                    builder.setMessage("Vui lòng không bỏ trống bất kì thông tin nào");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
                // all input is not empty send information customer and all item in Cart to server
                else{
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = Server.linkInforCustom;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String idCustom) { //value String idCustom will get idCustom in file insertCustom.php execute insert customer success
                            if(idCustom.isEmpty() == false){ // check the process customer is success if success file insertCustom.php will return the idCustomer
                                // insert the item in Cart to ordertable
                                RequestQueue requestQueueItem = Volley.newRequestQueue(getApplicationContext());
                                String urlItem = Server.linkSendCart;
                                StringRequest stringRequestItem = new StringRequest(Request.Method.POST, urlItem, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) { // this value response is the echo 'success' or 'error'
                                        if(response.equals("Success")){ // when process Success
                                            MainActivity.arrayListCart.clear(); // clear all item in Cart
                                            CartActivity.textCartEmpty.setVisibility(View.VISIBLE); // show the Message Cart Empty
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InformationCustomerPage.this);
                                            builder.setTitle("Thanh toán đơn đặt hàng thành công");
                                            builder.setMessage("Đã thanh toán đơn đàng hàng vui lòng nhấn OK để trở về homepage");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(InformationCustomerPage.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.show();
                                        }
                                        else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InformationCustomerPage.this);
                                            builder.setTitle("Thanh toán đơn đặt hàng không thành công");
                                            builder.setMessage("Quá trình thanh toán xảy ra lỗi vui lòng thử lại sau");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(InformationCustomerPage.this, CartActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for(int i = 0; i < MainActivity.arrayListCart.size(); i++){
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("idCustomer",idCustom); // get key from orderData.php
                                                jsonObject.put("idItem",MainActivity.arrayListCart.get(i).getId());
                                                jsonObject.put("nameItem",MainActivity.arrayListCart.get(i).getNameItem());
                                                jsonObject.put("numberItem",MainActivity.arrayListCart.get(i).getNumberItem());
                                                jsonObject.put("price",MainActivity.arrayListCart.get(i).getPrice());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("dataCart",jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                requestQueueItem.add(stringRequestItem);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //HashMap<Key,Value> send all information custom get by form thanh toan through insertCustom.php to send it the server
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap.put("nameCustom",nameCustom); // get key from $_POST in insertCustom.php
                            hashMap.put("phoneNumber",phone);
                            hashMap.put("email",email);
                            hashMap.put("address",address);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
    }

    private void eventToolBar() {
        setSupportActionBar(toolbarInforPage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarInforPage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}