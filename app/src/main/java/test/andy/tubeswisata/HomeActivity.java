package test.andy.tubeswisata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnstart = findViewById(R.id.btnstart);
        Button btnstartadmin = findViewById(R.id.btnstartadmin);

        btnstart.setOnClickListener(v->{

            startActivity(new Intent(this, listWisataActivityUser.class));

        });
        btnstartadmin.setOnClickListener(v->{

            startActivity(new Intent(this, LoginActivity.class));

        });

    }
}
