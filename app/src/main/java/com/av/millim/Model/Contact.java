package com.av.millim.Model;

import javax.xml.transform.sax.SAXTransformerFactory;

/**
 * Created by Maiada on 10/19/2017.
 */

public class Contact {



  private int id;
  private  String nameContact;
  private  String phoneNumber;
  private  String email;


  private String userId;
  private String userName;
  private String phoneNumberUserSearch;

  private boolean isMilliMContact;

    public Contact() {

    }

    public Contact(String nameContact, String phoneNumber) {
        this.nameContact = nameContact;
        this.phoneNumber = phoneNumber;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumberUserSearch() {
        return phoneNumberUserSearch;
    }

    public void setPhoneNumberUserSearch(String phoneNumberUserSearch) {
        this.phoneNumberUserSearch = phoneNumberUserSearch;
    }

    public boolean getIsMilliMContact() {
        return isMilliMContact;
    }

    public void setIsMilliMContact(boolean milliMContact) {
        isMilliMContact = milliMContact;
    }
}
