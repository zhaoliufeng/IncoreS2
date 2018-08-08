package com.ws.mesh.incores2;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    int symbolStr = 0;
    @Test
    public void test(){
        setSunrise(true);
        System.out.println("symbolStr " + symbolStr);
        setSunset(true);
        System.out.println("symbolStr " + symbolStr);
        setSunset(false);
        System.out.println("symbolStr " + symbolStr);
        setSunrise(false);
        System.out.println("symbolStr " + symbolStr);
    }

    public void setSunrise(boolean isOpen){
        //将之前的symbolStr的sunrise值置0
        symbolStr = (symbolStr & 0x02) + (isOpen ? 1 : 0);
    }

    public void setSunset(boolean isOpen){
        symbolStr = (symbolStr & 0x01) + (isOpen ? 2 : 0);
    }
}