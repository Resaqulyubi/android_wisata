package test.andy.tubeswisata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Response;
import test.andy.tubeswisata.network.Api;

public class LoginActivity extends Activity {
    private LoginActivity obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);
        obj = this;

        Button btnLogin = findViewById(R.id.btn_login);
        EditText et_username = findViewById(R.id.et_username);
        EditText et_password = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(v->{
            if (!et_username.getText().toString().isEmpty()||!et_password.getText().toString().isEmpty()){
                login(et_username.getText().toString(),et_password.getText().toString());

            }else {
                Toast.makeText(this, "Data Belum diisi!!", Toast.LENGTH_SHORT).show();
            }


        });

    }

    public boolean login(String username,String password) {
        boolean[] a = {false};

        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog dialog =new ProgressDialog(LoginActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("checking user...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;
                HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder();
                httpUrlBuilder.addQueryParameter("username",username);
                httpUrlBuilder.addQueryParameter("password",password);

                try (Response response = new Api(LoginActivity.this).
                        get(getString(R.string.api_user),httpUrlBuilder)) {
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
                                startActivity(new Intent(LoginActivity.this, listWisataActivityAdmin.class));

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(obj, "Tidak ada User yang ditemukan", Toast.LENGTH_SHORT).show();
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
