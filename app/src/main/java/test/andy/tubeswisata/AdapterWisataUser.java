package test.andy.tubeswisata;

import test.andy.tubeswisata.model.FeedItem;
import test.andy.tubeswisata.model.wisata;

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

//        // Checking for null feed url
//        if (item.getUrl() != null) {
//            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//                    + item.getUrl() + "</a> "));
//
//            // Making url clickable
//            url.setMovementMethod(LinkMovementMethod.getInstance());
//            url.setVisibility(View.VISIBLE);
//        } else {
//            // url is null, remove from the view
//            url.setVisibility(View.GONE);
//        }


        // Feed image
//        if (item.getImge() != null) {
//            feedImageView.setImageUrl(item.getImge(), imageLoader);
//            feedImageView.setVisibility(View.VISIBLE);
//            feedImageView
//                    .setResponseObserver(new FeedImageView.ResponseObserver() {
//                        @Override
//                        public void onError() {
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                        }
//                    });
//        } else {
//            feedImageView.setVisibility(View.GONE);
//        }

        return convertView;
    }

}