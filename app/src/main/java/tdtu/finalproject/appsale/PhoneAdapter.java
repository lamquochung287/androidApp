package tdtu.finalproject.appsale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PhoneAdapter extends BaseAdapter {
    Context context;
    ArrayList<Item> arrayListPhone;

    public PhoneAdapter(Context context, ArrayList<Item> arrayListPhone) {
        this.context = context;
        this.arrayListPhone = arrayListPhone;
    }

    @Override
    public int getCount() {
        return arrayListPhone.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListPhone.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_phone,null);
            viewHolder.imageView = view.findViewById(R.id.imagePhone);
            viewHolder.namePhone = view.findViewById(R.id.textNamePhone);
            viewHolder.price = view.findViewById(R.id.textPricePhone);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Item item = (Item) getItem(i);
        Picasso.with(context).load(item.getImage()).into(viewHolder.imageView); // image
        viewHolder.namePhone.setText(item.getName()); // name Phone
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // price of phone
        viewHolder.price.setText("Price: " + decimalFormat.format(item.getPrice()) + "đ");

        return view;
    }

    public class ViewHolder{
        public ImageView imageView;
        public TextView namePhone, price;
    }
}
