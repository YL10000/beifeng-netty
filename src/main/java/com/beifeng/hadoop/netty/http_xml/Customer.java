package com.beifeng.hadoop.netty.http_xml;

import java.util.List;

/**
 * 
 * Customer
 *	
 * @Description 客户信息
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class Customer {

    private long customerNumber;
    
    private String firstName;
    
    private String last;
    
    private List<String> middleNames;
    
    

    public Customer() {
        super();
    }
    
    public Customer(long customerNumber, String firstName, String last,
            List<String> middleNames) {
        super();
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.last = last;
        this.middleNames = middleNames;
    }



    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(List<String> middleNames) {
        this.middleNames = middleNames;
    }

    @Override
    public String toString() {
        return "Customer [customerNumber=" + customerNumber + ", firstName=" + firstName
                + ", last=" + last + ", middleNames=" + middleNames + "]";
    }
    
    
}
