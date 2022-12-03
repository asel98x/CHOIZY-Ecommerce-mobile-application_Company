package lk.choizy.company.Company;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Iterator;

import lk.choizy.company.R;

public class location_picker extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private PlacesClient mPlacesClient;
    private SupportMapFragment mapFragment;
    locationSelected listener;
    private LatLng newLocation;
    private Button selectLocationBtn;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION, COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = LayoutInflater.from(getContext()).inflate(R.layout.activity_location_picker,container,false);

        selectLocationBtn = view.findViewById(R.id.locationChoose);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Iterator<Boolean> iterator = result.values().iterator();

                    while (iterator.hasNext()) {
                        boolean granted = iterator.next();
                        if (!granted) {
                            Toast.makeText(getContext(), "Permission failed", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            if (!iterator.hasNext()) {
                                getCurrentLocation();
                            }
                        }
                    }
                });

        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(new String[]{FINE_LOCATION, COARSE_LOCATION});
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.map42, mapFragment)
                    .commit();
            mapFragment.getMapAsync(this);
        }
        selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!= null){
                    if(newLocation!=null){
                        listener.locationSelected(newLocation);
                        dismiss();
                    }
                }
            }
        });



        return view;
    }

    public void setListener(locationSelected listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {


        super.onDestroyView();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);



        getCurrentLocation();
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        com.google.android.gms.tasks.Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(Location location) {
                if(location == null){
                    Toast.makeText(getContext(), "Location Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My location");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                Marker markerOptions = mMap.addMarker(new MarkerOptions().position(latLng));

                mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        LatLng midLatLng = mMap.getCameraPosition().target;
                        if (markerOptions!=null) {
                            markerOptions.setPosition(midLatLng);
                            newLocation = markerOptions.getPosition();
                        }
                    }
                });
                //  map.addMarker(markerOptions);
            }
        });


    }

    public interface locationSelected{
        void locationSelected(LatLng latLng);
    }

    @Override
    public void onResume() {
        if(mapFragment != null){
            mapFragment.onResume();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        if(mapFragment != null){
            mapFragment.onPause();

        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mapFragment != null){
            mapFragment.onDestroy();
        }

        super.onDestroy();
    }
}