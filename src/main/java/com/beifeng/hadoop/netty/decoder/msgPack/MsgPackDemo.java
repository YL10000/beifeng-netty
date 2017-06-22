package com.beifeng.hadoop.netty.decoder.msgPack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

public class MsgPackDemo {
    
    public static void main(String[] args) throws IOException {
        List<String> firuts=new ArrayList<String>();
        firuts.add("苹果");
        firuts.add("西瓜");
        firuts.add("葡萄");
        MessagePack messagePack=new MessagePack();
        //将对象进行编码为字节数组
        byte[] raw=messagePack.write(firuts);
        
        //将字节数组解码为对象
        List<String> deFiruts=messagePack.read(raw,Templates.tList(Templates.TString));
        for(String firsut:deFiruts){
            System.out.println(firsut);
            
        }
        
    }

}
