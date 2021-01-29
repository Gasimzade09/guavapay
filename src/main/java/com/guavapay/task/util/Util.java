package com.guavapay.task.util;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static String generateCardNumber(){
        long smallest = 1000_0000_0000_0000L;
        long biggest =  9999_9999_9999_9999L;
        return Long.toString(ThreadLocalRandom.current().nextLong(smallest, biggest+1));
    }

    public static String generateAccountNumber(){
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        int count = 14;
        while (count-- !=0){
            int index = (int) (Math.random() * alphaNumericString.length());
            builder.append(alphaNumericString.charAt(index));
        }
        return builder.toString();
    }
}
