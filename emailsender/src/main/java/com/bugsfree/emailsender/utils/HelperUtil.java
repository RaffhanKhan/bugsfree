package com.bugsfree.emailsender.utils;

public interface HelperUtil {

    static boolean checksNullOrEmpty(String input) {
        return null == input || "".equals(input);
    }
}
