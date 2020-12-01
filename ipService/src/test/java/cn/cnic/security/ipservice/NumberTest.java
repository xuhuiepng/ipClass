package cn.cnic.security.ipservice;

import org.junit.Test;

import java.util.Arrays;

public class NumberTest {
    @Test
    public void name1(){
        Integer i = 0;
        System.out.println(i.byteValue());
        Short j = 0;
        System.out.println(j.byteValue());
        Float f = 0f;
        System.out.println(f.byteValue());
        Double d = 0d;
        System.out.println(d.byteValue() == i.byteValue());

    }
}
