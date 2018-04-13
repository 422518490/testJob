package com.job;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/18.
 * @Description 定时任务实体类
 */
public class ScheduleJobForm {

    public static final String STATUS_RUNNING = "1";
    public static final String STATUS_NOT_RUNNING = "0";
    public static final String CONCURRENT_IS = "1";
    public static final String CONCURRENT_NOT = "0";

    private Integer scheduleJobId = null;
    private Integer createdBy = null;
    private Date creationDate = null;
    private Integer lastUpdatedBy = null;
    private Date lastUpdateDate = null;
    //任务名称
    private String jobName = "";
    //任务分组
    private String jobGroup = "";
    //任务状态 是否启动任务
    private String jobStatus = "";
    //cron表达式,定时任务运行时间
    private String cronExpression = "";
    //描述
    private String description = "";
    //任务执行时调用哪个类的方法 包名+类名
    private String beanClass = "";
    //任务是否有状态
    private String concurrentStatus = "";
    //spring bean
    private String springId = "";
    //任务调用的方法名
    private String methodName = "";

    public Integer getScheduleJobId() {
        return scheduleJobId;
    }

    public void setScheduleJobId(Integer scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getConcurrentStatus() {
        return concurrentStatus;
    }

    public void setConcurrentStatus(String concurrentStatus) {
        this.concurrentStatus = concurrentStatus;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
