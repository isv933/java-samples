package org.isv.samples.exercises;

public class SafeSingletonSample {
    private static volatile Object instance;

    private SafeSingletonSample() {

    }

    public static Object getInstance() {
        var tmp = instance;
        if (tmp == null) {
            synchronized (SafeSingletonSample.class) {
                if (instance == null) {
                    instance = new Object();
                }

                tmp = instance;
            }
        }
        return tmp;
    }
}
