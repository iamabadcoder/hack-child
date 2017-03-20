package com.hackx.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.trip.tripspider.extractor.domain.TrspExtractResultDO;
import com.alibaba.trip.tripspider.httpclient.domain.TrspHttpRequestParam;
import com.alibaba.trip.tripspider.httpclient.domain.enumerate.TrspHttpMethod;
import com.alibaba.trip.tripspider.httpclient.domain.enumerate.TrspProxyType;
import com.alibaba.trip.tripspider.spider.crawler.TrspCrawlerAdapter;
import com.alibaba.trip.tripspider.spider.domain.TrspSpiderJobParamDO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MaFengWoMddTopScenicSpider extends TrspCrawlerAdapter {

    public static void main(String[] args) {
        String targetUrl = "http://360.mafengwo.cn/all.php";
        try {
            Document document = Jsoup.connect(targetUrl).get();
            Element pnlAllElement = document.select("ul#pnl_all").first();
            Elements mddElements = pnlAllElement.select("a");
            for (int i = 0; i < mddElements.size(); i++) {
                Element mddEle = mddElements.get(i);
                /*String mddId = mddEle.attr("href").trim().replace(".html", "").split("/")[2];*/
                System.out.println("rankId:'" + String.valueOf(i + 1) + "',mddName:'" + mddEle.ownText().trim() + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TrspExtractResultDO crawl(TrspSpiderJobParamDO trspSpiderJobParamDO) throws Exception {
        TrspExtractResultDO trspExtractResultDO = new TrspExtractResultDO();

        /* 抓取结果 */
        JSONObject jsonObject = new JSONObject();

        /* 参数获取 */
        JSONObject crawlParam = trspSpiderJobParamDO.getParam();
        String mddId = crawlParam.getString("mddId");
        String mddName = crawlParam.getString("mddName");
        jsonObject.put("mddId", mddId);
        jsonObject.put("mddName", mddName);

        /* TOP景点抽取 */
        TrspHttpRequestParam trspHttpRequestParam = buildRequestParam();
        String targetUrl = "http://www.mafengwo.cn/ajax/router.php?sAct=KMdd_StructWebAjax%7CGetPoisByTag&iPage=1&iTagId=0&iMddid=" + mddId;
        trspHttpRequestParam.setUrl(targetUrl);
        String mddScenicResponseBody = trspHttpManager.request(trspHttpRequestParam).getBody();
        List<String> topScenic = crawTopScenic(mddScenicResponseBody);
        if (null == topScenic || topScenic.size() == 0) { /* 重试一次 */
            mddScenicResponseBody = trspHttpManager.request(trspHttpRequestParam).getBody();
            topScenic = crawTopScenic(mddScenicResponseBody);
        }
        jsonObject.put("topScenic", topScenic);

        /* 构建结果 */
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        trspExtractResultDO.setResult(jsonArray);
        return trspExtractResultDO;
    }

    public List<String> crawTopScenic(String content) {
        List<String> top10Sights = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(content);
            Document document = Jsoup.parse(JSONObject.parseObject(jsonObject.get("data").toString()).get("list").toString());

            Elements liElements = document.select("li");

            for (int i = 0; i < liElements.size(); i++) {
                Element liEle = liElements.get(i);
                String scenicName = liEle.text().trim();
                String scenicId = null;
                /*Element aEle = liEle.select("a").first();
                if (null != aEle) {
                    scenicId = aEle.attr("href").replace(".html", "").trim().split("/")[2];
                }*/
                top10Sights.add(String.valueOf(i + 1) + "####" + scenicName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return top10Sights;
    }

    public TrspHttpRequestParam buildRequestParam() {
        TrspHttpRequestParam trspHttpRequestParam = new TrspHttpRequestParam();
        trspHttpRequestParam.setTimeout(10000);
        trspHttpRequestParam.setEncoding("utf-8");
        trspHttpRequestParam.setProxyType(TrspProxyType.TRAD_ADSL);
        trspHttpRequestParam.setHttpMethod(TrspHttpMethod.GET);
        return trspHttpRequestParam;
    }
}
