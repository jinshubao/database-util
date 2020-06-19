package com.jean.database.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 日志动态代理
 *
 * @author jinshubao
 */
public class LoggerWrapper {

    @SuppressWarnings("unchecked")
    public static <T> T warp(T instance) {
        LoggerInvocationHandler invocationHandler = new LoggerInvocationHandler(instance);
        return (T) Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(), invocationHandler);
    }


    private static class LoggerInvocationHandler implements InvocationHandler {

        private Object target;

        private Logger logger;

        LoggerInvocationHandler(Object target) {
            this.target = target;
            logger = LoggerFactory.getLogger(target.getClass().getName());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logger.isDebugEnabled()) {
                logger.debug("invoke[{}({})]", method.getName(), args);
            }
            return method.invoke(target, args);
        }
    }

}
