package org.isv.samples.exercises;

public class SingletonSample {
    static {
        System.out.println("SingletonSample instance created");
    }

    public static void someMethod() {
        System.out.println("someMethod");

    }

    public static Object getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        public static Object instance = new Object();

        static {
            System.out.println("Instance Holder created");
        }
    }
}
