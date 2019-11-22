package com.surfilter.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class HtmlUnitUtil {

    private static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    public static String getHtml(String url) {
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(1000000);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setTimeout(100000);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        String xml = null;
        try {
            HtmlPage htmlPage = webClient.getPage(url);
            DomNode domNode = htmlPage.querySelector("head > meta");
            String doc = domNode.asText();
            String textContent = htmlPage.getPage().getTextContent();
            xml = htmlPage.asText();
//            HtmlElement documentElement = htmlPage.getDocumentElement();
//            DomNode domNode = htmlPage.querySelector("head > script");
//            DomElement nextElementSibling = domNode.getNextElementSibling();
//            String asText = domNode.asText();
//            System.out.println(asText);
//            System.out.println(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    public static void main(String[] args) {
        getHtml(HttpUtil.getNewUrl("http://4aaxx.com/"));
    }
}
