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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by WINDOWS 10 on 14/09/2017.
 */

public class AdapterNotifikasi extends BaseAdapter {

    private static final String TAG = "AdapterPencatatan";
    private Context mContext;
    private List<reportPeralatan.Data> peralatanDBS = new ArrayList<>();
    TextView tv_time,tv_nama,tv_input ;
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();
    public AdapterNotifikasi(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return peralatanDBS.size();
    }

    @Override
    public reportPeralatan.Data getItem(int position) {
        return peralatanDBS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_record_pencatatan, null);

//
        tv_time = v.findViewById(R.id.tv_time);
        tv_nama = v.findViewById(R.id.tv_nama);
        tv_input = v.findViewById(R.id.tv_input);
        TextView tv_batas_atas = v.findViewById(R.id.tv_batas_atas);
        TextView tv_batas_bawah = v.findViewById(R.id.tv_batas_bawah);
        TextView tv_unit = v.findViewById(R.id.tv_unit);

        reportPeralatan.Data  data=  peralatanDBS.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date="";
        try {
            date = new SimpleDateFormat("dd MMM yyyy HH:mm").format(sdf.parse(data.getWaktu().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_batas_atas.setText("batas atas : "+data.getLebih_besar());
        tv_batas_bawah.setText("batas bawah : "+data.getLebih_kecil());
        tv_time.setText(date);
        tv_input.setText("input : "+data.getInput()+" "+Html.fromHtml(data.getSatuan()));
        if (data.getNama_peralatan_tengah()!=null&&!data.getNama_peralatan_tengah().isEmpty()){
            if (data.getNama_peralatan_akhir()!=null&& data.getNama_peralatan_akhir().isEmpty()){
                tv_nama.setText(data.getNama_peralatan()+" "+  data.getNama_peralatan_tengah()+"-"+data.getNama_peralatan_akhir());
            }else {
                tv_nama.setText(data.getNama_peralatan()+" "+data.getNama_peralatan_tengah());
            }
        }else {
            tv_nama.setText(data.getNama_peralatan()+" "+data.getNama_peralatan_akhir());
        }


        if (data. getNotifikasi().equalsIgnoreCase("alarm")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_input.setBackground(mContext.getDrawable(R.drawable.rounded_stroke_label_red_medium));
            }
        }

        if (data.getUnit()!=null&&!data.getUnit().isEmpty()){
            tv_unit.setVisibility(View.VISIBLE);
            tv_unit.setText("UNIT "+data.getUnit());
        }

        return v;
    }

    public void setList(List<reportPeralatan.Data> peralatanDBS){
        this.peralatanDBS = peralatanDBS;
        notifyDataSetChanged();
    }


    public  void setListStatusBayar(){


    }

    public List<reportPeralatan.Data> getPeralatanDBS(){
        Log.d(TAG, "setList: "+ peralatanDBS.toString());
        return peralatanDBS;

    }

//    public void setOnClick(onEditClickListener mListener){
//        this.mListener = mListener;
//    }

    public interface onEditClickListener {
        void onClickListener(int position);
    }


}
