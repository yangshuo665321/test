package cn.ys.crawler.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrawlerTest {

    public static void main(String[] args) throws Exception {

        //1.打开浏览器，创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.输入网址，发起get请求创建HttpGet对象
        String url = "http://www.jd.com";
        HttpGet httpGet = new HttpGet(url);

        //3.按回车，发送请求，返回响应，使用HttpClient对象发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.解析响应，获取数据
        //判断状态码是否是200
        if (response.getStatusLine().getStatusCode() == 200){

            HttpEntity httpEntity = response.getEntity();

            String content = EntityUtils.toString(httpEntity, "utf8");

            System.out.println(content);
        }
    }
}
