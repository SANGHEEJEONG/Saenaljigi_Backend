package com.example.saenaljigi.service;

import com.example.saenaljigi.repository.PointRepository;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserService userService;
    private final PointRepository pointRepository;

    public PointService(UserService userService, PointRepository pointRepository) {
        this.userService = userService;
        this.pointRepository = pointRepository;
    }

//    public void fetchAndSavePoints(String session) {
//        // 1. 요청 URL 설정
//        String url = "https://happydorm.sejong.ac.kr/mobile/getDemeritList.kmc";
//
//        // 2. 요청 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
//        headers.add("Accept-Encoding", "gzip, deflate, br, zstd");
//        headers.add("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
//        headers.add("Connection", "keep-alive");
//        headers.add("Cookie", session); // 동적으로 받은 session 값 설정
//        headers.add("Origin", "https://happydorm.sejong.ac.kr");
//        headers.add("Referer", "https://happydorm.sejong.ac.kr/m/pages/penalty.jsp");
//        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
//        headers.add("X-Requested-With", "XMLHttpRequest");
//
//        // 3. 요청 파라미터 설정 (폼 데이터)
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("locgbn", "SJ");
//        params.add("cPage", "1");
//        params.add("rows", "10");
//        params.add("list_type", "mypage");
//
//        // 4. 요청 본문과 헤더 설정
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
//        // 5. RestTemplate을 사용해 요청 보내기
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
//
//        // 6. 응답 데이터 처리
//        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//            Map<String, Object> data = response.getBody();
//            List<Map<String, Object>> root = (List<Map<String, Object>>) data.get("root");
//            if (root != null && !root.isEmpty()) {
//                Map<String, Object> firstRoot = root.get(0);
//
//                // 7. totalSum 처리
//                List<Map<String, Object>> totalSumList = (List<Map<String, Object>>) firstRoot.get("totalSum");
//                if (totalSumList != null && !totalSumList.isEmpty()) {
//                    Map<String, Object> totalSum = totalSumList.get(0);
//                    int totalCnt = (int) totalSum.get("total");
//                    int reward = (int) totalSum.get("merit");
//                    int penalty = (int) totalSum.get("demerit");
//
//                    System.out.println("TotalCnt: " + totalCnt + ", Reward: " + reward + ", Penalty: " + penalty);
//                    // 필요한 로직 추가 (예: DB 저장)
//                }
//
//                // 8. list 데이터 처리
//                List<Map<String, Object>> list = (List<Map<String, Object>>) firstRoot.get("list");
//                if (list != null) {
//                    for (Map<String, Object> listItem : list) {
//                        String date = (String) listItem.get("pe_pdate");
//                        String username = (String) listItem.get("pe_name");
//                        int point = (int) listItem.get("pe_jumsu");
//                        String type = (String) listItem.get("pe_gbn_name");
//
//                        // User 조회 및 Point 저장
//                        User user = userService.findByUsername(username);
//
//                        if (user != null) {
//                            // Point 객체를 빌더 패턴으로 생성
//                            Point pointEntity = Point.builder()
//                                    .date(LocalDate.parse(date))
//                                    .user(user)
//                                    .point(point)
//                                    .type(type)
//                                    .build();
//
//                            pointRepository.save(pointEntity);
//                        } else {
//                            System.out.println("User not found: " + username);
//                        }
//
//                    }
//                }
//            }
//        } else {
//            System.out.println("Failed to fetch data. Status: " + response.getStatusCode());
//        }
//    }

    public void fetchAndSavePoints(String session) {
        String url = "https://happydorm.sejong.ac.kr/mobile/getDemeritList.kmc";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            // 설정된 헤더
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01");
            httpPost.setHeader(HttpHeaders.COOKIE, session);
            httpPost.setHeader("X-Requested-With", "XMLHttpRequest");

            // 폼 데이터 설정
            String formData = "locgbn=SJ&cPage=1&rows=10&list_type=mypage";
            httpPost.setEntity(new StringEntity(formData));

            // 요청 실행
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 상태 코드와 응답 내용 확인
                System.out.println("Status Code: " + response.getCode());
                System.out.println("Headers: " + response.getHeaders());
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), "UTF-8");
                System.out.println("Response Body: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

