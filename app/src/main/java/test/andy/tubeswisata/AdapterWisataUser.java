package test.andy.tubeswisata;

import test.andy.tubeswisata.model.FeedItem;
import test.andy.tubeswisata.model.wisata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class AdapterWisataUser extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<wisata.Data> feedItems= new ArrayList<>();;
  ;

    public AdapterWisataUser(Activity activity, List<wisata.Data> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    public AdapterWisataUser(Activity activity) {
        this.activity = activity;

    }

    public void setFeedItems(List<wisata.Data> feedItems) {
        this.feedItems = feedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_layout, null);


        TextView tv_nama =  convertView.findViewById(R.id.tv_nama);
        TextView tv_kategori = convertView.findViewById(R.id.tv_kategori);
        TextView tv_deskripsi = convertView.findViewById(R.id.tv_deskripsi);
        TextView tv_latlong = convertView.findViewById(R.id.tv_latlong);
        ImageView imgv_foto = convertView.findViewById(R.id.imgv_foto);

        wisata.Data item = feedItems.get(position);

        tv_nama.setText(item.getNama());
        tv_kategori.setText(item.getKategori());
        tv_deskripsi.setText(item.getDeskripsi());
        tv_latlong.setText(item.getLnglat());


        String url = activity.getString(R.string.api_scheme)+"://"+
                activity.getString(R.string.api_host)+"/"+
                "image";

        Picasso.with(activity)
                .load(url + "/" + item.getFoto())
                .fit()
                .placeholder(R.drawable.ic_no_image)
                .into(imgv_foto);


        return convertView;
    }

}