package com.example.saenaljigi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class NoticeDetailService {

    public ObjectNode noticeDetailCrawl(String seq) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

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

                // JSON 응답 데이터 파싱
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode noticeNode = rootNode.get("root").get(0); // 첫 번째 공지 가져오기

                // 전달할 JSON 형태로 재구성
                ObjectNode parsedResponse = objectMapper.createObjectNode();
                parsedResponse.put("title", noticeNode.get("subject").asText());
                parsedResponse.put("author", noticeNode.get("regname").asText());
                parsedResponse.put("content", noticeNode.get("contents").asText());
                parsedResponse.put("date", noticeNode.get("regdate").asText());
                parsedResponse.put("views", noticeNode.get("visit_cnt").asInt());

                // 파일 첨부 확인 및 추가
                parsedResponse.put("file1", noticeNode.has("file_name1") ? noticeNode.get("file_name1").asText() : null);
                parsedResponse.put("file2", noticeNode.has("file_name2") ? noticeNode.get("file_name2").asText() : null);

                // System.out.println("공지 파싱 결과: " + parsedResponse.toPrettyString());
                return parsedResponse;
            }

        } catch (HttpStatusCodeException e) {
            // HTTP 상태 코드와 응답 본문을 출력하여 디버깅
            System.out.println("에러 발생! 상태 코드: " + e.getStatusCode());
            System.out.println("에러 본문: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 에러 발생 시 빈 JSON 반환
        return objectMapper.createObjectNode();
    }
}
