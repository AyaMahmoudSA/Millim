package com.av.millim.View;

import com.av.millim.Model.Transactions;

import java.util.ArrayList;

/**
 * Created by Maiada on 10/29/2017.
 */

public interface  LoginView  {

    String getUserPhoneOrMerchantID();
    String getUserPinCode();
    void   showUserPhoneAndPinCodeError(String errorPhoneAndPinCode);
    void   showUserPhoneError(String errorPhone);
    void   showUserPinCodeError(String errorPinCode);
    void   dataFailure(String failure);
    void   dataSuccess(String success, ArrayList<Transactions> transactionsArrayList);
    void   onNetworkFailure(String errorNetwork);

    void   startMainActivity();


}
