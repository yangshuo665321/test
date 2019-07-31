package cn.ys.jd.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;

    public HttpUtils(){
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url){

        //1.获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2.创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        //给请求设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        try {
            //3.使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //4.解析响应
            if (response.getStatusLine().getStatusCode() == 200){

                //判断响应体Entity是否不为空，如果不为空，就可以使用EntityUtils
                if (response.getEntity() != null){
                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //5.关闭response
            try {

                if (response != null){
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url){

        //1.获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2.创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        //给请求设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        try {
            //3.使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //4.解析响应
            if (response.getStatusLine().getStatusCode() == 200){

                //判断响应体Entity是否不为空，如果不为空，就可以使用EntityUtils
                if (response.getEntity() != null){
                    //下载图片
                    //获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));

                    //创建图片名，重命名图片
                    String picName = UUID.randomUUID().toString() + extName;

                    //下载图片
                    //声明OutputStream
                    OutputStream outputStream = new FileOutputStream(new File("/Users/yangshuo/jd/" + picName));
                    response.getEntity().writeTo(outputStream);

                    //返回图片名称
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //5.关闭response
            try {

                if (response != null){
                    response.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //如果下载失败，返回空串
        return "";
    }

    //配置请求信息
    private RequestConfig getConfig() {
        //配置请求信息
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)             //创建连接的最长时间，单位：ms
                .setConnectionRequestTimeout(500)    //设置获取连接的最长时间，单位：ms
                .setSocketTimeout(10*1000)           //设置数据传输的最长时间，单位：ms
                .build();
        return config;
    }
}
