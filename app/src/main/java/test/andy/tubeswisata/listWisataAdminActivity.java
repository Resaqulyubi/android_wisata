package test.andy.tubeswisata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Response;
import test.andy.tubeswisata.network.Api;
import test.andy.tubeswisata.util.Util;

import static android.view.View.GONE;

public class listWisataActivity extends AppCompatActivity {
    private listWisataActivity obj;
    private AdapterWisata adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);

        ListView lsvw_data=findViewById(R.id.id_list);

        obj = listWisataActivity.this;


        adapter=new AdapterWisata(this);
        lsvw_data.setAdapter(adapter);
        getRecord();

        lsvw_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialogListOpsi(j -> {
                    if (j == 0) {
                        adapter.getItem(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(listWisataActivity.this);
                        builder.setMessage("Pastikan Data Notifikasi yang sudah Solve, lanjutkan?");
                        builder.setPositiveButton("YA", (dialogInterface, i) -> {
//                            solve(adapter.getItem(position).getId());
                            Toast.makeText(listWisataActivity.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("TIDAK", (dialogInterface, i) -> {
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }, listWisataActivity.this);

            }

        });


    }



    public boolean getRecord() {
        boolean[] a = {false};
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        new AsyncTask<Void, Void, Boolean>() {
            Date dStart = null;
            Date dEnd = null;
//            https://www.studytutorial.in/android-line-chart-or-line-graph-using-mpandroid-library-tutorial
//            List<TransactionDB> transactions = new ArrayList<>();

            ProgressDialog dialog =new ProgressDialog(listWisataActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading get data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
                dStart = new Date();
                dEnd = new Date();

            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;


                HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder();

                try (Response response = new Api(listWisataActivity.this).
                        get(getString(R.string.api_wisata))) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);

                    List<reportPeralatan.Data> reportPeralatans =new ArrayList<>();

                    if (responseBodyObject.getBoolean("status")) {
                        Gson gson = new Gson();
                        reportPeralatan reportPeralatan = gson.fromJson(responseBodyString, reportPeralatan.class);

                        for (int x = 0; x < reportPeralatan.getData().size(); x++) {

                            reportPeralatans.add(reportPeralatan.getData().get(x));

                        }


                        obj.runOnUiThread(new Runnable() {

                            public void run() {
                                adapter.setList(reportPeralatans);

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                adapter.setList(reportPeralatans);
                                Toast.makeText(obj, "Tidak Ada data", Toast.LENGTH_SHORT).show();
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

//    public boolean solve(String id) {
//        boolean[] a = {false};
//
//        new AsyncTask<Void, Void, Boolean>() {
//            ProgressDialog dialog =new ProgressDialog(NotifikasiActivity.this);
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                dialog.setMessage("Loading update data...");
//
//                obj.runOnUiThread(new Runnable() {
//                    public void run() {
//                        dialog.show();
//                    }
//                });
//            }
//
//            @Override
//            protected Boolean doInBackground(Void... voids) {
//                boolean b=false;
//                FormBody.Builder formBody = new FormBody.Builder()
//                        .add("id_pencatatan", id);
//                try (Response response = new Api(NotifikasiActivity.this).
//                        post(getString(R.string.api_pencatatan_editdibacaandroid),formBody)) {
//                    if (response == null || !response.isSuccessful())
//                        throw new IOException("Unexpected code = " + response);
//
//                    String responseBodyString = response.body().string();
//                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
//                    if (responseBodyObject.getBoolean("status")) {
//
//                        obj.runOnUiThread(new Runnable() {
//                            public void run() {
//                                if (dialog!=null&dialog.isShowing()){
//                                    dialog.dismiss();
//                                }
//
//                                viewKonfetti.build()
//                                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
//                                        .setDirection(0.0, 359.0)
//                                        .setSpeed(1f, 5f)
//                                        .setFadeOutEnabled(true)
//                                        .setTimeToLive(2000L)
//                                        .addShapes(Shape.RECT, Shape.CIRCLE)
//                                        .addSizes(new Size(12, 5))
//                                        .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
//                                        .streamFor(300, 5000L);
//
//                                Toast.makeText(obj, "Berhasil di update", Toast.LENGTH_SHORT).show();
//                                getRecord();
//
//                            }
//                        });
//                    }else {
//                        obj.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText(obj, "Return false ,kesalahan data", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                } catch (IOException e) {
//                    obj.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return  b;
//            }
//
//            @Override
//            protected void onPostExecute(Boolean aVoid) {
//                super.onPostExecute(aVoid);
//                obj.runOnUiThread(new Runnable() {
//                    public void run() {
//                        if (dialog!=null&dialog.isShowing()){
//                            dialog.dismiss();
//                        }
//                    }
//                });
//
//
//            }
//        }.execute();
//
//
//        return  a[0];
//
//    }

    public interface ListenerDialog {
        void onClick(int i);
    }

    public void showDialogListOpsi(ListenerDialog listener, Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.Theme_Dialog_Margin_4);
        List<String> where = new ArrayList<String>();

        where.add("Solve");

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

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = Util.toDIP(context, 360);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        alertDialog.getWindow().setAttributes(lp);
    }


}
