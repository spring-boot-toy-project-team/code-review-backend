package com.codereview.util;

public class RandomStringUtils {

    /**
     * 인증코드 만들어주는 로직
     */
    public static String generateAuthNo() {
        return org.apache.commons.lang3.RandomStringUtils.randomNumeric(6);
    }
}
