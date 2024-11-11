package com.example.saenaljigi;

import com.example.saenaljigi.service.NoticeDetailService;
import com.example.saenaljigi.service.NoticeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaenaljigiApplication implements CommandLineRunner {

    private final NoticeService noticeService;
    private final NoticeDetailService noticeDetailService;

    public SaenaljigiApplication(NoticeService noticeService, NoticeDetailService noticeDetailService) {
        this.noticeService = noticeService;
        this.noticeDetailService = noticeDetailService;
    }


    public static void main(String[] args) {
        SpringApplication.run(SaenaljigiApplication.class, args);
    }

    // CommandLineRunner 인터페이스의 run() 메서드를 구현하여 애플리케이션 시작 시 실행될 로직을 추가
    @Override
    public void run(String... args) throws Exception {
        // 애플리케이션 실행 시 NoticeService의 noticeCrawl 메서드 호출
        noticeService.noticeCrawl();
        noticeDetailService.noticeDetailCrawl("35946");
    }
}
