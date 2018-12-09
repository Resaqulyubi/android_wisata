package test.andy.tubeswisata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Response;
import test.andy.tubeswisata.model.wisata;
import test.andy.tubeswisata.network.Api;
import test.andy.tubeswisata.util.Util;

public class TambahWisata extends AppCompatActivity {
    private TambahWisata obj;
    private AdapterWisata adapter;
    private EditText et_nama,et_lat ,et_long ,et_desk;
   private Button btn_simpan;
private Spinner sp_kategori;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_wisata);


         et_nama=findViewById(R.id.et_nama);
        et_lat=findViewById(R.id.et_lat);
        et_long=findViewById(R.id.et_long);
        et_desk=findViewById(R.id.et_desk);
        sp_kategori=findViewById(R.id.sp_kategori);
        btn_simpan=findViewById(R.id.btn_simpan);



        if (getIntent().getStringExtra("id")!=null){
         ;
            et_nama.setText(getIntent().getStringExtra("nama"));
            et_desk.setText(getIntent().getStringExtra("deskripsi"));
          String[] lnglat=  getIntent().getStringExtra("lnglat").split(",");
            et_long.setText(lnglat[0]);
            et_lat.setText(lnglat[1]);

        }

        String[] arraySpinner = new String[] {"Alam", "Buatan"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_kategori.setAdapter(arrayAdapter);


        obj = this;
        adapter=new AdapterWisata(this);

       btn_simpan.setOnClickListener(view -> {
           if (et_nama.getText().toString().isEmpty()||
                   et_lat.getText().toString().isEmpty()||
                   et_long.getText().toString().isEmpty()||
                   et_desk.getText().toString().isEmpty()||
                   sp_kategori.getSelectedItem().toString().isEmpty()){
               Toast.makeText(obj, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
           }else {

               if (getIntent().getStringExtra("id")!=null&&!getIntent().getStringExtra("id").isEmpty()){
                   update(getIntent().getStringExtra("id"),et_nama.getText().toString(),et_lat.getText().toString(),et_long.getText().toString(),et_desk.getText().toString(),sp_kategori.getSelectedItem().toString(),getIntent().getStringExtra("foto"));
               }else {
                   simpan(et_nama.getText().toString(),et_lat.getText().toString(),et_long.getText().toString(),et_desk.getText().toString(),sp_kategori.getSelectedItem().toString());
               }

           }

       });

    }
    
    public boolean simpan(String nama,String lati,String longt, String desk, String kategori) {
        boolean[] a = {false};

        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog dialog =new ProgressDialog(TambahWisata.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading update data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;
                FormBody.Builder formBody = new FormBody.Builder()
                        .add("nama", nama)
                        .add("lnglat", longt+","+lati)
                        .add("deskripsi", desk)
                        .add("foto", "")
                        .add("kategori", kategori)
                        .add("action", "POST");
                try (Response response = new Api(TambahWisata.this).
                        post(getString(R.string.api_wisata),formBody)) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (dialog!=null&dialog.isShowing()){
                                    dialog.dismiss();
                                }


                                Toast.makeText(obj, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(obj, "Return false ,kesalahan data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }

    public boolean update(String id, String nama, String lati, String longt, String desk, String kategori, String foto) {
        boolean[] a = {false};

        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog dialog =new ProgressDialog(TambahWisata.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading update data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;
                FormBody.Builder formBody = new FormBody.Builder()
                        .add("id", id)
                        .add("nama", nama)
                        .add("lnglat", longt+","+lati)
                        .add("deskripsi", desk)
                        .add("foto", foto)
                        .add("kategori", kategori)
                        .add("action", "PUT");
                try (Response response = new Api(TambahWisata.this).
                        post(getString(R.string.api_wisata),formBody)) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (dialog!=null&dialog.isShowing()){
                                    dialog.dismiss();
                                }


                                Toast.makeText(obj, "Berhasil diedit", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(obj, "Return false ,kesalahan data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }


    public interface ListenerDialog {
        void onClick(int i);
    }

    public void showDialogListOpsi(ListenerDialog listener, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog_Margin_4);
        List<String> where = new ArrayList<String>();

        where.add("Hapus");

        String[] strings = new String[where.size()];
        where.toArray(strings);

        builder.setItems(strings, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.onClick(0);
                    break;
                default:
                    listener.onClick(which);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = Util.toDIP(context, 360);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        alertDialog.getWindow().setAttributes(lp);
    }


}
