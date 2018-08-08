package com.ws.mesh.incores2.service.music;

import java.util.Random;

/**
 * Created by we_smart on 2018/1/23.
 */

public class MusicUtils {

    private static int mBrightness = 0;
    private static int mPerPosition = 0;

    private static byte[] color_1 = { 0x09, (byte) 0xFF, 0, 0, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_2 = { 0x09, 0, (byte) 0xFF, 0, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_3 = { 0x09, 0, 0, (byte) 0xFF, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_4 = { 0x09, (byte) 0xFF, (byte) 0x80, 0, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_5 = { 0x09, (byte) 0xFF, (byte) 0x80, (byte) 0x80, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_6 = { 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x80, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_7 = { 0x09, (byte) 0xFF, (byte) 0x00, (byte) 0x80, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_8 = { 0x09, (byte) 0xFF, (byte) 0xFF, 0, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_9 = { 0x09, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_10 = { 0x09, 0, (byte) 0xFF, (byte) 0xFF, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_11 = { 0x09, (byte) 0xFF, (byte) 0xFF, 0, 0, 0, 0, 0, (byte) 0x9F };
    private static byte[] color_12 = { 0x09, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, 0, 0, 0, 0, (byte) 0x9F};
    private static byte[] color_13 = { 0x09, (byte) 0x80, (byte) 0x00, (byte) 0xFF, 0, 0, 0, 0, (byte) 0x9F };

    private static byte[][] colorParams = { color_1, color_2, color_3, color_4, color_5, color_6, color_7,
            color_8, color_9, color_10, color_11, color_12, color_13 };

    public static byte[] getColor(int brightness) {
        if (Math.abs(brightness - mBrightness) < 50) {
            mBrightness = brightness;
            return colorParams[ mPerPosition ];
        } else {
            Random random = new Random();
            mPerPosition = random.nextInt(13);
            return colorParams[ mPerPosition ];
        }
    }

    public static int parseMusicData(byte[] data) {
        int E = 0;
        for (int x = 0 ; x < data.length ; x++) {
            E = E + (data[ x ] & 0xFF);
        }
        if (E == 0) return -1;
        E = E - 100000;
        return getBrightness(E);
    }

    public static int parseMusicData(short[] data) {
        int E = 0;
        for (int x = 0 ; x < data.length ; x++) {
            E = E + (Math.abs(data[ x ]));
        }
        if (E == 0) return -1;
        int brightness = (E - 5000) / 1000;
        return brightness;
    }


//    private static int getBrightness(int E) {
//        int brightness = 0;
//        if (0 > E) {
//            brightness = 0;
//        } else if (E <= 7000) {
//            if (E < 5000) {
//                brightness = 1;
//            } else if (E < 6000) {
//                brightness = 2;
//            } else {
//                brightness = 3;
//            }
//        } else if (E <= 10000) {
//            if (E <= 7700) {
//                brightness = 4;
//            } else if (E <= 8400) {
//                brightness = 5;
//            } else if (E <= 9100) {
//                brightness = 6;
//            } else {
//                brightness = 7;
//            }
//        } else if (E <= 20000) {
//            brightness = 8 + (E - 10000) / 500;
//        } else if (E <= 30000) {
//            brightness = 29 + (E - 20000) / 400;
//        } else if (E <= 40000) {
//            brightness = 54 + (E - 30000) / 400;
//        } else if (E <= 48000) {
//            brightness = 79 + (E - 40000) / 800;
//        } else {
//            brightness = 90 + (E - 48000) / 1000;
//        }
//        return brightness;
//    }

    private static int getBrightness(int E) {
        int brightness = 0;
        if (0 > E) {
            brightness = 0;
        } else if (E <= 7000) {
            brightness = 1 + (E - 5000) / 1000;
        } else if (E <= 10000) {
            brightness = 3 + (E - 7000) / 700;
        } else if (E <= 20000) {
            brightness = 1 + (E - 10000) / 1000;
        } else if (E <= 30000) {
            brightness = 40 + (E - 20000) / 400;
        } else if (E <= 40000) {
            brightness = 70 + (E - 30000) / 400;
        } else if (E <= 48000) {
            brightness = 79 + (E - 40000) / 800;
        } else {
            brightness = 90 + (E - 48000) / 1000;
        }
        return brightness;
    }

}
