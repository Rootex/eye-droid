package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.Server.camerapreview.R;

/**
 * Created by plaix on 3/4/16.
 */
public class HomeActivity extends BaseActivity {
    private TextView updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawerLayout.addView(contentView, 0);

        updates = (TextView) findViewById(R.id.updates);
        updates.setText("No current Memories");

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
