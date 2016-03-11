package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.Server.camerapreview.R;

import java.util.ArrayList;

/**
 * Created by plaix on 3/4/16.
 */
public class HomeActivity extends BaseActivity {
    private ListView updates;
    private ArrayList<String> recent;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawerLayout.addView(contentView, 0);

        updates = (ListView) findViewById(R.id.news_feed);

        recent = new ArrayList<String>();
        recent.add("No recent activity");

        adapter = new ArrayAdapter<String>(this, R.layout.feed_row, recent);
        updates.setAdapter(adapter);

    }

    public void addItem(View v){
        recent.add("No recent activity");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Warining!");
        alertbox.setMessage("Are you sure you want to exit?");

        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        onDestroy();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alertbox.show();

    }
}
