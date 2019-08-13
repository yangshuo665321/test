package cn.ys.job.dao;

import cn.ys.job.pojo.JobInfoField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface JobDao extends ElasticsearchRepository<JobInfoField, Long> {
}
