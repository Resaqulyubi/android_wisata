package test.andy.tubeswisata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Response;
import test.andy.tubeswisata.model.wisata;
import test.andy.tubeswisata.network.Api;

public class MainActivity extends Activity {
    private AdapterWisataUser adapter;
    private MainActivity obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);
        ListView lsvw_data=findViewById(R.id.id_list);
        obj=this;

        adapter=new AdapterWisataUser(this);
        lsvw_data.setAdapter(adapter);

        lsvw_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.getItem(position);
            }

        });
        getRecord();
    }

    public boolean getRecord() {
        boolean[] a = {false};
        new AsyncTask<Void, Void, Boolean>() {
            Date dStart = null;
            Date dEnd = null;

            ProgressDialog dialog =new ProgressDialog(MainActivity.this);

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


                try (Response response = new Api(MainActivity.this).
                        get(getString(R.string.api_wisata))) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);

                    List<wisata.Data> data =new ArrayList<>();

                    if (responseBodyObject.getBoolean("status")) {
                        Gson gson = new Gson();
                        wisata fromJson = gson.fromJson(responseBodyString, wisata.class);

                        for (int x = 0; x < fromJson.getData().size(); x++) {

                            data.add(fromJson.getData().get(x));

                        }


                        obj.runOnUiThread(new Runnable() {

                            public void run() {
                                adapter.setFeedItems(data);

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                adapter.setFeedItems(data);
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

}
