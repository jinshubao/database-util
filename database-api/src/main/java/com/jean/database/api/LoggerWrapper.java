package com.jean.database.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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

        private static final Map<Object, Logger> LOGGER_CACHE = new HashMap<>();

        private final Object target;
        private final Logger logger;

        LoggerInvocationHandler(Object target) {
            this.target = target;
            this.logger = this.findLogger();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logger.isDebugEnabled()) {
                logger.debug("invoke[{}({})]", method.getName(), args);
            }
            return method.invoke(target, args);
        }

        private Logger findLogger() {
            if (LOGGER_CACHE.containsKey(target)) {
                return LOGGER_CACHE.get(target);
            }
            Logger logger;
            Class<?> targetClass = target.getClass();
            try {
                Field field = targetClass.getDeclaredField("logger");
                logger = this.getLoggerInstance(targetClass, field);
                cacheLogger(logger);
                return logger;
            } catch (NoSuchFieldException ignored) {

            }

            Field[] declaredFields = targetClass.getDeclaredFields();
            for (Field field : declaredFields) {
                logger = this.getLoggerInstance(targetClass, field);
                if (logger != null) {
                    cacheLogger(logger);
                    return logger;
                }
            }
            logger = LoggerFactory.getLogger(targetClass);
            cacheLogger(logger);
            return logger;
        }


        Logger getLoggerInstance(Class<?> targetClass, Field declaredField) {
            if (!Logger.class.isAssignableFrom(declaredField.getType())) {
                return null;
            }
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            try {
                return (Logger) declaredField.get(targetClass);
            } catch (IllegalAccessException ignored) {

            }
            return null;
        }

        private void cacheLogger(Logger logger) {
            LOGGER_CACHE.put(target, logger);
        }
    }
}
