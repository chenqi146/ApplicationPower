package com.power.common.util;

import org.junit.jupiter.api.Test;

/**
 * Created by yu on 2017/8/2.
 */
public class PrettyMemoryUtilsTest {

    private static final int UNIT = 1024;

    public static void main(String[] args) {
        long mem = 65392864L;
        long result = mem / 1048576;
        System.out.println(((62) << 1));
    }

    @Test
    public void testPrettyByteSize() throws Exception {
        System.out.println(PrettyMemoryUtil.prettyByteSize(65392864));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * UNIT));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * 1023));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * 1023 * 1023 * 1023));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtil.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT * UNIT));
    }

}