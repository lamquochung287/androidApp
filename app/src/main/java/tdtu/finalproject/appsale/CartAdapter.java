package tdtu.finalproject.appsale;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

// this class to custom list view row_items in Cart
public class CartAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cart> arrayListCart;

    public CartAdapter(Context context, ArrayList<Cart> arrayListCart) {
        this.context = context;
        this.arrayListCart = arrayListCart;
    }

    @Override
    public int getCount() {
        return arrayListCart.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListCart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        int position = i;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_cart,null);
            viewHolder.imageView = view.findViewById(R.id.imageCart);
            viewHolder.nameItem = view.findViewById(R.id.nameCartItem);
            viewHolder.priceItem = view.findViewById(R.id.priceCartItem);
            viewHolder.number = view.findViewById(R.id.numberItem);
            viewHolder.buttonMinus = view.findViewById(R.id.buttonMinus);
            viewHolder.buttonPlus = view.findViewById(R.id.buttonPlus);
            viewHolder.buttonDelete = view.findViewById(R.id.buttonDelete);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Cart cart = (Cart)getItem(i);
        Picasso.with(context).load(cart.getImage()).into(viewHolder.imageView);
        viewHolder.nameItem.setText(cart.getNameItem());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.priceItem.setText("Price: " + decimalFormat.format(cart.getPrice()) + "đ");
        viewHolder.number.setText(String.valueOf(cart.getNumberItem()));

        // event plus number Item when click button +
        ViewHolder finalViewHolder = viewHolder;
        if(Integer.parseInt(finalViewHolder.number.getText().toString()) <= 1){
            finalViewHolder.buttonMinus.setVisibility(View.INVISIBLE);
        }
        else{
            finalViewHolder.buttonMinus.setVisibility(View.VISIBLE);
        }

        viewHolder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberNew = Integer.parseInt(finalViewHolder.number.getText().toString()) + 1;
                int number = MainActivity.arrayListCart.get(i).getNumberItem();
                long price = MainActivity.arrayListCart.get(i).getPrice();
                MainActivity.arrayListCart.get(i).setNumberItem(numberNew); // updater new number of Item
                long priceNew = (price * numberNew) / number;
                MainActivity.arrayListCart.get(i).setPrice(priceNew); // update new price of Item
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.number.setText(String.valueOf(numberNew));
                finalViewHolder.priceItem.setText("Price: " + decimalFormat.format(priceNew) + "đ");
                CartActivity.calculateSumPriceCart();
                if(Integer.parseInt(finalViewHolder.number.getText().toString()) <= 1){
                    finalViewHolder.buttonMinus.setVisibility(View.INVISIBLE);
                }
                else{
                    finalViewHolder.buttonMinus.setVisibility(View.VISIBLE);
                }
            }
        });

        //event minus number Item when click button -
        viewHolder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberNew = Integer.parseInt(finalViewHolder.number.getText().toString()) - 1;
                int number = MainActivity.arrayListCart.get(i).getNumberItem();
                long price = MainActivity.arrayListCart.get(i).getPrice();
                MainActivity.arrayListCart.get(i).setNumberItem(numberNew); // updater new number of Item
                long priceNew = (price * numberNew) / number;
                MainActivity.arrayListCart.get(i).setPrice(priceNew); // update new price of Item
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.number.setText(String.valueOf(numberNew));
                finalViewHolder.priceItem.setText("Price: " + decimalFormat.format(priceNew) + "đ");
                CartActivity.calculateSumPriceCart();
                if(Integer.parseInt(finalViewHolder.number.getText().toString()) <= 1){
                    finalViewHolder.buttonMinus.setVisibility(View.INVISIBLE);
                }
                else{
                    finalViewHolder.buttonMinus.setVisibility(View.VISIBLE);
                }
            }
        });

        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Vui lòng xác nhận xóa sản phẩm");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // check cart before and after delete item is Empty or not
                        if(MainActivity.arrayListCart.size() <= 0){
                            CartActivity.textCartEmpty.setVisibility(View.VISIBLE);
                        }
                        else{
                            CartActivity.textCartEmpty.setVisibility(View.INVISIBLE);
                            arrayListCart.remove(position);
                            notifyDataSetChanged();
                            CartActivity.calculateSumPriceCart();
                            // check cart again if cart have only 1 item and delete => cart empty show message empty
                            if(MainActivity.arrayListCart.size() <= 0){
                                CartActivity.textCartEmpty.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyDataSetChanged();
                        CartActivity.calculateSumPriceCart();
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    public class ViewHolder{
        public TextView nameItem, priceItem, number;
        public ImageView imageView;
        public Button buttonMinus, buttonPlus, buttonDelete;
    }
}
