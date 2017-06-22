package com.beifeng.hadoop.netty.decoder.protobuf;

import com.beifeng.hadoop.netty.decoder.protobuf.OrderProto.Order;
import com.beifeng.hadoop.netty.decoder.protobuf.OrderProto.Order.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

public class OrderProtoDemo {

    //编码
    private static byte[] encode(Order order){
        return order.toByteArray();
    }
    
    //解码
    private static Order decode(byte[] body) throws InvalidProtocolBufferException{
        return Order.parseFrom(body);
    }
    
    private static OrderProto.Order buildOrder(){
        Builder builder=OrderProto.Order.newBuilder();
        builder.setOrderId(1);
        builder.setMoney(3.02);
        builder.setProductName("西瓜");
        return builder.build();
    }
    
    public static void main(String[] args) throws InvalidProtocolBufferException {
        Order order=buildOrder();
        System.out.println(order);
        System.out.println(decode(encode(order)));
    }
}
