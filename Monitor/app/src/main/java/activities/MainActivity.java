package activities;

import com.Server.camerapreview.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageButton preview;
	private ImageButton review;
	private ImageButton settings;
	private ImageButton viewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_item);


		preview = (ImageButton) findViewById(R.id.imageButton1);
		review = (ImageButton) findViewById(R.id.imageButton2);
		settings = (ImageButton) findViewById(R.id.imageButton3);
		viewer = (ImageButton) findViewById(R.id.imageButton4);

		preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPreviewActivity();
			}
		});

		review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startReviewActivity();
			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSettingsActivity();
			}

		});

		viewer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PackageManager pm = getPackageManager();
				Intent appStartIntent = pm
						.getLaunchIntentForPackage("org.plaix.viewer");
				try{
				startActivity(appStartIntent);
				}catch(Exception x){
					Log.d("MainActivity", "Application not Found: "+x.getMessage());
					Toast.makeText(getApplicationContext(), "Module not installed", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	public void startPreviewActivity() {
		startActivity(new Intent(this, PreviewActivity.class));
	}

	public void startReviewActivity() {
		startActivity(new Intent(this, ReviewActivity.class));
	}

	public void startSettingsActivity() {
		startActivity(new Intent(this, SettingsActivity.class));
	}

	// Handling the back key press.
	@Override
	public void onBackPressed() {
		finish();
		startActivity(new Intent(this, HomeActivity.class));
	}

}
