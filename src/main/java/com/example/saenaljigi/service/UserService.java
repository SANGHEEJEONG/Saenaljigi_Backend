package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.UserRequest;
import com.example.saenaljigi.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String login(String loginUrl, UserRequest userRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Host", "happydorm.sejong.ac.kr");
        headers.set("Origin", "https://happydorm.sejong.ac.kr");
        headers.set("Referer", "https://happydorm.sejong.ac.kr/00/0000.kmc");
        headers.set("Sec-Fetch-Dest", "frame");
        headers.set("Sec-Fetch-Mode", "navigate");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("Sec-Fetch-User", "?1");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Google Chrome\";v=\"128\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Windows\"");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("admin_chk", "N");
        body.add("lf_locgbn", "SJ");
        body.add("id", userRequest.getStudentId().toString());
        body.add("pw", userRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        log.info("Login Request: {}", request);

        ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);

        log.info("Login Response: {}", response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            if (responseBody != null && responseBody.contains("아이디 또는 비밀번호가 일치하지않습니다.")) {
                return "로그인 실패 : 아이디 또는 비밀번호가 일치하지않습니다.";
            }

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            String listKorName = null;
            String listHakbun = null;
            try {
                JsonNode rootNode = objectMapper.readTree(responseBody);
                listKorName = rootNode.path("root").get(0).path("list_kor_name").asText();
                listHakbun = rootNode.path("root").get(0).path("list_hakbun").asText();
            } catch (Exception e) {
                log.error("JSON 파싱 에러: " + e.getMessage());
                return "JSON 파싱 실패";
            }

            log.info("Parsed Name: {}, Parsed ID: {}", listKorName, listHakbun);

            // 사용자 엔티티 처리
            User user = userRepository.findByStudentId(userRequest.getStudentId());
            if (user == null) {
                user = new User(userRequest.getStudentId(), listKorName);
                userRepository.save(user);
            }

            // JWT 토큰 생성
            return generateToken(user, listKorName, listHakbun);
        } else {
            return "로그인에 실패하였습니다";
        }
    }

    private String generateToken(User user, String name, String studentId) {
        return Jwts.builder()
                .setSubject(user.getStudentId().toString())
                .claim("name", name) // JWT에 이름 추가
                .claim("studentId", studentId) // JWT에 학번 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효기간
                .signWith(key)
                .compact();
    }
}

