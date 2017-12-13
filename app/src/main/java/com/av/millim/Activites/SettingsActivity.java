package com.av.millim.Activites;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.av.millim.R.id.map;
import static com.av.millim.R.id.thing_proto;
import static com.av.millim.Services.MyFirebaseMessagingService.getContext;

public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbarSettings;
    private RelativeLayout userSettings,merchantSettings;
    private EditText userName,userPassword;
    private EditText storeName,merchantName,merchantPassword;

    private String getUserName,getUserPassword;
    private String getStoreName,getMerchantName,getMerchantPassword;

    private ProgressDialog pDialog;
    private GoogleMap mMap;

    private MerchantLocation setMerchantLocation;

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

        String getTypeOfAccount = new StoreData(this).loadTypeOfContactTest();
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
        LatLng getLocation = new LatLng(Double.parseDouble(new StoreData(this).loadLatitude()),Double.parseDouble(new StoreData(this).loadLongitude() ));
        mMap.addMarker(new MarkerOptions().position(getLocation).title(new StoreData(this).loadStoreName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(getLocation)      // Sets the center of the map to Mountain View
                .zoom(25)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();

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

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
}
