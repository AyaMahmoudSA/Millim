package com.av.millim.Prestener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.av.millim.Activites.ForgotActivity;
import com.av.millim.Activites.MainActivity;
import com.av.millim.Data.CONSTANTS;
import com.av.millim.Data.StoreData;
import com.av.millim.ParseData.GetCallBack;
import com.av.millim.ParseData.ParseUtils;
import com.av.millim.R;
import com.av.millim.View.ForgotPinView;

import static android.R.attr.type;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.av.millim.Activites.SignUpActivity.getTypeOfAccount;

/**
 * Created by Maiada on 11/2/2017.
 */

public class ForgotPinPresenter {

    private ForgotPinView forgotPinVew;
    private ProgressDialog pDialog;
    private Context getContext;

    public ForgotPinPresenter(ForgotPinView forgotPinVew) {

        this.forgotPinVew = forgotPinVew;
    }

    public void onConfirmButtonClicked(Context context,String getTypeOfAccount) {
        this.getContext = context;

        if(getTypeOfAccount.equals("0")){

            forgotPinCodeUser();
           // Toast.makeText(getContext,"User",Toast.LENGTH_SHORT).show();

        }else{
         //   Toast.makeText(getContext,"Merchant",Toast.LENGTH_SHORT).show();

            forgotPinCodeMerchant();

        }
    }

