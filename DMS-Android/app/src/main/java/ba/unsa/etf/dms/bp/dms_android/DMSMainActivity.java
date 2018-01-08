package ba.unsa.etf.dms.bp.dms_android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DMSMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent receiveIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmsmain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        receiveIntent = getIntent();

        if(receiveIntent != null) {

            TextView textViewUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUsername);
            String username = receiveIntent.getStringExtra("username");
            textViewUsername.setText(username);
        }

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dmsmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        Class fragmentClass = null;

        int id = item.getItemId();


        if (id == R.id.nav_my_documents) {
            bundle.putString("NavType","MyDocuments");
            fragmentClass = DocumentListFragment.class;
        } else if (id == R.id.nav_shared_documents) {
            bundle.putString("NavType","SharedDocuments");
            fragmentClass = DocumentListFragment.class;
        } else if (id == R.id.nav_add_document) {
            fragmentClass = AddDocument.class;
        } else if (id == R.id.nav_add_document) {
            fragmentClass = AddDocument.class;
        } else if (id == R.id.nav_korisnici) {

        } else if (id == R.id.nav_odjava) {

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if(id == R.id.nav_my_documents || id == R.id.nav_shared_documents) {
                fragment.setArguments(bundle);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getFragmentManager();

        //fragmentManager.beginTransaction().replace(R.id.content_dmsmain, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.content_dmsmain, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
