package activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.Server.camerapreview.R;

/**
 * Created by plaix on 3/4/16.
 */
public class HomeActivity extends MainActivityMemories{
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
        finish();
        startActivity(new Intent(this, MainActivityMemories.class));
    }
}
