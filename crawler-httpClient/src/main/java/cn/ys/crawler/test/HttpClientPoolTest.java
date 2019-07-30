package cn.ys.crawler.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientPoolTest {

    public static void main(String[] args) throws Exception {

        //1.创建连接池管理器
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);

        //使用连接池管理器发起请求
        doGet(cm);
        doPost(cm);
    }

    public static void doGet(PoolingHttpClientConnectionManager cm) {

        //2.从连接池获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        HttpGet httpGet = new HttpGet("http://www.jd.com");

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
            //5.关闭response
            try {

                response.close();

                //由于是连接池，不能关闭httpClient
                //httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void doPost(PoolingHttpClientConnectionManager cm) {

    }
}
