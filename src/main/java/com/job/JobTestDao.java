package com.job;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */

public interface JobTestDao {

    public  List<ScheduleJobForm> getAll();

    public ScheduleJobForm getJobByNameAndGroup(ScheduleJobForm scheduleJobForm);
}
