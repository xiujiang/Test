package com.digibig.service.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.UnsupportedEncodingException;

/**
 * @author liuxiujiang
 * @version 1.0
 * @datetime 2018/10/16
 * @since 1.8
 */
public class ObjectRedisSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if(o == null){
            return null;
        }
        if(o instanceof String){
            return ((String) o).getBytes();
        }else if(o instanceof Integer){
            return o.toString().getBytes();
        }else if(o instanceof Byte){
            return new byte[]{((Byte) o).byteValue()};
        }else{
            return o.toString().getBytes();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if(bytes == null){
            return null;
        }
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
