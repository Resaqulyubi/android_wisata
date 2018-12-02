package test.andy.tubeswisata;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import test.andy.tubeswisata.model.wisata;


/**
 * Created by WINDOWS 10 on 14/09/2017.
 */

public class AdapterWisata extends BaseAdapter {

    private static final String TAG = "AdapterWisata";
    private Context mContext;
    private List<wisata.Data> wisataList = new ArrayList<>();
    TextView tv_kategori,tv_nama,tv_deskripsi ,tv_latlong;
    ImageView imgv_foto;
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();
    public AdapterWisata(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return wisataList.size();
    }

    @Override
    public wisata.Data getItem(int position) {
        return wisataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.feed_layout, null);

//
        tv_deskripsi = v.findViewById(R.id.tv_deskripsi);
        tv_nama = v.findViewById(R.id.tv_nama);
        tv_latlong = v.findViewById(R.id.tv_latlong);
        tv_kategori = v.findViewById(R.id.tv_kategori);
        imgv_foto = v.findViewById(R.id.imgv_foto);

        wisata.Data  data=  wisataList.get(position);

        tv_deskripsi.setText(" "+data.getDeskripsi());
        tv_nama.setText(""+data.getNama());
        tv_latlong.setText(data.getLnglat());
        tv_kategori.setText(data.getKategori());


        String url = mContext.getString(R.string.api_scheme)+"://"+
                mContext.getString(R.string.api_host)+"/"+
                "image";

        Picasso.with(mContext)
                .load(url + "/" + data.getFoto())
                .fit()
                .placeholder(R.drawable.ic_no_image)
                .into(imgv_foto);





        return v;
    }

    public void setList(List<wisata.Data> wisata){
        this.wisataList = wisata;
        notifyDataSetChanged();
    }


    public  void setListStatusBayar(){


    }

    public List<wisata.Data> getWisataList(){
        Log.d(TAG, "setList: "+ wisataList.toString());
        return wisataList;

    }

//    public void setOnClick(onEditClickListener mListener){
//        this.mListener = mListener;
//    }

    public interface onEditClickListener {
        void onClickListener(int position);
    }


}
