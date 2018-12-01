package test.andy.tubeswisata;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListWisata extends ArrayAdapter {
    private ArrayList list;
    private Activity act;

    public ListWisata(Activity context, ArrayList objects){
        super(context, R.layout.item, objects);
        this.list = objects;
        this.act = context;
    }

    static class ViewHolder {
        protected ImageView icon;
        protected TextView nama;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if (view == null){
            LayoutInflater inflater = act.getLayoutInflater();
            view = inflater.inflate(R.layout.item, null);
            ViewHolder holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.item_icon);
            holder.nama = (TextView) view.findViewById(R.id.item_nama);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Wisata wisata = (Wisata) list.get(position);
//        holder.icon.setImageResource(R.drawable.pong);
//        holder.nama.setText(wisata.getName());

        return  view;
    }
}
