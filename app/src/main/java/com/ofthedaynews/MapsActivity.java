package com.ofthedaynews;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM=15f;
    private static final String TAG = "MapActivity";
    private ImageView mGps, maptype, zoomIn, zoomOut;
    private LocationRequest locationRequest;
    private Marker currentLocationMarker;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private Location lastLocation;
    private Button btnSearch;
    private EditText msearchText;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    List<Address> addressList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnSearch = (Button) findViewById(R.id.btn_mapSearch);
        maptype = (ImageView) findViewById(R.id.imMaptype);
        zoomIn = (ImageView) findViewById(R.id.imZoomin);
        zoomOut = (ImageView) findViewById(R.id.imZoomout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (savedInstanceState != null) {
//            CameraUpdate cameraPos = CameraUpdateFactory.newCameraPosition(
//                    (CameraPosition)(savedInstanceState.getParcelable(KEY_CAMERA_POSITION)));
//            mMap.moveCamera(cameraPos);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "Request permission :called");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Request permission 1:called");
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Request permission 2:called");
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
                }
                return;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        Log.d("lat = ", "" + latitude);
//        moveCamera(new LatLng(location.getLatitude(),location.getLongitude()),
//                DEFAULT_ZOOM,"My Location");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
        currentLocationMarker.showInfoWindow();
    }

    private void moveCamera(LatLng latLng,float zoom,String title){
        Log.d(TAG, " Move Camera lat:"+latLng.latitude +",lng: "+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            }
            return false;
        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onClick(View view) {
        Object dataTransfer[] = new Object[2];
        NearByPlaces nearByPlaces = new NearByPlaces();

        switch (view.getId()) {
            case R.id.btn_mapSearch: {
                msearchText = (EditText) findViewById(R.id.input_search);
                String searchString = msearchText.getText().toString();
               // List<Address> addressList;
                MarkerOptions markerOptions = new MarkerOptions();
                if (!searchString.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(searchString, 5);
                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address myAddress = addressList.get(i);
                                moveCamera(new LatLng(myAddress.getLatitude(),myAddress.getLongitude()),DEFAULT_ZOOM,
                                        myAddress.getAddressLine(0));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

            case R.id.btnHospital:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                nearByPlaces.execute(dataTransfer);
               // Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnRestaurant:
                mMap.clear();
                String restaurant = "Restaurant";
                url = getUrl(latitude, longitude, restaurant);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                nearByPlaces.execute(dataTransfer);
               // Toast.makeText(MapsActivity.this, "Showing Nearby Restaurant", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnSchool:

                        mMap.clear();
                        String School = "school";
                        url = getUrl(latitude, longitude, School);
                        dataTransfer[0] = mMap;
                        dataTransfer[1] = url;
                        Log.d("onClick", url);
                        nearByPlaces.execute(dataTransfer);
                       // Toast.makeText(MapsActivity.this,"Nearby Schools", Toast.LENGTH_LONG).show();

                break;

            case R.id.imMaptype:
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;

            case R.id.imZoomin:
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;

            case R.id.imZoomout:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyplaces) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyplaces);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyD1r7RePlVIpkO6NmQVVOOZbETTkGblUjc");
        Log.d("MAPSACTIVITY", "URL" + googlePlaceUrl.toString());
        Toast.makeText(MapsActivity.this,nearbyplaces, Toast.LENGTH_LONG).show();
        return googlePlaceUrl.toString();

    }






}
