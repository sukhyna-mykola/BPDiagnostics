package com.example.bpdiagnostics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bpdiagnostics.fragments.AboutFragment;
import com.example.bpdiagnostics.fragments.MeasureFragment;
import com.example.bpdiagnostics.fragments.UserSearchFragment;
import com.example.bpdiagnostics.fragments.UserStatisticsFragment;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.helpers.PreferencesManager;
import com.example.bpdiagnostics.utils.Constants;

public class MainActivity extends AppCompatActivity implements OnFragmentSearchLisntener {

    private static final String ABOUT = "ABOUT";
    private static final String INFO = "INFO";
    private static final String SEARCH = "SEARCH";
    private static final String MEASURE = "MESURE";
    private TabLayout tabLayout;
    private FragmentManager fragmentManager;

    private DBManager manager;


    private long id;

    public static final int REQUEST_CODE = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fragmentManager = getSupportFragmentManager();

        manager = DBManager.getInstance(this);


        id = PreferencesManager.getInstance(this).readUserId();

        configureViews(id);

    }

    private void configureViews(long id) {
        if (id == -1) {
            startActivityForResult(new Intent(this, RegistrationActivity.class), REQUEST_CODE);
        } else {
            int doctor = manager.getDoctorStatusByEmailAndPassword(id);

            if (doctor != -1) {
                if (doctor == Constants.DOCTOR_YES) {
                    configureDoctorTabs();
                } else {
                    configureUserTabs();
                }
            }
        }
    }


    private void configureDoctorTabs() {

        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Пошук").setTag(SEARCH));
        tabLayout.addTab(tabLayout.newTab().setText("Про програму").setTag(ABOUT));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag().equals(SEARCH)) {
                    addFragment(UserSearchFragment.newInstance());
                }
                if (tab.getTag().equals(ABOUT)) {
                    addFragment(AboutFragment.newInstance());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addFragment(UserSearchFragment.newInstance());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void addFragment(Fragment newFragment) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = newFragment;
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, newFragment).commitAllowingStateLoss();
        }

    }

    private void addStatisticsFragment(long id) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        Fragment newFragment = UserStatisticsFragment.newInstance(id);
        if (fragment == null) {
            fragment = newFragment;
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack("").commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, newFragment).addToBackStack("").commitAllowingStateLoss();
        }

    }


    private void configureUserTabs() {
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Занесення даних").setTag(MEASURE));
        tabLayout.addTab(tabLayout.newTab().setText("Інформація про стан").setTag(INFO));
        tabLayout.addTab(tabLayout.newTab().setText("Про програму").setTag(ABOUT));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getTag().equals(MEASURE)) {
                    addFragment(MeasureFragment.newInstance(id));

                }
                if (tab.getTag().equals(INFO)) {
                    addFragment(UserStatisticsFragment.newInstance(id));
                }
                if (tab.getTag().equals(ABOUT)) {
                    addFragment(AboutFragment.newInstance());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        addFragment(MeasureFragment.newInstance(id));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data.getExtras() != null) {
                    id = data.getLongExtra(PreferencesManager.KEY_ID, -1);
                    configureViews(id);
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exit) {
           // PreferencesManager.getInstance(this).saveUserId(-1);
            startActivityForResult(new Intent(this, RegistrationActivity.class), REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUserFragment(long id) {
        addStatisticsFragment(id);
    }
}
