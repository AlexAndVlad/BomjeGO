package alexandvlad.bomjego;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import alexandvlad.bomjego.database.CaughtBomjeDb;
import alexandvlad.bomjego.model.WildBomjeEntry;

public class CaughtListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final WildBomjeEntryAdapter adapter = new WildBomjeEntryAdapter(this);
        recyclerView.setAdapter(adapter);
        final Context context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.setBomje(new CaughtBomjeDb(context).getAll());
            }
        }).start();
    }
}
