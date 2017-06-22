package com.beifeng.hadoop.netty.protocol;

import java.io.IOException;


import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;


public class NettyMarshallingCodecFactory {
    
    /**
     * 
     * buildMarshaller
     * 
     * @Description 构建Marshaller
     * @return
     * @throws Exception
     * @return Marshaller 
     * @throws Exception 
     * @see
     * @since
     */
    /*protected static Marshaller buildMarshaller() throws Exception {
        final MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller=marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }*/
    
    
    public static NettyMarshallingDecoder buildMarshallingDecoder() throws Exception {
        MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        //Unmarshaller unmarshaller=marshallerFactory.createUnmarshaller(configuration);
        UnmarshallerProvider provider=new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        return new NettyMarshallingDecoder(provider, 10240);
    }
    
    
    public static NettyMarshallingEncoder buildMarshallingEncoder() {
        MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider=new DefaultMarshallerProvider(marshallerFactory, configuration);
        return new NettyMarshallingEncoder(provider);
    }
    
    /**
     * 
     * buildUnMarshaller
     * 
     * @Description 构建UnMarshaller
     * @return
     * @throws Exception
     * @return Unmarshaller 
     * @see
     * @since
     */
    /*protected static Unmarshaller buildUnMarshaller() throws Exception {
        final MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        Unmarshaller unmarshaller=marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
        
    }*/
}
