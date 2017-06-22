package com.beifeng.hadoop.netty.http_xml;

/**
 * 
 * Order
 *	
 * @Description 订单信息
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class Order {

    private long orderNumber;
    
    private Customer customer;
    
    private Address billTo;
    
    private Shipping shipping;
    
    

    public Order() {
        super();
    }
    
    public Order(long orderNumber, Customer customer, Address billTo, Shipping shipping) {
        super();
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.billTo = billTo;
        this.shipping = shipping;
    }



    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getBillTo() {
        return billTo;
    }

    public void setBillTo(Address billTo) {
        this.billTo = billTo;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {
        return "Order [orderNumber=" + orderNumber + ", customer=" + customer
                + ", billTo=" + billTo + ", shipping=" + shipping + "]";
    }
    
    
}
