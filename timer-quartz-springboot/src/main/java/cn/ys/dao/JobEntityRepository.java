package cn.ys.dao;

import cn.ys.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobEntityRepository extends JpaRepository<JobEntity, Long> {

    JobEntity getById(Integer id);

}
