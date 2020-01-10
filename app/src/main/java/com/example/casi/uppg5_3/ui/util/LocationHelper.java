package com.example.casi.uppg5_3.ui.util;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.casi.uppg5_3.R;
import com.example.casi.uppg5_3.service.model.Player;
import com.example.casi.uppg5_3.service.repository.action.Action;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class LocationHelper {

    private static final String TAG = "LocationHelper";
    private Fragment fragment;
    private GoogleMap mMap;
    private String[] permissions;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 8000;
    private long SMALLEST_DISPLACEMENT = 20;
    private final float DEFAULT_ZOOM = 15f;
    private float zoom;
    public LatLng lastLocation;


    public LocationHelper(Fragment fragment, GoogleMap mMap) {
        this.fragment = fragment;
        this.mMap = mMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                return true;
            }
        });
        permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (!checkPermission()) {
            fragment.requestPermissions(permissions, 1234);
        }
        if (checkPermission()) {
            mMap.setMyLocationEnabled(false);
        }
        zoom = DEFAULT_ZOOM;
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        mLocationRequest.setNumUpdates(1);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(fragment.getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (checkPermission())
            LocationServices.getFusedLocationProviderClient(
                    fragment.getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location = locationResult.getLastLocation();
                            Log.d(TAG, "->onLocationResult(): " + location.toString());
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            lastLocation = latLng;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                            Action.uLocation(latLng);
                            Action.uPlayers();
                        }
                    },
                    Looper.myLooper());
    }


    public void cameraLastLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, zoom));
    }

    private boolean checkPermission() {
        boolean result = true;
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), perm) !=
                    PackageManager.PERMISSION_GRANTED) {
                result = false;
            }
        }
        return result;
    }

    public void updatePlayers(List<Player> players) {
        mMap.clear();
        try {
            for (Player p : players) {
                MarkerOptions options = new MarkerOptions()
                        .position(p.getLatLng())
                        .snippet(p.getName())
                        .title(p.getLatLng().toString())
                        .icon(p.getIcon(fragment.getActivity(), lastLocation));
                Marker marker = mMap.addMarker(options);
                options.anchor(0f, 0.5f);
                marker.showInfoWindow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//    MarkerOptions options = new MarkerOptions()
//            .position(p.getLatLng())
//            .snippet(p.getName())
//            .icon(p.getIcon(fragment.getActivity()));


//    public void cameraMyLocation(){
//        if(checkPermission())
//            LocationServices.getFusedLocationProviderClient(fragment.getActivity()).getLastLocation()
//                    .addOnSuccessListener(fragment.getActivity(), new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null){
//                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//                            }
//                        }
//                    });
//    }
