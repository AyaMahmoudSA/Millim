package com.av.millim.Activites;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.av.millim.Data.ActivityUtil;
import com.av.millim.Data.StoreData;
import com.av.millim.Model.MerchantLocation;
import com.av.millim.ParseData.GetCallBack;
import com.av.millim.ParseData.ParseUtils;
import com.av.millim.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.av.millim.Activites.SignUpActivity.getTypeOfAccount;
import static com.av.millim.R.id.map;
import static com.av.millim.R.id.thing_proto;
import static com.av.millim.Services.MyFirebaseMessagingService.getContext;

public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Toolbar toolbarSettings;
    private RelativeLayout userSettings,merchantSettings;
    private EditText userName,userPassword;
    private EditText storeName,merchantName,merchantPassword;

    private String getUserName,getUserPassword;
    private String getStoreName,getMerchantName,getMerchantPassword;

    private ProgressDialog pDialog;
    private GoogleMap mMap;

    private MerchantLocation setMerchantLocation;
    private String getTypeOfAccount;

    private final int REQ_PERMISSION = 999;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private final int UPDATE_INTERVAL =  1 * 60 * 1000; // 3 minutes
    private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    private Marker locationMarker;  // to current location

    String getLatitude;
    String getLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSettings = (Toolbar) findViewById(R.id.toolbar_settings);
        toolbarSettings.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow_blue));
        setSupportActionBar(toolbarSettings);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // to back
        getSupportActionBar().setDisplayShowTitleEnabled(false); // remove label name (title)

        Setup_UI();


    }

    private void Setup_UI() {
        userSettings = (RelativeLayout) findViewById(R.id.user_settings);
        merchantSettings = (RelativeLayout) findViewById(R.id.merchant_settings);

        userName   = (EditText) findViewById(R.id.et_user_name);
        userPassword   = (EditText) findViewById(R.id.et_user_password);

        storeName          =  (EditText) findViewById(R.id.et_store_name);
        merchantName       = (EditText) findViewById(R.id.et_merchant_name);
        merchantPassword   = (EditText) findViewById(R.id.et_merchant_password);

        getTypeOfAccount = new StoreData(this).loadTypeOfContactTest();
        if(getTypeOfAccount.equals("1")){
            merchantSettings.setVisibility(View.VISIBLE);
            userSettings.setVisibility(View.GONE);
        }else{
            userSettings.setVisibility(View.VISIBLE);
            merchantSettings.setVisibility(View.GONE);
        }



        userName.setText(new StoreData(this).loadUserName());
        userPassword.setText(new StoreData(this).loadPinCode());


        storeName.setText(new StoreData(this).loadStoreName());
        merchantName.setText(new StoreData(this).loadUserName());
        merchantPassword.setText(new StoreData(this).loadPinCode());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

               getLatitude  = new StoreData(this).loadLatitude();
               getLongitude = new StoreData(this).loadLongitude();

                 // merchant does not put location
                 // get current
                if(getTypeOfAccount.equals("1")&getLatitude.matches("")&&getLongitude.matches("")){
                    // Create an instance of GoogleAPIClient.
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
                    }
                }



    }


    public  boolean checkDataUser(){
        if(TextUtils.isEmpty(getUserName)&&TextUtils.isEmpty(getUserPassword)){
             userName.setError("Empty");
             userPassword.setError("Empty");
             return false;
        }else if(TextUtils.isEmpty(getUserName)){
            userName.setError("Empty");
            return false;
        }else if(TextUtils.isEmpty(getUserPassword)){
            userPassword.setError("Empty");
            return false;
        }else if(getUserPassword.length()<3){
            userPassword.setError(getResources().getString(R.string.error2));
            return false;
        }
        return  true;
    }

    public void saveUserSettings(View view) {
        getUserName     = userName.getText().toString();
        getUserPassword = userPassword.getText().toString();
        boolean isCompleted = checkDataUser();
        if(isCompleted){

            final AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Confirm Data");
            alertDialog.setMessage("Saving data?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            ParseUtils.UpdateUser(new GetCallBack.GetSignUpUserCallBack() {
                                @Override
                                public void onSuccess(JSONObject jsonObject, String success) {
                                    try {

                                        Toast.makeText(SettingsActivity.this,"Data Saved.",Toast.LENGTH_SHORT).show();
                                        new StoreData(SettingsActivity.this).saveUserName(jsonObject.getString("Name"));
                                        new StoreData(SettingsActivity.this).savePinCode(jsonObject.getString("Pin_Code"));
                                        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(String throwable) {

                                }

                                @Override
                                public void onNetworkFailure() {

                                }
                            },getUserName,Integer.parseInt(new StoreData(SettingsActivity.this).loadPhoneUser()),Integer.parseInt(getUserPassword));


                        }});

            alertDialog.show();

        }

    }


    public  boolean checkMerchantUser(){
        if(TextUtils.isEmpty(getStoreName)&&TextUtils.isEmpty(getMerchantName)&&TextUtils.isEmpty(getMerchantPassword)){
            storeName.setError("Empty");
            merchantName.setError("Empty");
            merchantPassword.setError("Empty");
            return false;
        }else if(TextUtils.isEmpty(getStoreName)){
            storeName.setError("Empty");
            return false;
        }else if(TextUtils.isEmpty(getMerchantName)){
            merchantName.setError("Empty");
            return false;
        }else if(TextUtils.isEmpty(getMerchantPassword)){
            merchantPassword.setError("Empty");
            return false;
        }else if(getMerchantPassword.length()<4){
            merchantPassword.setError(getResources().getString(R.string.error2));
            return false;
        }
        return  true;
    }

    public void saveMerchantSettings(View view) {
        getStoreName        = storeName.getText().toString();
        getMerchantName     = merchantName.getText().toString();
        getMerchantPassword = merchantPassword.getText().toString();

        boolean isCompleted = checkMerchantUser();
        if(isCompleted) {
            final AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Confirm Data");
            alertDialog.setMessage("Saving data?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            ParseUtils.UpdateMerchant(new GetCallBack.GetSignUpUserCallBack() {
                                @Override
                                public void onSuccess(JSONObject jsonObject, String success) {
                                    try {
                                        Toast.makeText(SettingsActivity.this, "Data Saved.", Toast.LENGTH_SHORT).show();

                                        new StoreData(SettingsActivity.this).saveStoreName(jsonObject.getString("Store_Name"));
                                        new StoreData(SettingsActivity.this).savePinCode(jsonObject.getString("Pin_Code"));
                                        new StoreData(SettingsActivity.this).saveUserName(jsonObject.getString("Name"));

                                        new StoreData(SettingsActivity.this).saveLongitude(setMerchantLocation.getLongitude());
                                        new StoreData(SettingsActivity.this).saveLongitude(setMerchantLocation.getLatitude());

                                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(String throwable) {

                                }

                                @Override
                                public void onNetworkFailure() {

                                }
                            }, getMerchantName, Integer.parseInt(new StoreData(SettingsActivity.this).loadPhoneMerchant()), Integer.parseInt(getMerchantPassword),getStoreName,setMerchantLocation);


                        }
                    });

            alertDialog.show();

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;

        if(getTypeOfAccount.equals("1")){

              if(!getLatitude.matches("")&&!getLatitude.matches("")){
                  LatLng getLocation = new LatLng(Double.parseDouble(getLatitude),Double.parseDouble(getLongitude));
                  mMap.addMarker(new MarkerOptions().position(getLocation).title(new StoreData(this).loadStoreName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));
                  float zoom = 20f;
                  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(getLocation, zoom);
                  mMap.animateCamera(cameraUpdate);

                  setMerchantLocation(getLocation);

                  mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                      @Override
                      public void onMapClick(LatLng point) {
                          // TODO Auto-generated method stub
                          //  lstLatLngs.add(point);
                          mMap.clear();
                          mMap.addMarker(new MarkerOptions().position(point).title(new StoreData(SettingsActivity.this).loadStoreName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));

                          setMerchantLocation(point);

                      }
                  });

              }


        }


    }


    public MerchantLocation setMerchantLocation(LatLng latLng){
       setMerchantLocation = new MerchantLocation();

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address>    listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);



            if(null!=listAddresses&&listAddresses.size()>0) {


                String countryName = listAddresses.get(0).getCountryName(); //country
                String countryAdminArea = listAddresses.get(0).getAdminArea();//city
                String subAdminArea = listAddresses.get(0).getSubAdminArea();//region

                setMerchantLocation.setLongitude(String.valueOf(latLng.longitude));
                setMerchantLocation.setLatitude(String.valueOf(latLng.latitude));
                setMerchantLocation.setCountry(countryName);
                setMerchantLocation.setCity(countryAdminArea);
                setMerchantLocation.setRegion(subAdminArea);

//                Toast.makeText(SettingsActivity.this,countryName+countryAdminArea+subAdminArea,Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  setMerchantLocation;
    }

    @Override
    protected void onStart() {

        if(getTypeOfAccount.equals("1")&getLatitude.matches("")&&getLongitude.matches(""))
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        if(getTypeOfAccount.equals("1")&getLatitude.matches("")&&getLongitude.matches(""))
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        writeActualLocation(location);
    }


    // Get last known location
    private void getLastKnownLocation() {
        //   Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if ( lastLocation != null ) {
            /*  //  Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());*/
                startLocationUpdates();
            } else {
                //   Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }


    // Check for permission to access Location
    private boolean checkPermission() {
        //   Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        //  Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Start location Updates
    private void startLocationUpdates(){
        //  Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    private void markerLocation(LatLng latLng) {
        //  Log.i(TAG, "markerLocation("+latLng+")");
        String title ="Your current location";
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map));
        if ( mMap!=null ) {
            // Remove the anterior marker
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);

            setMerchantLocation(latLng);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    // TODO Auto-generated method stub
                    //  lstLatLngs.add(point);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(point).title(new StoreData(SettingsActivity.this).loadStoreName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));

                    setMerchantLocation(point);

                }
            });




        }
    }


    // Write location coordinates on UI
    private void writeActualLocation(Location location) {
        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    }

}
