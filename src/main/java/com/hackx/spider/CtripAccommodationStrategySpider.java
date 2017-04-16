package com.hackx.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.trip.tripspider.downloader.domain.exception.TrspDownloadException;
import com.alibaba.trip.tripspider.extractor.domain.TrspExtractResultDO;
import com.alibaba.trip.tripspider.extractor.domain.exception.TrspExtractException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CtripAccommodationStrategySpider extends TrspCrawlerAdapter {

    public static void main(String[] args) {
        crawlCitylist();
    }

    public static void crawlCitylist() {
        String entryUrl = "http://you.ctrip.com/place/countrylist.html";
        try {
            Document document = Jsoup.connect(entryUrl).get();
            Elements countrylistElements = document.select("div.countrylist");
            for (Element countrylistEle : countrylistElements) {
                for (Element liEle : countrylistEle.select("li")) {
                    Element hrefEle = liEle.select("a").first();
                    if (null != hrefEle) {
                        String[] partsArr = hrefEle.attr("href").split("/");
                        System.out.println("cityNameEn:'" + partsArr[partsArr.length - 1].replace(".html", "") + "'," + "cityNameCn:'" + hrefEle.ownText() + "'");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TrspExtractResultDO crawl(TrspSpiderJobParamDO trspSpiderJobParamDO) throws TrspExtractException, TrspDownloadException, Exception {
        TrspExtractResultDO trspExtractResultDO = new TrspExtractResultDO();

        /* 抓取结果 */
        JSONObject jsonObject = new JSONObject();

        /* 参数获取 */
        JSONObject crawlParam = trspSpiderJobParamDO.getParam();
        String cityNameEn = crawlParam.getString("cityNameEn");
        String cityNameCn = crawlParam.getString("cityNameCn");
        jsonObject.put("cityNameEn", cityNameEn);
        jsonObject.put("cityNameCn", cityNameCn);

        /* TOP景点抽取 */
        TrspHttpRequestParam trspHttpRequestParam = buildRequestParam();
        String targetUrl = "http://you.ctrip.com/hotels/" + cityNameEn + "/map.html";
        trspHttpRequestParam.setUrl(targetUrl);
        String cityHotelMapBody = trspHttpManager.request(trspHttpRequestParam).getBody();
        List<Map<String, String>> regionAccommodationStrategy = crawlRegionAccommodationStrategy(cityHotelMapBody);
        if (null == regionAccommodationStrategy || regionAccommodationStrategy.size() == 0) { /* 重试一次 */
            cityHotelMapBody = trspHttpManager.request(trspHttpRequestParam).getBody();
            regionAccommodationStrategy = crawlRegionAccommodationStrategy(cityHotelMapBody);
        }
        jsonObject.put("regionAccommodationStrategy", regionAccommodationStrategy);

        /* 构建结果 */
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        trspExtractResultDO.setResult(jsonArray);
        return trspExtractResultDO;
    }

    public List<Map<String, String>> crawlRegionAccommodationStrategy(String cityHotelMapBody) {
        List<Map<String, String>> regionAccommodationStrategy = new ArrayList<>();
        try {
            Document document = Jsoup.parse(cityHotelMapBody);
            List<String> regionDescs = new ArrayList<>();
            for (Element descriptionEle : document.select("div.description")) {
                Element h3Ele = descriptionEle.select("h3").first();
                if (null == h3Ele) continue;
                Element pEle = descriptionEle.select("p").first();
                if (null == pEle) continue;
                if (h3Ele.ownText().contains("全部")) continue;
                Map<String, String> regionMap = new HashMap<>();
                regionMap.put("regionName", h3Ele.ownText().replace("住宿", ""));
                regionMap.put("regionDesc", pEle.ownText());
                regionAccommodationStrategy.add(regionMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regionAccommodationStrategy;
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
