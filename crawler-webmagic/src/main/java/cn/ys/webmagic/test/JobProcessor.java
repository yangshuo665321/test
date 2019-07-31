package cn.ys.webmagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.RedisScheduler;

public class JobProcessor implements PageProcessor {

    //解析页面
    public void process(Page page) {

        //解析返回的数据page，并且把解析的结果放到ResultItems中
        page.putField("div", page.getHtml().css("div.unin_reason_dialog h3").all());

        //XPath
        page.putField("div2",page.getHtml().xpath("//div[@class=title]/h2/a"));

        //正则表达式
        page.putField("div3",page.getHtml().css("div.title h2 a").regex(".*数学.*").all());

        //获取链接
        page.addTargetRequests(page.getHtml().css("div.title").links().regex(".*9$").all());//以9结尾的
        page.putField("url",page.getHtml().css("div.article-title-box h1"));
    }

    private Site site = Site.me();
    public Site getSite() {
        return site;
    }

    //主函数，执行爬虫
    public static void main(String[] args) {
        Spider.create(new JobProcessor())
                .addUrl("https://blog.csdn.net/nav/ai")  //设置爬取数据的页面
//                .addPipeline(new ConsolePipeline())
//                .addPipeline(new FilePipeline("/Users/yangshuo/jd/")) //设置结果输出到文件
                .addPipeline(new JsonFilePipeline("/Users/yangshuo/jd/")) //以 JSON 方式保存
                .thread(5)                               //设置5个线程执行
//                .setScheduler(new QueueScheduler())      //设置内存队列
//                .setScheduler(new FileCacheQueueScheduler("/Users/yangshuo/jd/")) //设置文件队列
                .setScheduler(new RedisScheduler("127.0.0.1")) //设置Redis队列
                .run();                                  //执行爬虫
    }
}
