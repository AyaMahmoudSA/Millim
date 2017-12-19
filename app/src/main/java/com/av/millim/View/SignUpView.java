package com.av.millim.View;

import android.content.Intent;

import com.av.millim.Model.MerchantLocation;

/**
 * Created by Maiada on 10/30/2017.
 */

public interface SignUpView {


    String getFirstName();
    String getLastName();
    String getMobileOrAccountNumber();
    String getPinCode();
    String getConfirmPinCode();


    String getStoreName();
    String getFirstNameMerchant();
    String getLastNameMerchant();
    String getMobileOrAccountNumberMerchant();
    String getPinCodeMerchant();
    String getConfirmPinCodeMerchant();

    void  showErrorStoreName(String errorStoreName);
    void  showErrorAllFields(String errorAllFields);
    void  showErrorFirstName(String errorFirstName);
    void  showErrorLastName(String errorLastName);
    void  showErrorMobileOrAccountNumber(String errorMobileOrAccountNumbe);
    void  showErrorPinCode(String errorPinCode);
    void  showErrorConfirmPinCode(String errorConfirmPinCode);

    void  showErrorFirstNameMerchant(String errorFirstName);
    void  showErrorLastNameMerchant(String errorLastName);
    void  showErrorMobileOrAccountNumberMerchant(String errorMobileOrAccountNumbe);
    void  showErrorPinCodeMerchant(String errorPinCode);
    void  showErrorConfirmPinCodeMerchant(String errorPinCode);


    void  executeUploadImageOrFile(String merchantId);
    void  showErrorAttachButton(String errorAttachButton);
    void  getCurrentLocation();
     MerchantLocation getLocationData();

    void  showErrorMismatchConfirmPinCode(String errorMismatchConfirmPinCode);
    void  showErrorMismatchConfirmPinCodeMerchant(String errorMismatchConfirmPinCode);


    void   dataSuccess(String success);
    void   dataFailure(String failure);
    void   onNetworkFailure(String errorNetwork);

    void startLoginActivity();
    void startActivityForResult(Intent intent,int galleryPick);
    void requestForPermission();

    void showVerificationDialogCode(String getVerificationCodeGenerator);

    void getPermission();
    void getLocation();




}
