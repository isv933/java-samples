package org.isv.samples.exercises.aop;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class InterceptionProxyTest {
    public static class HelloWorld {
        public void printHello(String message) {
            System.out.println(message);
        }
    }

    @Test
    public void shouldIntercept(){
        var proxy =  InterceptionProxy.createProxy(HelloWorld.class, List.of(
                (target, method, methodArgs, original) -> {

                    System.out.printf("Logging: Called method %s with args %s\n", method.getName(),
                            Arrays.stream(methodArgs).map(Object::toString).
                                    collect(Collectors.joining(",", "[", "]"))
                    );

                    var result = original.process();
                    System.out.println("Logging: Returned from method");
                    return result;
                },
                (target, method, methodArgs, original) -> {

                    System.out.printf("Tracing: Called method %s with args %s\n", method.getName(),
                            Arrays.stream(methodArgs).map(Object::toString).
                                    collect(Collectors.joining(",", "[", "]"))
                    );

                    var result = original.process();
                    System.out.println("Tracing: Returned from method");
                    return result;
                }
        ));

        proxy.printHello("helo!!!");
    }
}