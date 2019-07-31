package cn.ys.jd.task;

import cn.ys.jd.pojo.Item;
import cn.ys.jd.service.ItemService;
import cn.ys.jd.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    //解析工具类
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Scheduled(fixedDelay = 100*1000)//当下载任务完成后，间隔多长时间进行下一次的任务
    public void itemTask() throws Exception {

        //声明需要解析的初始地址
        String url = "https://search.jd.com/Search?" +
                "keyword=手机&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=手机" +
                "&cid2=653&cid3=655&s=53&click=0&page=";//京东手机页面的page请求参数为1时是第一页，为3时为第二页，以此类推

        //按照页面对手机的搜索结果进行遍历解析
        for (int i = 1; i < 10; i = i + 2){
            String html = httpUtils.doGetHtml(url + i);

            System.out.println(html);

            //解析页面，获取商品数据并存储
            this.parse(html);
        }

        System.out.println("手机数据抓取完成！");
    }

    //解析页面，获取商品数据并存储
    private void parse(String html) throws Exception {

        //解析 html 获取 Document
        Document document = Jsoup.parse(html);

        //获取spu，通过组合选择器再找直接子元素 ul，再找直接子元素 li
        Elements spuEles = document.select("div#J_goodsList > ul > li");

        for (Element spuEle : spuEles) {
            //获取spu，由于数据库里面存储的是Long
            long spu = Long.parseLong(spuEle.attr("data-spu"));

            //获取sku信息
            Elements skuEles = spuEle.select("li.ps-item");

            for (Element skuEle : skuEles) {
                //获取sku，由于数据库里面存储的是Long
                long sku = Long.parseLong(skuEle.select("[data-sku]").attr("data-sku"));

                //根据sku查询商品数据
                Item item = new Item();
                item.setSku(sku);
                List<Item> list = this.itemService.findAll(item);

                if (list.size() > 0) {
                    //如果商品存在，就进行下一次循环
                    continue;
                }

                //设置商品的spu
                item.setSpu(spu);

                //获取商品的详情url
                String itemUrl = "https://item.jd.com/" + sku + ".html";
                item.setUrl(itemUrl);

                //获取商品的图片
                String picUrl = "https:" + skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                picUrl = picUrl.replace("/n9/", "/n1/");//获得的原始的n9图片小，换成n1就变大了
                String picName = this.httpUtils.doGetImage(picUrl);
                item.setPic(picName);

                //获取商品的价格
                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);

                //获取商品的标题
                String itemInfo = this.httpUtils.doGetHtml(item.getUrl());
                String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
                item.setTitle(title);

                item.setCreated(new Date());
                item.setUpdated(item.getCreated());

                //保存商品数据到数据库
                this.itemService.save(item);
            }

        }
    }
}
