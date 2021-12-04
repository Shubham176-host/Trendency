package com.example.aptaki.Model;

public class Shipping
{
    private String sname, scity, spincode, saddress1, saddress2, sphone;

    public Shipping()
    {

    }

    public Shipping(String sname, String scity, String spincode, String saddress1, String saddress2, String sphone) {
        this.sname = sname;
        this.scity = scity;
        this.spincode = spincode;
        this.saddress1 = saddress1;
        this.saddress2 = saddress2;
        this.sphone = sphone;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getScity() {
        return scity;
    }

    public void setScity(String scity) {
        this.scity = scity;
    }

    public String getSpincode() {
        return spincode;
    }

    public void setSpincode(String spincode) {
        this.spincode = spincode;
    }

    public String getSaddress1() {
        return saddress1;
    }

    public void setSaddress1(String saddress1) {
        this.saddress1 = saddress1;
    }

    public String getSaddress2() {
        return saddress2;
    }

    public void setSaddress2(String saddress2) {
        this.saddress2 = saddress2;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }
}
