package com.SecondClass.utils;

/**
 * @title: SynchronizedDemo2
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/2/28 20:15
 **/

public class SynchronizedDemo2 {

    Object object = new Object();
    public void method1() {
        synchronized (object) {

        }
        method2();
    }

    private static void method2() {

    }
}
