package si.rozna.ping.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Optional;

import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.ui.components.GeneralPopupComponent;
import si.rozna.ping.ui.drawer.groups.GroupsFragment;
import si.rozna.ping.ui.drawer.groups.NewGroupFragment;
import si.rozna.ping.ui.drawer.home.HomeFragment;
import si.rozna.ping.ui.drawer.invites.InvitesFragment;
import si.rozna.ping.ui.drawer.profile.ProfileFragment;
import si.rozna.ping.ui.drawer.settings.SettingsFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView mHeaderUsername;
    private TextView mHeaderEmail;

    private Fragment fragment;
    private Fragment prevFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Header components
        NavigationView nv = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_view);
        View header = nv.getHeaderView(0);
        mHeaderUsername = header.findViewById(R.id.nav_header_username);
        mHeaderEmail = header.findViewById(R.id.nav_header_email);

        // Drawer toggle (Hamburger icon)
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Navigation view (menu in drawer)
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Switches to appropriate fragment (screen)
                switchFragment(item.getItemId());

                // Closes drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);

                // Sets item as selected
                navigationView.setCheckedItem(item);
                return false;
            }
        });

        // Sets fragment to be shown when application is run for the first time
        if(savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            switchFragment(R.id.nav_home);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        
        Optional<FirebaseUser> user = AuthService.getCurrentUser();
        if(user.isPresent()) {

            String username = AuthService.getCurrentUserDisplayName().orElse("Welcome");
            String email = AuthService.getCurrentUserEmail().orElse("anon@mail.com");

            mHeaderUsername.setText(username);
            mHeaderEmail.setText(email);

        } else {
            Timber.e("USER DOES NOT EXIST");
            // Logout user
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        System.out.println(item.getItemId());

        switch (item.getItemId()) {
            // Hamburger icon selected
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                else
                    mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        // Back button first closes drawer.
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof NewGroupFragment){
                switchFragment(this.prevFragment);
            }else {
                new GeneralPopupComponent(
                        this,
                        mDrawerLayout,
                        GeneralPopupComponent.Action.LEAVE_APPLICATION,
                        getString(R.string.warning_leave_application)
                ).show();
            }
        }
    }

    /*
    * Used to switch fragments that are not part of drawer layout menu
    * */
    public void switchFragment(Fragment fragment){

        this.prevFragment = this.fragment;
        this.fragment = fragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void switchFragment(int itemId){

        this.prevFragment = this.fragment;

        switch (itemId){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_groups:
                fragment = new GroupsFragment();
                break;
            case R.id.nav_invites:
                fragment = new InvitesFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_logout:
                new GeneralPopupComponent(
                        this,
                        mDrawerLayout,
                        GeneralPopupComponent.Action.LOGOUT,
                        getString(R.string.warning_logout)
                ).show();
                break;
            default:
                fragment = new HomeFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    public void hideSoftKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

}
