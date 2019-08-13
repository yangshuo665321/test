package cn.ys.es;

import cn.ys.job.JobApp;
import cn.ys.job.pojo.JobInfo;
import cn.ys.job.pojo.JobInfoField;
import cn.ys.job.service.JobInfoService;
import cn.ys.job.service.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApp.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private JobService jobService;

    //创建索引和映射
    @Test
    public void createIndex() {
        elasticsearchTemplate.createIndex(JobInfoField.class);
        elasticsearchTemplate.putMapping(JobInfoField.class);
    }

    @Test
    public void jobInfoData() {

        //声明页码数，从 1 开始
        int p = 1;
        //声明查询到的数据条数
        int pageSize = 0;

        do {
            //从数据库中查询数据
            Page<JobInfo> page = jobInfoService.findJobInfoByPage(p, 500);

            //声明容器，存放 JobInfoField
            ArrayList<JobInfoField> list = new ArrayList<>();

            //把查询到的数据封装为 JobInfoField
            for (JobInfo jobInfo : page.getContent()) {
                //声明对象
                JobInfoField jobInfoField = new JobInfoField();
                //封装数据，复制数据
                BeanUtils.copyProperties(jobInfo, jobInfoField);

                //把封装好的数据的对象放到list中
                list.add(jobInfoField);
            }

            //把封装好的数据放到索引库中
            jobService.saveAll(list);

            //页码数加 1
            p++;

            //获取查询结果集的数据条数
            pageSize = page.getContent().size();
        } while (pageSize == 500);
    }
}
