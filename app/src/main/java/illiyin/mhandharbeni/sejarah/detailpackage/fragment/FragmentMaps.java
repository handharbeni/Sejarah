package illiyin.mhandharbeni.sejarah.detailpackage.fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import illiyin.mhandharbeni.libraryroute.Navigation;
import illiyin.mhandharbeni.sejarah.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by root on 2/10/18.
 */

public class FragmentMaps extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;

    private View v;
    private MapView mMapView;
    private GoogleMap googleMaps;
    private GoogleApiClient googleApiClient;
    private int REQUEST_CHECK_SETTINGS = 123;
    String[] permissions = new String[]{
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.STORAGE
    };

    private Double lat, lng;
    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        return v;
    }

    private void fetch_extras() {
        Bundle args = getArguments();
        lat = args.getDouble(LATITUDE);
        lng = args.getDouble(LONGITUDE);

        Log.d(TAG, "fetch_extras: Lat "+lat);
        Log.d(TAG, "fetch_extras: Long "+lng);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_extras();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        checkGps();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        requestUpdate();
        final LatLng[] start = {null};
        this.googleMaps = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        this.googleMaps.setMyLocationEnabled(true);
        this.googleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                start[0] = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
//        if (this.googleMaps.isMyLocationEnabled()){
//            LatLng end = new LatLng(lat, lng);
//            buildRoute(googleMaps, start[0], end);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    private void checkGps() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        assert manager != null;
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity().getApplicationContext())) {
//            Toast.makeText(NavActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }

        if (!hasGPSDevice(getActivity().getApplicationContext())) {
            Toast.makeText(getActivity().getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity().getApplicationContext())) {
            enableLoc();
        }/* else {
            Toast.makeText(getActivity().getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
        }*/
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (mgr == null)
            return false;

        final List<String> providers = mgr.getAllProviders();
        return providers != null && providers.contains(LocationManager.GPS_PROVIDER);

    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .build();

            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);
            builder.setNeedBle(true);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        //                    FirebaseCrash.report(exception);
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            getActivity(),
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException | ClassCastException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }

            });
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void buildRoute(GoogleMap mMap, LatLng start, LatLng end) {
        Navigation nav = new Navigation(mMap, start, end, getActivity().getApplicationContext(), getActivity());
        nav.find(
                true,
                false,
                bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_place_start),
                bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_place_end)
        );
    }

    private void requestUpdate() {
        initializeLocationManager();
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                LOCATION_INTERVAL,
                LOCATION_DISTANCE,
                mLocationListeners[0]
        );
    }
    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            if (googleMaps.isMyLocationEnabled()){
                googleMaps.clear();
                LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng end = new LatLng(lat, lng);
                buildRoute(googleMaps, start, end);
            }
//            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
