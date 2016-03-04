package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.Server.camerapreview.R;

/**
 * Created by plaix on 3/4/16.
 */
public class BaseActivity extends Activity{
    protected DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;
    private static boolean isLaunch = true;
//    private ImageView ad;
    String[] androidVersionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = drawerTitle = getTitle();

        androidVersionArray = new String[] { "Feed", "Modules", "Tags", "Memories", "Capture", "Settings" };

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.drawerList);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                androidVersionArray));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view){
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view){
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        if(isLaunch){
            isLaunch = false;
            selectItem(0);
        }

//        ad = (ImageView) findViewById(R.id.ad);
//        ad.setImageResource(R.drawable.ad);
//        if(savedInstanceState == null){
//            selectItem(0);
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }

    }

    protected void selectItem(int position){
        Intent launch;
        switch (position){
            //home
            case 0:
                launch = new Intent(BaseActivity.this, HomeActivity.class);
                launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launch);
                break;
            //modules
            case 1:
                launch = new Intent(BaseActivity.this, ModulesActivity.class);
                startActivity(launch);
                break;
            //tags
            case 2:
                launch = new Intent(BaseActivity.this, ReviewActivity.class);
                startActivity(launch);
                break;
            //memories
            case 3:
                launch = new Intent(BaseActivity.this, ReviewActivity.class);
                startActivity(launch);
                break;
            //capture
            case 4:
                launch = new Intent(BaseActivity.this, CaptureActivity.class);
                startActivity(launch);
                break;
            //settings
            case 5:
                launch = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(launch);
                break;
            default:
                break;
        }
//        // Update Title on action bar
        drawerList.setItemChecked(position, true);
        setTitle(androidVersionArray[position]);
        drawerLayout.closeDrawer(drawerList);


    }

    @Override
    public void setTitle(CharSequence title_) {
        title = title_;
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}

