package com.surfilter.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanContext implements ApplicationContextAware{
    private  static ApplicationContext applicationContext;
    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        // TODO Auto-generated method stub
        this.applicationContext = arg0;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    @SuppressWarnings("unchecked")
    public  static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public  static <T> T getBean(Class<T> clz) throws BeansException {
        return (T) applicationContext.getBean(clz);
    }

}

