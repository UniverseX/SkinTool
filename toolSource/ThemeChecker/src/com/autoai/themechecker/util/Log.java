package com.autoai.themechecker.util;

public class Log {
    private static final boolean isLoggable = false;
    public static void println(Object o){
        if(isLoggable){
            System.out.println(o);
        }
    }
}
