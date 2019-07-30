package jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testUrl() throws Exception{

        //解析url地址，第一个参数是访问的url；第二个参数是访问时候的超时时间
        Document document = Jsoup.parse(new URL("http://www.jd.com"), 1000);

        //使用标签选择器，获取title标签中的内容
        String title = document.getElementsByTag("title") //通过标签获取元素
                .first()                                           //选择第一个元素
                .text();                                           //获取第一个元素的文本内容

        System.out.println(title); //京东(JD.COM)-正品低价、品质保障、配送及时、轻松购物！
    }

    @Test
    public void testString() throws Exception{

        //使用工具类读取文件，获取字符串
        String content = FileUtils.readFileToString(new File("/Users/yangshuo/IdeaProjects/test/crawler-jsoup/src/main/resources/test.html"), "utf8");

        //解析字符串
        Document document = Jsoup.parse(content);

        String title = document.getElementsByTag("title").first().text();
        System.out.println(title);//你好，Crawler！
    }

    @Test
    public void testFile() throws Exception{

        //解析文件
        Document document = Jsoup.parse(new File("/Users/yangshuo/IdeaProjects/test/crawler-jsoup/src/main/resources/test.html"), "utf8");

        String title = document.getElementsByTag("title").first().text();
        System.out.println(title);//你好，Crawler！
    }
}
