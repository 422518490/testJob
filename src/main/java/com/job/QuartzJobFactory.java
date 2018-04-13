package com.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Administrator on 2016/5/18.
 */
public class QuartzJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobForm scheduleJob = (ScheduleJobForm)context.getMergedJobDataMap().get("scheduleJob");
        TaskUtils.invokeMethod(scheduleJob);
    }
}