    public void forgotPinCodeUser() {
        boolean checkData = checkData("0");
        if(checkData){
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext).create();
            alertDialog.setTitle("Is this your number?");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setMessage("+2 "+forgotPinVew.getPhoneNumber());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            pDialog = new ProgressDialog(getContext);
                            pDialog.setMessage("Loading...");
                            pDialog.setCanceledOnTouchOutside(false);
                            showProgressDialog();
                            ParseUtils.ForgotPinUser(new GetCallBack.ForgotPinUser() {
                                @Override
                                public void onSuccess(String newPinCode, String success) {
                                    forgotPinVew.onDataSuccess(success);
                                    //Sending SMS

                                    showProgressDialog();
                                    ParseUtils.SendSmsToUserOrMerchantForgotPinCode(new GetCallBack.SendSMS() {
                                        @Override
                                        public void onSuccess(String getRespsone) {
                                            forgotPinVew.startLoginActivity();
                                            hideProgressDialog();

                                        }
                                        @Override
                                        public void onNetworkFailure() {

                                        }
                                    },forgotPinVew.getPhoneNumber(),"You recently requested to get your MilliM password, your password in our system : "+" "+ newPinCode );


                                    //   forgotPinVew.onDataSuccess(newPinCode,success);

                                }

                                @Override
                                public void onFailure(String errror) {
                                    hideProgressDialog();
                                    forgotPinVew.onDataFailure(errror);
                                }

                                @Override
                                public void onNetworkFailure() {
                                    hideProgressDialog();
                                    boolean isOffline =  isNetworkOnline();
                                    if(!isOffline){
                                        forgotPinVew.onNetworkFailure(getContext.getResources().getString(R.string.test_server_conn_error));

                                    }
                                }
                            },forgotPinVew.getPhoneNumber());



                        }
                    });
            alertDialog.show();



        }


    }


    private void forgotPinCodeMerchant() {
        boolean checkData = checkData("1");
        if(checkData){
             String getAccountIdFormat = setAccountIdFormat(forgotPinVew.getAccountNumber());
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Is this your account number?");
            alertDialog.setMessage(" "+getAccountIdFormat+" ");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            pDialog = new ProgressDialog(getContext);
                            pDialog.setMessage("Loading...");
                            pDialog.setCanceledOnTouchOutside(false);
                            showProgressDialog();
                            //String getDeviceToken = new StoreData(getContext).loadDeviceToken();

/*
                            ParseUtils.ForgotPinMerchant(new GetCallBack.ForgotPinUser() {
                                @Override
                                public void onSuccess(String newPinCode, String success) {
                                    forgotPinVew.onDataSuccess(success);
                                    //Sending SMS
                                    showProgressDialog();
                                    ParseUtils.SendSmsToUserOrMerchantForgotPinCode(new GetCallBack.SendSMS() {
                                        @Override
                                        public void onSuccess(String getRespsone) {
                                            forgotPinVew.startLoginActivity();
                                            hideProgressDialog();
                                        }
                                        @Override
                                        public void onNetworkFailure() {

                                        }
                                    },forgotPinVew.getPhoneNumber(),"You recently requested to get your MilliM password, your password in our system : "+" "+ newPinCode );                 //
                                    //
                                }

                                @Override
                                public void onFailure(String errror) {
                                    hideProgressDialog();
                                    forgotPinVew.onDataFailure(errror);
                                }

                                @Override
                                public void onNetworkFailure() {
                                    hideProgressDialog();
                                    boolean isOffline =  isNetworkOnline();
                                    if(!isOffline){
                                        forgotPinVew.onNetworkFailure(getContext.getResources().getString(R.string.test_server_conn_error));

                                    }
                                }
                            },forgotPinVew.getPhoneNumber());
*/

                            ParseUtils.SearchMerchant(new GetCallBack.SearchUser() {
                                @Override
                                public void onSuccess(String merchantID, String merchantName, String merchantPhone, String credit, String pinCode) {
                                    forgotPinVew.onDataSuccess("Sending SMS....");
                                    //Sending SMS
                                    showProgressDialog();
                                    ParseUtils.SendSmsToUserOrMerchantForgotPinCode(new GetCallBack.SendSMS() {
                                        @Override
                                        public void onSuccess(String getRespsone) {
                                            forgotPinVew.startLoginActivity();
                                            hideProgressDialog();
                                        }
                                        @Override
                                        public void onNetworkFailure() {

                                        }
                                    },merchantPhone,"You recently requested to get your MilliM password, your password in our system : "+" "+ pinCode );

                                }

                                @Override
                                public void onFailure(String failure) {
                                    hideProgressDialog();
                                    forgotPinVew.onDataFailure(failure);
                                }

                                @Override
                                public void onNetworkFailure() {
                                    hideProgressDialog();
                                    boolean isOffline =  isNetworkOnline();
                                    if(!isOffline){
                                        forgotPinVew.onNetworkFailure(getContext.getResources().getString(R.string.test_server_conn_error));

                                    }
                                }
                            },forgotPinVew.getAccountNumber(),"2");

                        }});


            alertDialog.show();



        }

    }
    private  boolean checkData(String Type){

        // phoneNumber
        if(Type.equals("0")){
            if(TextUtils.isEmpty(forgotPinVew.getPhoneNumber())){
                forgotPinVew.showErrorPhoneNumber(getContext.getResources().getString(R.string.empty_field));
                return false;
            }else if(forgotPinVew.getPhoneNumber().length()<10){
                forgotPinVew.showErrorPhoneNumber(getContext.getResources().getString(R.string.error1));
                return false;
            }
        }else {
         //accountNumber
            if(TextUtils.isEmpty(forgotPinVew.getAccountNumber())){
                forgotPinVew.showErrorAccountNumber(getContext.getResources().getString(R.string.empty_field));
                return false;
            }else if(forgotPinVew.getAccountNumber().length()<6){
                forgotPinVew.showErrorAccountNumber(getContext.getResources().getString(R.string.error4));
                return false;
            }

        }


        return true;
    }

    public boolean isNetworkOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    private String setAccountIdFormat(String accountId){
        StringBuilder numberFormat = new StringBuilder();
        for(int i=0;i<accountId.length();i++){
            String getString = String.valueOf(accountId.charAt(i));
            if(i!=3){
                numberFormat.append(getString);
            }else{
                numberFormat.append("-"+getString);

            }

        }

        return  numberFormat.toString();

    }

}
