package com.example.casi.uppg5_3.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.casi.uppg5_3.HorrorApplication;
import com.example.casi.uppg5_3.R;
import com.example.casi.uppg5_3.ui.util.LocationHelper;
import com.example.casi.uppg5_3.viewmodel.MapViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";
    private MapView mapView;
    private GoogleMap mMap;
    public LocationHelper locationHelper;
    private MapViewModel viewModel;
    //private boolean loggedIn = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "->onRequestPermissionsResult()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) v.findViewById(R.id.myMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        viewModel = HorrorApplication.getAppComponent().getMapViewModel();

        //Observe playerlist
        viewModel.getPlayerList().observe(this, players -> {
            if (locationHelper != null)
                locationHelper.updatePlayers(players);
        });

        //Observe messages
        viewModel.getMessageObservable().observe(this, message -> {
            if (message.getMessage() != null)
                Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
        });

        //Observe login
        viewModel.getLoginStatusObservable().observe(this, login -> {
            //loggedIn = login.isLoggedin();
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        try {
            if (viewModel.getLoginStatusObservable().getValue().isLoggedin())
                locationHelper = new LocationHelper(this, mMap);
        }catch(Exception e){
            //Ignore
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
