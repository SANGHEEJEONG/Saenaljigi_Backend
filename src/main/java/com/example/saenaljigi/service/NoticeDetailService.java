package com.example.saenaljigi.service;


import org.springframework.http.*;
        import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class NoticeDetailService {

    public void noticeDetailCrawl(String seq) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 URL
        String url = "https://happydorm.sejong.ac.kr/bbs/getBbsView.kmc";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36");
        headers.set("x-requested-with", "XMLHttpRequest");

        // 요청 파라미터 설정 (폼 데이터)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("seq", seq);           // 게시글 고유 번호
        params.add("bbs_locgbn", "SJ");   // 게시판 위치 구분
        params.add("bbs_id", "notice");   // 게시판 ID

        // 요청 본문과 헤더 설정
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답이 200 OK인 경우
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                System.out.println("상세 페이지 응답 데이터: " + responseBody);
            }

        } catch (HttpStatusCodeException e) {
            // HTTP 상태 코드와 응답 본문을 출력하여 디버깅
            System.out.println("에러 발생! 상태 코드: " + e.getStatusCode());
            System.out.println("에러 본문: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
