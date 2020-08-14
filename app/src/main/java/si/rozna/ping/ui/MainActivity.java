package si.rozna.ping.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Optional;

import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.auth.LoginActivity;
import si.rozna.ping.ui.drawer.groups.GroupsFragment;
import si.rozna.ping.ui.drawer.groups.NewGroupFragment;
import si.rozna.ping.ui.drawer.home.HomeFragment;
import si.rozna.ping.ui.drawer.profile.ProfileFragment;
import si.rozna.ping.ui.drawer.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private TextView mHeaderUsername;
    private TextView mHeaderEmail;

    private Fragment fragment;
    private Fragment prevFragment;

    enum LeaveAction {
        LEAVE_APPLICATION,
        LOGOUT
    }

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
            Log.e(TAG, "USER DOES NOT EXIST");
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
                showLogoutWarning(mDrawerLayout,
                        LeaveAction.LEAVE_APPLICATION,
                        getString(R.string.warning_leave_application));
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
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_logout:
                showLogoutWarning(mDrawerLayout,
                        LeaveAction.LOGOUT,
                        getString(R.string.warning_logout));
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

    private void showLogoutWarning(View view, LeaveAction leaveAction, String message){
        showLogoutWarning(view, leaveAction, message, null, null);
    }

    private void showLogoutWarning(View view, LeaveAction leaveAction, String message, String okBtnName, String cancelBtnName){

        // Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_general, null);


        // Create the popup window
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                true);

        // Set popup message
        if(message != null) {
            TextView info = popupView.findViewById(R.id.tv_popup_general_info);
            info.setText(message);
        }

        // Dismiss the popup window when touched
        Button cancelPopupBtn = popupView.findViewById(R.id.btn_popup_general_action_cancel);
        cancelPopupBtn.setOnClickListener(view1 -> popupWindow.dismiss());
        if(cancelBtnName != null)
            cancelPopupBtn.setText(cancelBtnName);

        // OK Button -> leave application but don't logout user
        Button okPopupBtn = popupView.findViewById(R.id.btn_popup_general_action_ok);
        okPopupBtn.setOnClickListener((view1) -> {
            popupWindow.dismiss();
            switch (leaveAction){
                case LEAVE_APPLICATION:
                    leaveApplication();
                    break;
                case LOGOUT:
                    logout();
                    break;
            }
        });
        if(okBtnName != null)
            okPopupBtn.setText(okBtnName);

        // Show popup
        // Which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }

    private void logout(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> startActivity(new Intent(getBaseContext(), LoginActivity.class)));
    }

    private void leaveApplication(){
        finish();
    }

}
