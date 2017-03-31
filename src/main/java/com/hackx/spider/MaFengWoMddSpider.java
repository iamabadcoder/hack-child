package com.hackx.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

public class MaFengWoMddSpider {

    public static void main(String[] args) {

        Set<String> nameSet = new HashSet<>();
        nameSet.add("�����ǵ���");
        nameSet.add("ŷ�޵���");
        nameSet.add("�����޵���");
        nameSet.add("���ǵ���");
        nameSet.add("���ǵ���");
        nameSet.add("������");
        nameSet.add("���޵���");
        nameSet.add("���ǵ���");
        nameSet.add("����");
        nameSet.add("���ǵ���");

        String targetUrl = "http://360.mafengwo.cn/all.php";
        try {
            Document document = Jsoup.connect(targetUrl).get();
            Element pnlAllElement = document.select("ul#pnl_all").first();
            Elements liElements = pnlAllElement.select("li");

            for (Element liEle : liElements) {
                if (nameSet.contains(liEle.childNode(0).toString().replace("��", ""))) {
                    Elements mddElements = liEle.select("a");
                    for (Element mddEle : mddElements) {
                        System.out.println(mddEle.ownText().trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
