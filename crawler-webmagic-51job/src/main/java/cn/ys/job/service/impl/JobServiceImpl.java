package cn.ys.job.service.impl;

import cn.ys.job.dao.JobDao;
import cn.ys.job.pojo.JobInfoField;
import cn.ys.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Override
    public void save(JobInfoField jobInfoField) {
        jobDao.save(jobInfoField);
    }

    @Override
    public void saveAll(List<JobInfoField> list) {
        jobDao.saveAll(list);
    }
}
