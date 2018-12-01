package test.andy.tubeswisata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);
        ArrayList list = new ArrayList();

        list.add(new Wisata("Banyu Anjlok"));

        ListAdapter adapter = new ListWisata(this, list) {
        };

        ListView listView = (ListView) findViewById(R.id.id_list);
        listView.setAdapter(adapter);
    }
}
