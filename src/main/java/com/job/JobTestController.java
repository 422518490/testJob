package com.job;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/19.
 */
@RestController
@EnableAutoConfiguration
public class JobTestController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean = null;
    @Autowired
    private JobTest jobTest = null;
    @Autowired
    private JobTestDao jobTestDao = null;

    /**
     * 获取所有计划中的任务列表
     * @return
     * @throws SchedulerException
     */
    @RequestMapping(value = "/test/getAllJob",method = RequestMethod.GET)
    public List<ScheduleJobForm> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> groupMatcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
        List<ScheduleJobForm> scheduleJobForms = new ArrayList<ScheduleJobForm>();
        for(JobKey jobKey : jobKeys){
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for(Trigger trigger : triggers){
                ScheduleJobForm jobForm = new ScheduleJobForm();
                jobForm.setJobName(jobKey.getName());
                jobForm.setJobGroup(jobKey.getGroup());
                jobForm.setDescription("触发器:"+trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobForm.setJobStatus(triggerState.name());
                if(trigger instanceof CronTrigger){
                    CronTrigger cronTrigger = (CronTrigger)trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    jobForm.setCronExpression(cronExpression);
                }
                scheduleJobForms.add(jobForm);
            }
        }
        return scheduleJobForms;
    }

    /**
      * 所有正在运行的job
     * @return
     * @throws SchedulerException
    */
   public List<ScheduleJobForm> getRunningJob() throws SchedulerException {
       Scheduler scheduler = schedulerFactoryBean.getScheduler();
       List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
       List<ScheduleJobForm> jobList = new ArrayList<ScheduleJobForm>(executingJobs.size());
       for (JobExecutionContext executingJob : executingJobs) {
           ScheduleJobForm job = new ScheduleJobForm();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
       }
       return jobList;
   }

    /**
     * 暂停一个job
     * @param scheduleJob
     * @throws SchedulerException
    */
    public void pauseJob(ScheduleJobForm scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /* 恢复一个job
    * @param scheduleJob
    * @throws SchedulerException
    */
    public void resumeJob(ScheduleJobForm scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     * @param scheduleJob
     * @throws SchedulerException
   */
   public void deleteJob(ScheduleJobForm scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);
   }

    /**
     * 立即执行job
     * @param scheduleJob
     * @throws SchedulerException
    */
    public void runAJobNow(ScheduleJobForm scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     * @param scheduleJob
     * @throws SchedulerException
    */
    public void updateJobCron(ScheduleJobForm scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //判断时间表达式是否正确
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    @RequestMapping(value = "/test/addJob/{jobName}/{jobGroup}")
    public void addNewJob(@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup) throws SchedulerException {
        ScheduleJobForm scheduleJobForm = new ScheduleJobForm();
        scheduleJobForm.setJobName(jobName);
        scheduleJobForm.setJobGroup(jobGroup);
        scheduleJobForm = jobTestDao.getJobByNameAndGroup(scheduleJobForm);
        jobTest.addJob(scheduleJobForm);
    }

}
