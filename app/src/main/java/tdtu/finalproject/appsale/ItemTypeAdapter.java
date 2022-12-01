package tdtu.finalproject.appsale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemTypeAdapter extends BaseAdapter {
    ArrayList<ItemType> arrayList;
    Context context;

    public ItemTypeAdapter(ArrayList<ItemType> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_menu,null);
            viewHolder.textView = view.findViewById(R.id.typeItemText);
            viewHolder.imageView = view.findViewById(R.id.image);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        ItemType itemType = (ItemType) getItem(i);
        viewHolder.textView.setText(itemType.getName());
        Picasso.with(context).load(itemType.getImage()).into(viewHolder.imageView);

        return view;
    }

    public class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
