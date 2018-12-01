package test.andy.tubeswisata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);

        Button btnLogin = findViewById(R.id.btn_login);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(v->{
            if (!et_email.getText().toString().isEmpty()||!et_password.getText().toString().isEmpty()){
                startActivity(new Intent(this, MainActivity.class));
            }else {
                Toast.makeText(this, "Data Belum diisi!!", Toast.LENGTH_SHORT).show();
            }


        });

    }
}
