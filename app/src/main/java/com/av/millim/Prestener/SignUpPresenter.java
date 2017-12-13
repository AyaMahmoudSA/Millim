package com.av.millim.Prestener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.av.millim.Activites.SignUpActivity;
import com.av.millim.Data.ActivityUtil;
import com.av.millim.Data.CONSTANTS;
import com.av.millim.Data.StoreData;
import com.av.millim.Model.MerchantLocation;
import com.av.millim.ParseData.GetCallBack;
import com.av.millim.ParseData.ParseUtils;
import com.av.millim.R;
import com.av.millim.View.SignUpView;

import org.json.JSONObject;

import java.util.Random;

import static com.av.millim.Activites.SignUpActivity.getTypeOfAccount;

/**
 * Created by Maiada on 10/30/2017.
 */

public class SignUpPresenter {

    private static  SignUpView signUpView;
    private static ProgressDialog progressDialog;
    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
    }
    private static Context getContext;
    private  String  verificationCodeGenerator;
    public void onSignUpButtonClicked(Context context){
        this.getContext = context;
        String getType = new StoreData(getContext).loadTypeOfContactSign();
        if(getType.equals("1")){

// signUp For Merchant
            getMerchantSignUpDetails();

        }else{
// signUp For User
            getUserSignUpDetails();

        }

    }

    private void getMerchantSignUpDetails() {
     //   signUpView.getCurrentLocation();
        boolean checkDataMerchant = checkDataMerchant();
        if(checkDataMerchant){
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Is this your number?");
            alertDialog.setMessage("+2"+signUpView.getMobileOrAccountNumberMerchant());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            progressDialog = new ProgressDialog(getContext);
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            showProgressDialog();


                            final MerchantLocation getMerchantLocation = signUpView.getLocationData();

                            if(getMerchantLocation!=null){

                                ParseUtils.SignUpMerchant(new GetCallBack.GetSignUpMerchantCallBack() {
                                    @Override
                                    public void onSuccess(String success,JSONObject jsonObject,String merchantId) {
                                        new StoreData(getContext).saveMerchantId(merchantId);
                                        signUpView.dataSuccess(success);
                                        signUpView.executeUploadImageOrFile(merchantId);
                                        new StoreData(getContext).saveTypeOfContactSign("0");

                     /*  new StoreData(getContext).saveFilePath1("");
                       new StoreData(getContext).saveFilePath2("");
                       new StoreData(getContext).saveUploadImage("");
                       new StoreData(getContext).saveUploadFile("");*/
                                        signUpView.startLoginActivity();
                                        hideProgressDialog();

                                    }

                                    @Override
                                    public void onFailure(String throwable) {
                                        signUpView.dataFailure(throwable);
                                        hideProgressDialog();



                                    }

                                    @Override
                                    public void onNetworkFailure() {

                                    }
                                },signUpView.getStoreName(),signUpView.getFirstNameMerchant()+" "+signUpView.getLastNameMerchant(),Integer.parseInt(signUpView.getMobileOrAccountNumberMerchant()),Integer.parseInt(signUpView.getConfirmPinCodeMerchant()),getMerchantLocation,"",1,CONSTANTS.deviceToken);


                            }else{
                                MerchantLocation getMerchantLocationN = new MerchantLocation();
                                getMerchantLocationN.setLatitude("");
                                getMerchantLocationN.setLongitude("");
                                getMerchantLocationN.setCountry("");
                                getMerchantLocationN.setCity("");
                                getMerchantLocationN.setRegion("");

                                ParseUtils.SignUpMerchant(new GetCallBack.GetSignUpMerchantCallBack() {
                                    @Override
                                    public void onSuccess(String success,JSONObject jsonObject,String merchantId) {
                                        new StoreData(getContext).saveMerchantId(merchantId);
                                        signUpView.dataSuccess(success +" : "+ merchantId);
                                        signUpView.executeUploadImageOrFile(merchantId);

                                        //   Toast.makeText(getContext,"Uploading your file...",Toast.LENGTH_SHORT).show();

                       /* new StoreData(getContext).saveTypeOfContactSign("0");
                        new StoreData(getContext).saveFilePath1("");
                        new StoreData(getContext).saveFilePath2("");
                        new StoreData(getContext).saveUploadImage("");
                        new StoreData(getContext).saveUploadFile("");*/

                                        signUpView.startLoginActivity();
                                        hideProgressDialog();


                                    }

                                    @Override
                                    public void onFailure(String throwable) {
                                        signUpView.dataFailure(throwable);
                                        hideProgressDialog();


                                    }

                                    @Override
                                    public void onNetworkFailure() {

                                    }
                                },signUpView.getStoreName(),signUpView.getFirstNameMerchant()+" "+signUpView.getLastNameMerchant(),Integer.parseInt(signUpView.getMobileOrAccountNumberMerchant()),Integer.parseInt(signUpView.getConfirmPinCodeMerchant()),getMerchantLocationN,"",1,CONSTANTS.deviceToken);
                            }



                        }});

            alertDialog.show();



        }/*else {
            new StoreData(getContext).saveFilePath1("");
            new StoreData(getContext).saveFilePath2("");
            new StoreData(getContext).saveUploadImage("");
            new StoreData(getContext).saveUploadFile("");
        }*/
    }


    private void getUserSignUpDetails(){
        boolean  checkDataUser = checkDataUser();
        if(checkDataUser){

            // First Check number is writing right
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Is this your number?");
            alertDialog.setMessage("+2"+signUpView.getMobileOrAccountNumber());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            progressDialog = new ProgressDialog(getContext);
                            progressDialog.setMessage(getContext.getResources().getString(R.string.loading_data));
                            showProgressDialog();

                            // Check first phoneNumber
                            // if hasPinCod  ---> User Register
                            // if status    ----> not register and start it verification code
                            ParseUtils.ForgotPinUser(new GetCallBack.ForgotPinUser() {
                                @Override
                                public void onSuccess(String newPinCode, String success) {
                                    signUpView.dataSuccess("Duplicate phone number.");
                                    hideProgressDialog();
                                }

                                @Override
                                public void onFailure(String throwable) {
                                    verificationCodeGenerator = String.format("%04d", new Random().nextInt(10000));
                                    Toast.makeText(getContext,"Sending SMS...",Toast.LENGTH_LONG).show();
                                 /*   signUpView.showVerificationDialogCode(verificationCodeGenerator);
                                    hideProgressDialog();*/

                                    ParseUtils.SendSmsToUserOrMerchantForgotPinCode(new GetCallBack.SendSMS() {
                                        @Override
                                        public void onSuccess(String getRespsone) {
                                            signUpView.showVerificationDialogCode(verificationCodeGenerator);
                                            hideProgressDialog();
                                        }

                                        @Override
                                        public void onNetworkFailure() {

                                        }
                                    },signUpView.getMobileOrAccountNumber(),"MilliM PIN:"+" "+verificationCodeGenerator);



                                }

                                @Override
                                public void onNetworkFailure() {

                                }
                            },signUpView.getMobileOrAccountNumber());


                        }});
            alertDialog.show();






        }



    }

    private boolean checkDataUser(){
        if (TextUtils.isEmpty(signUpView.getFirstName()) && TextUtils.isEmpty(signUpView.getLastName()) &&
                TextUtils.isEmpty(signUpView.getMobileOrAccountNumber()) && TextUtils.isEmpty(signUpView.getPinCode())
                && TextUtils.isEmpty(signUpView.getConfirmPinCode())
                )

        {
            signUpView.showErrorAllFields(getContext.getResources().getString(R.string.empty_field));
            return false;
        }
        else if(TextUtils.isEmpty(signUpView.getFirstName())){
            signUpView.showErrorFirstName(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getLastName())){
            signUpView.showErrorLastName(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getMobileOrAccountNumber())) {
            signUpView.showErrorMobileOrAccountNumber(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getPinCode())) {
            signUpView.showErrorPinCode(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getConfirmPinCode())){
            signUpView.showErrorConfirmPinCode(getContext.getResources().getString(R.string.empty_field));
            return false;
        }
        else if(!signUpView.getMobileOrAccountNumber().startsWith("01")){
            signUpView.showErrorMobileOrAccountNumber(getContext.getResources().getString(R.string.error1));
            return false;
        }
        else if(signUpView.getMobileOrAccountNumber().length()<=10){
            signUpView.showErrorMobileOrAccountNumber(getContext.getResources().getString(R.string.error1));
            return false;
        }else if(signUpView.getPinCode().length()<4){
            signUpView.showErrorPinCode(getContext.getResources().getString(R.string.error2));
            return false;
        }
       else if(!signUpView.getConfirmPinCode().matches(signUpView.getPinCode())){
            signUpView.showErrorMismatchConfirmPinCode(getContext.getResources().getString(R.string.not_match));
            return false;
        }
        return true;
    }


    private boolean checkDataMerchant(){
        if (TextUtils.isEmpty(signUpView.getStoreName())&&TextUtils.isEmpty(signUpView.getFirstNameMerchant()) && TextUtils.isEmpty(signUpView.getLastNameMerchant()) &&
                TextUtils.isEmpty(signUpView.getMobileOrAccountNumberMerchant()) && TextUtils.isEmpty(signUpView.getPinCodeMerchant())
                && TextUtils.isEmpty(signUpView.getConfirmPinCodeMerchant())
                )

        {
            signUpView.showErrorAllFields(getContext.getResources().getString(R.string.empty_field));
            return false;
        }
        else if(TextUtils.isEmpty(signUpView.getStoreName())){
            signUpView.showErrorStoreName(getContext.getResources().getString(R.string.empty_field));
            return false;
        }
        else if(TextUtils.isEmpty(signUpView.getFirstNameMerchant())){
            signUpView.showErrorFirstNameMerchant(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getLastNameMerchant())){
            signUpView.showErrorLastNameMerchant(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getMobileOrAccountNumberMerchant())) {
            signUpView.showErrorMobileOrAccountNumberMerchant(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getPinCodeMerchant())) {
            signUpView.showErrorPinCodeMerchant(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(TextUtils.isEmpty(signUpView.getConfirmPinCodeMerchant())){
            signUpView.showErrorConfirmPinCodeMerchant(getContext.getResources().getString(R.string.empty_field));
            return false;
        }else if(!signUpView.getMobileOrAccountNumberMerchant().startsWith("01")){
            signUpView.showErrorMobileOrAccountNumberMerchant(getContext.getResources().getString(R.string.error1));
            return false;
        }
        else if(signUpView.getMobileOrAccountNumberMerchant().length()<=10){
            signUpView.showErrorMobileOrAccountNumberMerchant(getContext.getResources().getString(R.string.error3));
            return false;
        }else if(signUpView.getPinCodeMerchant().length()<4){
            signUpView.showErrorPinCodeMerchant(getContext.getResources().getString(R.string.error2));
            return false;
        }
        else if(!signUpView.getPinCodeMerchant().matches(signUpView.getConfirmPinCodeMerchant())) {
            signUpView.showErrorMismatchConfirmPinCodeMerchant(getContext.getResources().getString(R.string.not_match));
            return false;
        }

        return true;
    }

    public static boolean isNetworkOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    private static void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.setProgress(2);
            progressDialog.show();

    }

    private static void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    public static void  userHasVerifiedSendData(){
        showProgressDialog();
        ParseUtils.SignUpUser(new GetCallBack.GetSignUpUserCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject, String success) {
                signUpView.dataSuccess(success);
                signUpView.startLoginActivity();
                new StoreData(getContext).saveTypeOfContactSign("0");
                hideProgressDialog();

            }
            @Override
            public void onFailure(String failure) {
                signUpView.dataFailure(failure);
                hideProgressDialog();

            }

            @Override
            public void onNetworkFailure() {
                hideProgressDialog();
                boolean isOffline =  isNetworkOnline();
                if(!isOffline){
                    signUpView.onNetworkFailure(getContext.getResources().getString(R.string.test_server_conn_error));

                }
            }
        },signUpView.getFirstName()+" "+signUpView.getLastName(), Integer.parseInt(signUpView.getMobileOrAccountNumber()), Integer.parseInt(signUpView.getPinCode()),Integer.parseInt("1"),CONSTANTS.deviceToken);
    }
}
