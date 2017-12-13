package com.av.millim.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.av.millim.Activites.LoginActivity;
import com.av.millim.Activites.MainActivity;
import com.av.millim.Model.Contact;
import com.av.millim.Model.Transactions;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.R.id.edit;
import static com.av.millim.Data.CONSTANTS.deviceToken;
import static com.av.millim.Data.CONSTANTS.filePath1;
import static com.av.millim.Data.CONSTANTS.filePath2;

/**
 * Created by Maiada on 10/26/2017.
 */

public class StoreData {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String DATABASE_NAME = "com.av.millim";
    private Context context;
    ArrayList<Contact> yourData = new ArrayList<Contact>();


    public StoreData(Context context) {
        super();
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserId", userId);
        editor.commit();
    }

    public String loadUserId() {
        String userId = sharedPreferences.getString("UserId", "");
        return userId;
    }

    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", userName);
        editor.commit();
    }

    public String loadUserName() {
        String userName = sharedPreferences.getString("UserName", "");
        return userName;
    }

    public void savePinCode(String pinCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PinCode", pinCode);
        editor.commit();
    }

    public String loadPinCode() {
        String pinCode = sharedPreferences.getString("PinCode", "");
        return pinCode;
    }

    public void saveBalance(String balance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Balance", balance);
        editor.commit();

    }

    public String loadBalance() {
        String balance = sharedPreferences.getString("Balance", "");
        return balance;
    }

    public void savePhoneUser(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneUser", phone);
        editor.commit();
    }

    public String loadPhoneUser() {
        String phoneUser = sharedPreferences.getString("PhoneUser", "");
        return phoneUser;
    }

    public void saveStoreName(String storeName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("StoreName", storeName);
        editor.commit();
    }

    public String loadStoreName(){
        String loadStoreName = sharedPreferences.getString("StoreName", "");
        return loadStoreName;
    }

    public void saveDeviceToken(String deviceToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DeviceToken", deviceToken);
        editor.commit();
    }

    public String loadDeviceToken() {
        String loadDeviceToken = sharedPreferences.getString("DeviceToken", "");
        return loadDeviceToken;
    }

    public void saveContactList(ArrayList<Contact> getContactList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String dataStr = new Gson().toJson(getContactList);
        editor.putString("ContactList", dataStr);
        editor.commit();

    }

    public String loadContactList() {
        String loadContactList = sharedPreferences.getString("ContactList", "");
        return loadContactList;
    }


    public void saveTypeOfContactTest(String typeOfContact) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TypeOfContact", typeOfContact);
        editor.commit();
    }

    public String loadTypeOfContactTest() {
        String typeOfContact = sharedPreferences.getString("TypeOfContact", "");
        return typeOfContact;

    }
    public void saveTypeOfContactSign(String typeOfContact) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TypeOfContactSign", typeOfContact);
        editor.commit();
    }

    public String loadTypeOfContactSign() {
        String typeOfContact = sharedPreferences.getString("TypeOfContactSign", "");
        return typeOfContact;
    }

    public void saveMerchantId(String merchantId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MerchantId", merchantId);
        editor.commit();
    }

    public String loadMerchantId() {
        String merchantId = sharedPreferences.getString("MerchantId", "");
        return merchantId;
    }

    public void saveFilePath1(String filePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("File_Path1", filePath);
        editor.commit();
    }

    public String loadFilePath1() {
        String filePath1 = sharedPreferences.getString("File_Path1", "");
        return filePath1;
    }
    public void saveFilePath2(String filePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("File_Path2", filePath);
        editor.commit();
    }

    public String loadFilePath2() {
        String filePath2 = sharedPreferences.getString("File_Path2", "");
        return filePath2;
    }

    public void saveUploadImage(String filePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UploadImage", filePath);
        editor.commit();
    }

    public String loadUploadImage() {
        String uploadImage = sharedPreferences.getString("UploadImage", "");
        return uploadImage;
    }
    public void saveUploadFile(String filePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UploadFile", filePath);
        editor.commit();
    }

    public String loadUploadFile() {
        String uploadFile = sharedPreferences.getString("UploadFile", "");
        return uploadFile;
    }
    public void savePhoneMerchant(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneMerchant", phone);
        editor.commit();
    }

    public String loadPhoneMerchant() {
        String phoneMerchant = sharedPreferences.getString("PhoneMerchant", "");
        return phoneMerchant;
    }


    public void saveTransactionList(ArrayList<Transactions> getContactList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String dataStr = new Gson().toJson(getContactList);
        editor.putString("TransactionList", dataStr);
        editor.commit();

    }

    public String loadTransactionList() {
        String loadContactList = sharedPreferences.getString("TransactionList", "");
        return loadContactList;
    }


    public  void saveLongitude(String saveLongitude){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Longitude", saveLongitude);
        editor.commit();
    }

    public  String loadLongitude(){
        String loadLongitude = sharedPreferences.getString("Longitude", "");
        return loadLongitude;

    }

    public  void saveLatitude(String saveLatitude){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Latitude", saveLatitude);
        editor.commit();
    }

    public  String loadLatitude(){
        String loadLatitude = sharedPreferences.getString("Latitude", "");
        return loadLatitude;

    }

}