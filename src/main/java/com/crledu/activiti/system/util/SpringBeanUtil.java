package com.crledu.activiti.system.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 获取spring中注入的bean
 ************************************************************
 * @CreatedBy: yhy on 2018年7月9日下午7:41:38
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {  

 
    private static ApplicationContext applicationContext;  

    
    @Override  
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;  
    }  


    public static Object getObject(String beanName) {  
        Object object = null;  
        object = applicationContext.getBean(beanName);  
        return object;  
    }
    @SuppressWarnings("unchecked")
	public static Object getObjectByClass(Class clazz) {  
        Object object = null;  
        object = applicationContext.getBean(clazz);
        return object;  
    } 
} 