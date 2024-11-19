package com.example.saenaljigi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
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

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36");
        headers.set("x-requested-with", "XMLHttpRequest");

        // 요청 파라미터 설정 (폼 데이터)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cPage", "1");
        params.add("rows", "10");
        params.add("bbs_locgbn", "SJ");
        params.add("bbs_id", "notice");

        // 요청 본문과 헤더 설정
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답이 200 OK인 경우 파싱 진행
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                // JSON 파싱
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode topList = root.path("root").path(0).path("topList");

                List<Map<String, String>> parsedList = new ArrayList<>();

                // 각 항목에서 title과 createdAt만 추출
                for (JsonNode item : topList) {
                    Map<String, String> noticeItem = new HashMap<>();
                    noticeItem.put("title", item.path("subject").asText());
                    noticeItem.put("createdAt", item.path("regdate").asText());
                    parsedList.add(noticeItem);
                }

                // 추출된 데이터를 JSON 문자열로 변환
                return objectMapper.writeValueAsString(parsedList);
            }
        } catch (HttpStatusCodeException e) {
            System.out.println("에러 발생! 상태 코드: " + e.getStatusCode());
            System.out.println("에러 본문: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"error\": \"데이터를 불러오는 중 문제가 발생했습니다.\"}";
    }
}
