package cn.ys.crawler.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpGetParamTest {

    public static void main(String[] args) throws Exception {

        //1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //请求的地址为：https://search.jd.com/Search?keyword=小米&enc=utf-8&spm=2.1.0
        //创建URIBuilder
        URIBuilder uriBuilder = new URIBuilder("https://search.jd.com/Search");
        //设置参数
        uriBuilder.setParameter("keyword","小米");
        uriBuilder.setParameter("enc","utf-8");
        uriBuilder.setParameter("spm","2.1.0");

        //2.创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        System.out.println("发送的请求为：" + httpGet);

        CloseableHttpResponse response = null;
        try {
            //3.使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //4.解析响应
            if (response.getStatusLine().getStatusCode() == 200){
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //5.关闭response、httpclient
            try {

                response.close();

                httpClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
