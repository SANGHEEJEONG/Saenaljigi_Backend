package service;

import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.select.Elements;

@Service
public class NoticeService {

    @PostConstruct
    public void noticeCrawl() throws IOException{
        String NOTICE_URL = "https://happydorm.sejong.ac.kr/60/6010.kmc";

        Document doc = Jsoup.connect(NOTICE_URL).get();
        System.out.println(doc);

        Elements elements = doc.select("#list");

        String[] str = elements.text().split(" ");

        System.out.println(str.length);
        for(int i =0;i<str.length;i++) {
            System.out.println(str[i]);
        }
    }
}
