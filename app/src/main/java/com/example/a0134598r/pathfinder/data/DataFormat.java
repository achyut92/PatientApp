package com.example.a0134598r.pathfinder.data;

/**
 * Created by jixiang on 11/9/15.
 */
public class DataFormat {

    public static double Stringtodouble(String src) {

        double result = 0.0;
        try {
            result = Double.parseDouble(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doubletoString(double src) {

        String result = "";
        try {
            result = String.valueOf(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String intToString(int src) {
        String result = "";
        try {
            result = String.valueOf(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
