package cn.ys.job.task;

import cn.ys.job.pojo.JobInfo;
import cn.ys.job.util.MathSalary;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    @Autowired
    private SpringDataPipeline springDataPipeline;

    private String url = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,java,2,1.html?" +
            "lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99" +
            "&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9" +
            "&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override
    public void process(Page page) {

        //解析页面，获取招聘信息详情的url地址
        //找div#resultList下面的子元素div.el获得招聘信息div集合
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();

        //判断获取到的集合是否为空
        if (list.size() == 0) {
            //如果为空，表示为招聘详情页，解析页面获取招聘详情信息，保存数据
            this.saveJobInfo(page);
        } else {
            //如果不为空，表示这是列表页，解析出详情页的url地址，放到任务队列中
            for (Selectable selectable : list){
                //获取url地址
                String jobInfoUrl = selectable.links().toString();
                //把获取到的url地址放到任务队列中
                page.addTargetRequest(jobInfoUrl);
            }

            //获取下一页的url
            String bkUrl = page.getHtml().css("div.p_in ul li.bk").nodes().get(1).links().toString();
            //把url放到任务队列中
            page.addTargetRequest(bkUrl);
        }

        String html = page.getHtml().toString();
    }

    //解析页面获取招聘详情信息，保存数据
    private void saveJobInfo(Page page) {

        //创建招聘详情对象
        JobInfo jobInfo = new JobInfo();

        //解析页面
        Html html = page.getHtml();

        //获取数据，封装到对象中
        jobInfo.setCompanyName(html.css("div.cn p.cname a","text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        jobInfo.setJobAddr(html.css("div.cn p.msg","text").toString());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setUrl(page.getUrl().toString());

        //TODO
        //获取薪资
//        Integer[] salary = MathSalary.getSalary(html.css("div.cn strong", "text").toString());
//        jobInfo.setSalaryMin(salary[0]);
//        jobInfo.setSalaryMax(salary[1]);
//        jobInfo.setTime();

        //把结果保存起来
        page.putField("jobInfo", jobInfo);

    }

    private Site site = Site.me()
            .setCharset("gbk")//设置编码,适应页面
            .setTimeOut(10*1000)//设置超时时间
            .setRetrySleepTime(3000)//设置重试时间
            .setRetryTimes(3);//设置重试次数

    @Override
    public Site getSite() {
        return site;
    }

    //initialDelay 当任务启动后，等待多久执行方法
    //fixedDelay 每个多久执行方法
    @Scheduled(initialDelay = 1000, fixedDelay = 100*1000)
    public void process() {

        System.out.println(url);
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(10*10000)))
                .thread(10)
                .addPipeline(this.springDataPipeline)
                .run();

    }
}
