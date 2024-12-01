package com.example.saenaljigi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {

    public String noticeCrawl() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // 요청 URL
        String url = "https://happydorm.sejong.ac.kr/bbs/getBbsList.kmc";

        // 결과를 누적할 리스트
        List<Map<String, String>> aggregatedList = new ArrayList<>();

        try {
            // 1-10페이지 반복 요청
            for (int i = 1; i <= 10; i++) {
                // 요청 헤더 설정
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36");
                headers.set("x-requested-with", "XMLHttpRequest");

                // 요청 파라미터 설정 (폼 데이터)
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("cPage", String.valueOf(i)); // 현재 페이지 번호
                params.add("rows", "10");
                params.add("bbs_locgbn", "SJ");
                params.add("bbs_id", "notice");

                // 요청 본문과 헤더 설정
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

                // POST 요청 보내기
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                // 응답이 200 OK인 경우 파싱 진행
                if (response.getStatusCode() == HttpStatus.OK) {
                    String responseBody = response.getBody();

                    // JSON 파싱
                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode topList = root.path("root").path(0).path("topList");

                    // 각 항목에서 title과 createdAt만 추출하여 누적 리스트에 추가
                    for (JsonNode item : topList) {
                        Map<String, String> noticeItem = new HashMap<>();
                        noticeItem.put("noticeId",item.path("seq").asText());
                        noticeItem.put("title", item.path("subject").asText());
                        noticeItem.put("createdAt", item.path("regdate").asText());
                        aggregatedList.add(noticeItem);
                    }
                }
            }

            // 누적된 데이터를 JSON 문자열로 변환하여 반환
            return objectMapper.writeValueAsString(aggregatedList);

        } catch (HttpStatusCodeException e) {
            System.out.println("에러 발생! 상태 코드: " + e.getStatusCode());
            System.out.println("에러 본문: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"error\": \"데이터를 불러오는 중 문제가 발생했습니다.\"}";
    }
}
