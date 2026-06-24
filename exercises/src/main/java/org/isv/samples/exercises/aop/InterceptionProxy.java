package org.isv.samples.exercises.aop;


import org.springframework.cglib.proxy.Enhancer;

import java.util.List;

public class InterceptionProxy {
    @SuppressWarnings("unchecked")
    static public <T> T createProxy(Class<T> clazz, List<InvocationInterceptor> interceptors) {
        var enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new CglibInterceptionProxy(interceptors.toArray(InvocationInterceptor[]::new)));
        return (T) enhancer.create();
    }

}
