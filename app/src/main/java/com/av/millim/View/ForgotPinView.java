package com.av.millim.View;

/**
 * Created by Maiada on 11/2/2017.
 */

public interface ForgotPinView {

    String getPhoneNumber();
    String getAccountNumber();

    void showErrorPhoneNumber(String errorPhoneNumber);
    void showErrorAccountNumber(String errorAccountNumber);

    void onDataSuccess(String success);
    void onDataFailure(String failure);
    void onNetworkFailure(String networkFailure);

    void startLoginActivity();



}
