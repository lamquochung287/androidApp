package tdtu.finalproject.appsale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    Context context;
    ArrayList<Item> arrayListItem;

    public ItemAdapter( ArrayList<Item> arrayList, Context context) {
        this.context = context;
        this.arrayListItem = arrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_homepage,null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = arrayListItem.get(position);
        Picasso.with(context).load(item.getImage()).into(holder.imageView);
        holder.nameItem.setText(item.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText("Price: " + decimalFormat.format(item.getPrice()) + "đ");

    }

    @Override
    public int getItemCount() {
        return arrayListItem.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView nameItem, price;

        public ItemHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageItem);
            nameItem = view.findViewById(R.id.textnameitem);
            price = view.findViewById(R.id.textprice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InforItem.class);
                    intent.putExtra("InforItem",arrayListItem.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
    }
}
