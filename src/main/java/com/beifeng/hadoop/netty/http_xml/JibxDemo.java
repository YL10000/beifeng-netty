package com.beifeng.hadoop.netty.http_xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.BindingFactoryBase;

import com.beifeng.hadoop.netty.NettyConstant;

/**
 * 
 * JibxDemo
 * 
 * @Description 使用jibx框架将java对象和xml进行转换
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class JibxDemo {

    private IBindingFactory factory = null;

    private StringWriter writer;

    private StringReader reader;

    public static void main(String[] args) throws Exception {
        Order order = new Order(123l, new Customer(362l, "狄", "杰", Arrays.asList("仁")),
                new Address("河南", "", "洛阳", "大唐", "123456"), Shipping.DOMESTIC_EXPRESS);
        System.out.println("原始对象：");
        System.out.println(order);
        System.out.println("编码后的xml文件：");
        String xmlStr=new JibxDemo().encode2Xml(order);
        System.out.println(xmlStr);
        System.out.println("解码后的对象：");
        order=new JibxDemo().decode2Order(xmlStr);
        System.out.println(order);
    }

    /**
     * 
     * encode2Xml
     * 
     * @Description 将对象编码为xml
     * @param order
     * @return
     * @throws Exception
     * @return String
     * @see
     * @since
     */
    private String encode2Xml(Order order) throws Exception {
        factory = BindingDirectory.getFactory(order.getClass());
        writer = new StringWriter();
        IMarshallingContext context = factory.createMarshallingContext();
        context.setIndent(2);
        context.marshalDocument(order, NettyConstant.CHARSET_UTF_8, null, writer);
        String xmlStr = writer.toString();
        writer.close();
        return xmlStr;
    }

    /**
     * 
     * decode2Order
     * 
     * @Description 将xml文件解码为对象
     * @param xmlStr
     * @return
     * @throws Exception
     * @return Order
     * @see
     * @since
     */
    private Order decode2Order(String xmlStr) throws Exception {
        factory = BindingDirectory.getFactory(Order.class);
        reader = new StringReader(xmlStr);
        IUnmarshallingContext context = factory.createUnmarshallingContext();
        Order order = (Order) context.unmarshalDocument(reader);
        reader.close();
        return order;
    }
}
