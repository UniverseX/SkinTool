package com.autoai.themechecker.util;

public class Log {
    private static final boolean isLoggable = true;
    public static void println(Object o){
        if(isLoggable){
            System.out.println(o);
        }
    }
}
