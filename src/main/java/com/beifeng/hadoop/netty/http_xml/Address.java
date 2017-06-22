package com.beifeng.hadoop.netty.http_xml;

/**
 * 
 * Address
 *	
 * @Description 订单地址
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class Address {

    private String streel1;
    
    private String streel2;
    
    private String city;
    
    private String state;
    
    private String postCode;
    
    

    public Address() {
        super();
    }

    public Address(String streel1, String streel2, String city, String state,
            String postCode) {
        super();
        this.streel1 = streel1;
        this.streel2 = streel2;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
    }

    public String getStreel1() {
        return streel1;
    }

    public void setStreel1(String streel1) {
        this.streel1 = streel1;
    }

    public String getStreel2() {
        return streel2;
    }

    public void setStreel2(String streel2) {
        this.streel2 = streel2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return "Address [streel1=" + streel1 + ", streel2=" + streel2 + ", city=" + city
                + ", state=" + state + ", postCode=" + postCode + "]";
    }
}
