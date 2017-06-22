package com.beifeng.hadoop.netty.decoder.marshalling;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodecFactory {
    
    /**
     * 
     * buildDecoder
     * 
     * @Description 构建Marshalling解码器
     * @return
     * @return MarshallingDecoder 
     * @see
     * @since
     */
    public static MarshallingDecoder buildDecoder() {
        //"serial"表示创建的是java序列化工厂对象
        final MarshallerFactory factory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider=new DefaultUnmarshallerProvider(factory, configuration);
        //1024表示单个消息序列化后的最大长度
        MarshallingDecoder decoder=new MarshallingDecoder(provider, 1024);
        return decoder;
    }
    
    /**
     * 
     * buildEncoder
     * 
     * @Description 构建marshing编码器
     * @return
     * @return MarshallingEncoder 
     * @see
     * @since
     */
    public static MarshallingEncoder buildEncoder(){
        final MarshallerFactory factory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider=new DefaultMarshallerProvider(factory, configuration);
        MarshallingEncoder encoder=new MarshallingEncoder(provider);
        return encoder;
    }

}
