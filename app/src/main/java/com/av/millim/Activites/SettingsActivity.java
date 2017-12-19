package com.av.millim.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.av.millim.Activites.SignUpActivity.getTypeOfAccount;
import static com.av.millim.R.id.latitudeTextView;
import static com.av.millim.R.id.longitudeTextView;
import static com.av.millim.R.id.map;
import static com.av.millim.R.id.thing_proto;
import static com.av.millim.Services.MyFirebaseMessagingService.getContext;

public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener  {

    private Toolbar toolbarSettings;
    private RelativeLayout userSettings,merchantSettings;
    private EditText userName,userPassword;
    private EditText storeName,merchantName,merchantPassword;

    private String getUserName,getUserPassword;
    private String getStoreName,getMerchantName,getMerchantPassword;

    private GoogleMap mMap;

    private MerchantLocation setMerchantLocation;
    private String getTypeOfAccount;

    private Location myLocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;


    String getLatitude;
    String getLongitude;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        showProgressDialog();

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

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);

            storeName.setText(new StoreData(this).loadStoreName());
            merchantName.setText(new StoreData(this).loadUserName());
            merchantPassword.setText(new StoreData(this).loadPinCode());

            getLatitude  = new StoreData(this).loadLatitude();
            getLongitude = new StoreData(this).loadLongitude();

            hideProgressDialog();

        }else{

            userSettings.setVisibility(View.VISIBLE);
            merchantSettings.setVisibility(View.GONE);

            userName.setText(new StoreData(this).loadUserName());
            userPassword.setText(new StoreData(this).loadPinCode());
            hideProgressDialog();

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


    public void updateLocationWithCurrentLocation(View view) {
        if(googleApiClient==null){
            setUpGClient();
        }else{
            checkPermissions();
        }
    }

    private synchronized void setUpGClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (myLocation != null) {

            Double latitude=myLocation.getLatitude();
            Double longitude=myLocation.getLongitude();
            mMap.clear();

            LatLng getLocation = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(getLocation).title("Your current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));
            float zoom = 20f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(getLocation, zoom);
            mMap.animateCamera(cameraUpdate);
            setMerchantLocation(getLocation);


          /*  latitudeTextView.setText("Latitude : "+latitude);
            longitudeTextView.setText("Longitude : "+longitude);*/
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected())
            {
                int permissionLocation = ContextCompat.checkSelfPermission(SettingsActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    myLocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat.checkSelfPermission(SettingsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED)
                                    {
                                        myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try
                                    {
                                        status.startResolutionForResult(SettingsActivity.this,REQUEST_CHECK_SETTINGS_GPS);
                                    }
                                    catch (IntentSender.SendIntentException e)
                                    {
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:


                        break;
                }
                break;
        }
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(SettingsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(SettingsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
}
