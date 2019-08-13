package cn.ys.job.service;

import cn.ys.job.pojo.JobInfoField;

import java.util.List;

public interface JobService {

    /**
     * 保存一条数据
     * @param jobInfoField
     */
    public void save(JobInfoField jobInfoField);

    /**
     * 批量保存数据
     * @param list
     */
    public void saveAll(List<JobInfoField> list);
}
