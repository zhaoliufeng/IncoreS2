package com.ws.mesh.incores2.utils;

public class ViewUtils {
    /**
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @param pos 中间值
     * @return 中间值的颜色
     */
    public static int interpolate(int startColor, int endColor, float pos){
        int startA = alpha(startColor);
        int startR = red(startColor);
        int startG = green(startColor);
        int startB = blue(startColor);

        int endA = alpha(endColor);
        int endR = red(endColor);
        int endG = green(endColor);
        int endB = blue(endColor);

        int interA = (int) (startA + (endA - startA) * pos);
        int interR = (int) (startR + (endR - startR) * pos);
        int interG = (int) (startG + (endG - startG) * pos);
        int interB = (int) (startB + (endB - startB) * pos);

        return argb(interA, interR, interG, interB);
    }

    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int alpha(int color) {
        return color >>> 24;
    }

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static String[] getAlarmShow(int hours, int minutes) {
        String[] data = new String[ 2 ];
        if (hours >= 0 && hours < 12) {
            data[ 0 ] = alarmShow(hours == 0 ? 12 : hours) + ":" + alarmShow(minutes);
            data[ 1 ] = "AM";
        } else {
            data[ 0 ] = alarmShow(hours == 12 ? 12 : hours - 12) + ":" + alarmShow(minutes);
            data[ 1 ] = "PM";
        }
        return data;
    }

    private static String alarmShow(int time) {
        if (time < 10) {
            if (time == -1) return "0";
            return "0" + time;
        } else {
            return Integer.toString(time);
        }
    }

    //翻转byte[]
    public static byte[] reverseBytes(byte[] src){
        byte tempVal = src[0];
        for (int i = 0; i < src.length/2; i++){
            src[i] = src[src.length - i - 1];
            src[src.length - i - 1] = tempVal;
            tempVal = src[i + 1];
        }
        return src;
    }

    public static byte[] weekNumToBinaryByteArray(int intVal) {
        byte[] values = new byte[]{ 0, 0, 0, 0, 0, 0, 0 };
        if (intVal != -1) {
            int step = 1;
            while ( intVal != 0 ) {
                values[ values.length - step ] = (byte) (intVal % 2);
                intVal /= 2;
                step++;
            }
        }
        return values;
    }

    public static int byteArrayToWeekNum(byte[] weekByte) {
        int value;
        value = (weekByte[ 0 ] << 6)
                + (weekByte[ 1 ] << 5)
                + (weekByte[ 2 ] << 4)
                + (weekByte[ 3 ] << 3)
                + (weekByte[ 4 ] << 2)
                + (weekByte[ 5 ] << 1)
                + (weekByte[ 6 ]);
        return value;
    }
}
