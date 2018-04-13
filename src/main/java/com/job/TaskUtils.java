package com.job;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/5/18.
 * @Description 用于启动任务
 */
public class TaskUtils {
    public final static Logger log = Logger.getLogger(TaskUtils.class);
    public static void invokeMethod(ScheduleJobForm scheduleJob){
        Object object = null;
        Class clazz = null;
        if(StringUtils.isBlank(scheduleJob.getSpringId())){
            //根据spirng bean id 获取Java类
            clazz = SpringUtils.getBean(scheduleJob.getSpringId());
        }else if(StringUtils.isNotBlank(scheduleJob.getBeanClass())){
            try {
                //根据类全路径名获取Java类
                clazz = Class.forName(scheduleJob.getBeanClass());
                //实例化类
                object = clazz.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(object == null){
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
            return;
        }

        clazz = object.getClass();
        Method method = null;
        try {
            //判断特定的方法来反射获取方法
            if(scheduleJob.getMethodName().equals("testVariableJob")){
                method = clazz.getDeclaredMethod(scheduleJob.getMethodName(),String.class);
            }else {
                method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
            }
        } catch (NoSuchMethodException e) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
            e.printStackTrace();
        }
        if(method != null){
            try {
                //判断特定的方法是否传参等，同时启动方法
                if(scheduleJob.getMethodName().equals("testVariableJob")){
                    method.invoke(object,"参数测试");
                }else{
                    method.invoke(object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }
}
