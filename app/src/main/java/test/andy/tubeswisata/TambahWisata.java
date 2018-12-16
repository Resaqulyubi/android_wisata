package test.andy.tubeswisata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.andy.tubeswisata.model.wisata;
import test.andy.tubeswisata.network.Api;
import test.andy.tubeswisata.util.Util;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TambahWisata extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private TambahWisata obj;
    private AdapterWisata adapter;
    private EditText et_nama, et_lat, et_long, et_desk;
    private Button btn_simpan;
    private Button btn_add_gambar;
    private TextView tv_add_gambar;
    private LinearLayout lnly_add_gambar;
    private Spinner sp_kategori;
    private boolean isAddImage = false;
    Uri ImageFileUri;
    private String selectedPath = "";
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_wisata);


        et_nama = findViewById(R.id.et_nama);
        et_lat = findViewById(R.id.et_lat);
        et_long = findViewById(R.id.et_long);
        et_desk = findViewById(R.id.et_desk);
        sp_kategori = findViewById(R.id.sp_kategori);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_add_gambar = findViewById(R.id.btn_add_gambar);
        tv_add_gambar = findViewById(R.id.tv_add_gambar);
        lnly_add_gambar = findViewById(R.id.lnly_add_gambar);

        if (getIntent().getStringExtra("id") != null) {
            ;
            et_nama.setText(getIntent().getStringExtra("nama"));
            et_desk.setText(getIntent().getStringExtra("deskripsi"));
            String[] lnglat = getIntent().getStringExtra("lnglat").split(",");
            et_long.setText(lnglat[0]);
            et_lat.setText(lnglat[1]);

        }

        String[] arraySpinner = new String[]{"Alam", "Buatan"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_kategori.setAdapter(arrayAdapter);


        obj = this;
        adapter = new AdapterWisata(this);

        btn_simpan.setOnClickListener(view -> {
            if (et_nama.getText().toString().isEmpty() ||
                    et_lat.getText().toString().isEmpty() ||
                    et_long.getText().toString().isEmpty() ||
                    et_desk.getText().toString().isEmpty() ||
                    sp_kategori.getSelectedItem().toString().isEmpty()) {
                Toast.makeText(obj, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {


                if (getIntent().getStringExtra("id") != null && !getIntent().getStringExtra("id").isEmpty()) {
                    update(getIntent().getStringExtra("id"), et_nama.getText().toString(), et_lat.getText().toString(), et_long.getText().toString(), et_desk.getText().toString(), sp_kategori.getSelectedItem().toString(), getIntent().getStringExtra("foto"));
                } else {
                    if (selectedPath.isEmpty()){
                        Toast.makeText(obj, "Belum memilih gambar", Toast.LENGTH_SHORT).show();
                    }else {
                        simpan(et_nama.getText().toString(), et_lat.getText().toString(), et_long.getText().toString(), et_desk.getText().toString(), sp_kategori.getSelectedItem().toString());
                    }
                }

            }

        });


        if (getIntent().getBooleanExtra("add image", false)) {
            isAddImage = true;
            lnly_add_gambar.setVisibility(View.VISIBLE);


            btn_add_gambar.setOnClickListener(view -> {
                chooseImage();
            });
        }


        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
        
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data ", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data ", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }


    public boolean simpan(String nama, String lati, String longt, String desk, String kategori) {
        boolean[] a = {false};


        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog dialog = new ProgressDialog(TambahWisata.this);

            String nameTrim = nama.replaceAll("\\s+", "");
            String filepath = selectedPath;
            String extension = "";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading update data...");
                int i = filepath.lastIndexOf('.');
                if (i >= 0) {
                    extension = filepath.substring(i + 1);
                }

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b = false;
                FormBody.Builder formBody = new FormBody.Builder()
                        .add("nama", nama)
                        .add("lnglat", longt + "," + lati)
                        .add("deskripsi", desk)
                        .add("foto", nameTrim+"."+extension)
                        .add("kategori", kategori)
                        .add("action", "POST");
                try (Response response = new Api(TambahWisata.this).
                        post(getString(R.string.api_wisata), formBody)) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        UploadImage(filepath,nameTrim,extension);

                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (dialog != null & dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                Toast.makeText(obj, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();

                            }
                        });
                    } else {
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
                return b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog != null & dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return a[0];

    }

    public boolean update(String id, String nama, String lati, String longt, String desk, String kategori, String foto) {
        boolean[] a = {false};

        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog dialog = new ProgressDialog(TambahWisata.this);

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
                boolean b = false;
                FormBody.Builder formBody = new FormBody.Builder()
                        .add("id", id)
                        .add("nama", nama)
                        .add("lnglat", longt + "," + lati)
                        .add("deskripsi", desk)
                        .add("foto", foto)
                        .add("kategori", kategori)
                        .add("action", "PUT");
                try (Response response = new Api(TambahWisata.this).
                        post(getString(R.string.api_wisata), formBody)) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (dialog != null & dialog.isShowing()) {
                                    dialog.dismiss();
                                }


                                Toast.makeText(obj, "Berhasil diedit", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();

                            }
                        });
                    } else {
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
                return b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog != null & dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return a[0];

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

    private void chooseImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                ImageFileUri = data.getData();
//                selectedPath = getPathgetPath(ImageFileUri);
                getMimeType(this,ImageFileUri);
                selectedPath = getRealPathFromURI(ImageFileUri);
//                selectedPath = ImageFileUri.getPath()+"."+getMimeType(this,ImageFileUri)   ;


                tv_add_gambar.setText(selectedPath);
            }
        }
    }

    public interface ListenerDialog {
        void onClick(int i);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
    private Void UploadImage(String filePath,String name, String extension){

        File file = new File(filePath);
        file.getPath();
        String lama_path=file.getPath();

        String new_path= lama_path.replace(file.getName(),name+"."+extension );

        Log.d("aaaaaaaaa", "UploadImage: new path"+new_path);

        File newFile = new File(new_path);

        if (file.exists()) {
            FileChannel src = null;
            FileChannel dst=null;
            try {
                src = new FileInputStream(file).getChannel();
                dst = new FileOutputStream(newFile).getChannel();
                filePath=new_path;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        new UploadFileToServer(name+"."+extension,new_path).execute();


        return null;
    }


    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        String filename="";
String filePath="";
        UploadFileToServer(String filename,String filePath){
            this.filePath =filePath;
           this.filename =filename;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
//            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
//
//            // updating progress bar value
//            progressBar.setProgress(progress[0]);
//
//            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = "";

            File sourceFile = new File(filePath);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", filename, RequestBody.create(MediaType.parse(getMediaType(sourceFile)), sourceFile))
                    .build();


            try (Response response = new Api(TambahWisata.this,"").setDefaultTimeOut(20).post(TambahWisata.this.getString(R.string.api_upload),requestBody)) {
                if (response == null || !response.isSuccessful())
                    throw new IOException("Unexpected code = " + response);

                String responseBodyString = response.body().string();
                JSONObject responseBodyObject = new JSONObject(responseString);
                if (!responseBodyObject.getBoolean("error")){
                   return responseBodyObject.getString("hasil");
                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();

            } finally {

            }


            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            File fdelete = new File(filePath);
            if (fdelete.exists()) {
                fdelete.delete();
            }
            // showing the server response in an alert dialog

//            JSONObject responseBodyObject = new JSONObject(responseBodyString);
//            if (responseBodyObject.getBoolean("status")) {


            super.onPostExecute(result);
        }


    }

    public static String getMediaType(File file) {
        String mediaType = "";

        Uri selectedUri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        mediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        return mediaType;
    }
}
