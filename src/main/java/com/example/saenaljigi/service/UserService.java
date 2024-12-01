package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.UserRequest;
import com.example.saenaljigi.dto.UserResponse;
import com.example.saenaljigi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final CalendarService calendarService;


    public User isLoginSuccess(String loginUrl, UserRequest userRequest) throws LoginException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Host", "happydorm.sejong.ac.kr");
        headers.set("Origin", "https://happydorm.sejong.ac.kr");
        headers.set("Referer", "https://happydorm.sejong.ac.kr/00/0000.kmc");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("admin_chk", "N");
        body.add("lf_locgbn", "SJ");
        body.add("id", userRequest.getUsername());
        body.add("pw", userRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

                if (responseBody != null && responseBody.contains("아이디 또는 비밀번호가 일치하지않습니다.")) {
                    throw new LoginException();
                }

                User user = userRepository.findByUsername(userRequest.getUsername());

                if (user == null) {
                    user = User.builder()
                            .username(userRequest.getUsername())
                            .mealCnt(150L)
                            .build();
                    userRepository.save(user);
                    calendarService.copySystemCalendarsToUser(user);
                }

                return user;
            } else {
                throw new LoginException();
            }
        } catch (Exception e) {
            log.error("로그인 요청 중 오류 발생", e);
            throw new LoginException();
        }
    }




}

