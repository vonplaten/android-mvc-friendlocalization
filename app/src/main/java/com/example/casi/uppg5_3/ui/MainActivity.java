package com.example.casi.uppg5_3.ui;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.casi.uppg5_3.HorrorApplication;
import com.example.casi.uppg5_3.R;
import com.example.casi.uppg5_3.service.model.FragmentPicker;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.repository.action.Action;
import com.example.casi.uppg5_3.viewmodel.MainViewModel;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MapFragment map_fragment;
    private LoginFragment login_fragment;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set View
        setContentView(R.layout.activity_main);

        //Toolbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));


        //DEFAULT FRAGMENT
        viewFragment(Fragcode.MAP_FRAGMENT);

        //Inject viewmodel
        viewModel = HorrorApplication.getAppComponent().getMainViewModel();

        //Observe serverMessages
        viewModel.getFragmentPickerObservable().observe(this, fragmentPicker -> {
            if (fragmentPicker.getPick().equals(FragmentPicker.PickCode.LOGIN_FRAGMENT)) {
                viewFragment(Fragcode.LOGIN_FRAGMENT);

                Toast toast = Toast.makeText(this, "You must be logged in to play", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 350);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);
                toast.show();
            }
        });
    }


    /////////////// MENU /////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_connect:
                viewFragment(Fragcode.LOGIN_FRAGMENT);
                break;
            case R.id.menuitem_horror:
                viewFragment(Fragcode.MAP_FRAGMENT);
                break;
            case R.id.menuitem_human:
//                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
//                String username = mSettings.getString("username", "anonym");
                Action.uTurn(Player.Type.HUMAN);
                //Action.sPlayer(Player.Type.HUMAN, username, map_fragment.locationHelper.lastLocation);
                break;
            case R.id.menuitem_zombie:
//                SharedPreferences mSettings2 = PreferenceManager.getDefaultSharedPreferences(this);
//                String username2 = mSettings2.getString("username", "anonym");
                Action.uTurn(Player.Type.ZOMBIE);
                //Action.sPlayer(Player.Type.ZOMBIE, username2, map_fragment.locationHelper.lastLocation);
                break;
            case R.id.menuitem_update:
                try {
                    map_fragment.locationHelper.cameraLastLocation();
                } catch (java.lang.NullPointerException e) {
                 //Ignore
                }
                break;
        }
        return true;
    }
    private enum Fragcode {
        MAP_FRAGMENT,
        LOGIN_FRAGMENT
    }
    private void viewFragment(Fragcode f) {
        FragmentManager manager = getSupportFragmentManager();
        switch (f) {
            case MAP_FRAGMENT:
                map_fragment = new MapFragment();
                manager.beginTransaction()
                        .replace(R.id.fragmentHolder, map_fragment, map_fragment.getTag())
                        .commit();
                break;
            case LOGIN_FRAGMENT:
                login_fragment = new LoginFragment();
                manager.beginTransaction()
                        .replace(R.id.fragmentHolder, login_fragment, login_fragment.getTag())
                        .commit();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "->onRequestPermissionsResult()");
    }



}
