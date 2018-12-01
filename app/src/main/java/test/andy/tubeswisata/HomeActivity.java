package test.andy.tubeswisata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnstart = findViewById(R.id.btnstart);
        Button btnstartadmin = findViewById(R.id.btnstartadmin);

        btnstart.setOnClickListener(v->{

            startActivity(new Intent(this, MainActivity.class));

        });
        btnstartadmin.setOnClickListener(v->{

            startActivity(new Intent(this, LoginActivity.class));

        });

    }
}
