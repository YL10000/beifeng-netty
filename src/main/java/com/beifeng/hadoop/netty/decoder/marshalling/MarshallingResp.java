package com.beifeng.hadoop.netty.decoder.marshalling;

import java.io.Serializable;

public class MarshallingResp implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    public MarshallingResp() {
        super();
    }

    public MarshallingResp(int id, int code, String desc) {
        super();
        this.id = id;
        this.code = code;
        this.desc = desc;
    }

    private int id;
    
    private int code;
    
    private String desc;
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "MarshallingResp [id=" + id + ", code=" + code + ", desc=" + desc + "]";
    }

}
