package com.job;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/18.
 */
@Service
@ImportResource("config/spring.xml")
public class JobTest {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean = null;
    @Autowired
    private JobTestDao jobTestDao = null;
    //@PostConstruct
    public void testJobBegin() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
         // 这里从数据库中获取任务信息数据
        List<ScheduleJobForm> jobList = jobTestDao.getAll();
        for (ScheduleJobForm job : jobList) {
            addJob(job);
         }

    }

    public void testJob(){
        System.out.println("123--"+new Date());
    }

    public void testVariableJob(String var) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("456---"+var);
    }

    public void addJob(ScheduleJobForm job) throws SchedulerException {
        System.out.print("begin....");
        if(job == null || ScheduleJobForm.STATUS_RUNNING.equals(job.getJobStatus())){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据任务名称和任务组组成唯一的triggerKey
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(),job.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        //查看是否已经存在该任务
        if(cronTrigger == null){
            Class clazz = ScheduleJobForm.CONCURRENT_IS.equals(job.getConcurrentStatus()) ? QuartzJobFactory.class:QuartzJobFactoryDisallowConcurrentExecution.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(),job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob",job);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(),job.getJobGroup()).withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail,cronTrigger);
        }else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            // 按新的cronExpression表达式重新构建trigger
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
           scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
    }


}
