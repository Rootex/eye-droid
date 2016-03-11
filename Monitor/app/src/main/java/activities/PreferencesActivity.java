package activities;

import com.Server.camerapreview.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PreferencesActivity extends BaseActivity {

	private EditText email;
	private EditText password;
	private EditText phoneNo;
	private CheckBox emailNotifier;
	private CheckBox phoneNotifier;
	private EditText delay;
	
	public static final String PREF_NAME = "user_settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_preferences, null, false);
		drawerLayout.addView(contentView, 0);
//		setContentView(R.layout.activity_preferences);
		
		
		email = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		phoneNo = (EditText) findViewById(R.id.editText4);
		emailNotifier = (CheckBox) findViewById(R.id.checkBox1);
		phoneNotifier = (CheckBox) findViewById(R.id.checkBox2);
		delay = (EditText) findViewById(R.id.editText3);
		
		SharedPreferences sPref = getSharedPreferences(PREF_NAME, 0);
		
		if(sPref.getString("email", "nil") != "nil")
			email.setText(sPref.getString("email", "Gmail Address"));
		
		if(sPref.getString("phone", "nil") != "nil")
			phoneNo.setText(sPref.getString("phone", "Phone number"));
		
		if(sPref.getBoolean("emailCheck", false))
			emailNotifier.setChecked(true);
		
		if(sPref.getBoolean("phoneCheck", false))
			phoneNotifier.setChecked(true);
		
		if(sPref.getInt("delay", 0) != 0)
			delay.setText(sPref.getInt("delay", 0)+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add("Save").setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						SharedPreferences userData = getSharedPreferences(
								PREF_NAME, 0);

						Editor editor = userData.edit();
						editor.putString("email", email.getText().toString());
						editor.putString("password", password.getText()
								.toString());
						editor.putString("phone", phoneNo.getText().toString());
						editor.putBoolean("emailCheck", emailNotifier.isChecked());
						editor.putBoolean("phoneCheck", phoneNotifier.isChecked());
						editor.putInt("delay", Integer.parseInt(delay.getText().toString()));
						editor.commit();
						Toast.makeText(getApplicationContext(), "Data Saved",
								Toast.LENGTH_SHORT).show();
						finish();						
						return true;
					}
				});
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
		startActivity(new Intent(this, HomeActivity.class));
	}
}
