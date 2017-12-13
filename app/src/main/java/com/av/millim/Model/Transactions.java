package com.av.millim.Model;

import java.io.Serializable;

/**
 * Created by Maiada on 10/26/2017.
 */

public class Transactions  implements Serializable {

    public String TransactionID ;
    public String Type ;
    public String From_UserID ;
    public String From_MerchantID ;
    public String To_UserID ;
    public String To_MerchantID;
    public String To_UserPhone;
    public String To_UserName;
    public String Date ;
    public String Status ;


    public String getTo_MerchantName() {
        return To_MerchantName;
    }

    public void setTo_MerchantName(String to_MerchantName) {
        To_MerchantName = to_MerchantName;
    }

    public String To_MerchantName;

    public String Confirmation ;
    public String Credit ;
    public String Country ;
    public String City;
    public String Region;
    public String Longitude ;
    public String Latitude ;
    public String DeviceID ;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }


    public String getTo_UserName() {
        return To_UserName;
    }

    public void setTo_UserName(String to_UserName) {
        To_UserName = to_UserName;
    }
    public String getConfirmation() {
        return Confirmation;
    }

    public void setConfirmation(String confirmation) {
        Confirmation = confirmation;
    }

    public String getTo_UserPhone() {
        return To_UserPhone;
    }

    public void setTo_UserPhone(String to_UserPhone) {
        To_UserPhone = to_UserPhone;
    }
}
