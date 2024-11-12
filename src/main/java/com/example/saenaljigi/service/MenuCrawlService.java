package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.dto.FoodDto;
import com.example.saenaljigi.dto.MenuDto;
import com.example.saenaljigi.util.FoodTime;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.saenaljigi.util.FoodTime.석식;
import static com.example.saenaljigi.util.FoodTime.중식;

@Service
@RequiredArgsConstructor
public class MenuCrawlService {
    private static final Logger logger = LoggerFactory.getLogger(MenuCrawlService.class);

    private final MenuService menuService;
    private final CalendarService calendarService;


    private static final String HAPPY_DORM_DATAS_URL = "https://happydorm.sejong.ac.kr/60/6050.kmc";

//    @Scheduled(fixedDelay = 180000) // 일정 시간마다 실행되도록 설정
    @Scheduled(cron = "0 0 3 ? * MON")
    public void scheduledCrawlMenu() throws IOException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(HAPPY_DORM_DATAS_URL);

            WebDriverWait jsWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            jsWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            // JavaScript로 강제로 클릭
            WebElement selectedTab = jsWait.until(ExpectedConditions.presenceOfElementLocated(By.id("tabDayA")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedTab);
            int day = 1;
            while (day<8) {
                String lunchId = "fo_menu_lun" + day;
                String dinnerId = "fo_menu_eve" + day;
                String dateId = "vDate" + day;

                // 날짜 텍스트를 가져와 Calendar에 해당 날짜가 있는지 확인
                String dateText = driver.findElement(By.id(dateId)).getText();
                Calendar calendar = getCalendarByDate(dateText);

                // 중식 중복 확인 후 저장
                if (!menuService.existsByCalendarAndFoodTime(calendar, 중식)) {
//                    String lunchText = driver.findElement(By.id(lunchId)).getText();
                    parseAndSaveMeal(driver,lunchId,"중식", calendar);
                }

                // 석식 중복 확인 후 저장
                if (!menuService.existsByCalendarAndFoodTime(calendar, 석식)) {
//                    String dinnerText = driver.findElement(By.id(dinnerId)).getText();
                    parseAndSaveMeal(driver,dinnerId, "석식", calendar);
                }

                day++;

//                try {
//                    // 요일과 날짜를 가져오기
//                    String dateId = "vDate" + day;
//                    WebElement dateElement = driver.findElement(By.id(dateId));
//                    String dateText = dateElement.getText(); // 날짜 추출
//                    Calendar calendar = getCalendarByDate(dateText); // Calendar 엔티티 가져오기 또는 생성
//
//                    // 조식, 중식, 석식 메뉴 크롤링
////                    parseAndSaveMeal(driver, "fo_menu_mor" + day, "조식", calendar); // 조식
//                    parseAndSaveMeal(driver, "fo_menu_lun" + day, "중식", calendar); // 중식
//                    parseAndSaveMeal(driver, "fo_menu_eve" + day, "석식", calendar); // 석식
//
//                    day++;
//                } catch (Exception e) {
//                    logger.error("Error processing day {}: {}", day, e.getMessage(), e);
//                    break;
//                }
            }

        } catch (Exception e) {
            logger.error("Error during scheduledCrawlMenu execution: {}", e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    private void parseAndSaveMeal(WebDriver driver, String mealId, String mealType, Calendar calendar) {
        try {
            WebElement mealElement = driver.findElement(By.id(mealId));
            String mealText = mealElement.getText(); // 메뉴 텍스트 가져오기
            MenuDto menuDto = MenuDto.builder()
                    .foodTime(mealType)
                    .isCheck(false)
                    .calendarId(calendar.getId())
                    .foods(parseFoods(mealText))
                    .build();
            menuService.createMenu(menuDto);

            logger.info("Saved {} menu for date {}: {}", mealType, calendar.getDay(), menuDto);
        } catch (Exception e) {
            logger.error("Error parsing {} menu for date {}: {}", mealType, calendar.getDay(), e.getMessage());
        }
    }


    private Calendar getCalendarByDate(String dateText) {
        dateText = dateText.trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        LocalDate date = LocalDate.parse(dateText, formatter);
        return calendarService.getOrCreateCalendarByDate(date);
    }


    private List<FoodDto> parseFoods(String foodsText) {
        List<FoodDto> foodDtos = new ArrayList<>();
        if (foodsText == null || foodsText.trim().isEmpty()) {
            return foodDtos;
        }
        String[] foodsArray = foodsText.split(" "); // 공백으로 구분된 음식 목록
        for (String foodName : foodsArray) {
            FoodDto foodDto = FoodDto.builder()
                    .foodName(foodName.trim())
                    .isSelected(false) // 초기 상태 설정
                    .build();
            foodDtos.add(foodDto);
        }
        return foodDtos;
    }


//    private void addFixedBreakfastMenus() {
//        // 모든 Calendar를 조회하여 조식 메뉴 추가
//        List<Calendar> calendars = calendarService.getAllCalendars();
//        for (Calendar calendar : calendars) {
//            MenuDto breakfastMenu = MenuDto.builder()
//                    .foodTime("조식")
//                    .isCheck(false)
//                    .calendarId(calendar.getId())  // 필요 시 Calendar ID를 직접 참조
//                    .foods(List.of(
//                            FoodDto.builder()
//                                    .foodName("고정 조식 메뉴1")
//                                    .isSelected(false)
//                                    .build(),
//                            FoodDto.builder()
//                                    .foodName("고정 조식 메뉴2")
//                                    .isSelected(false)
//                                    .build()
//                    ))
//                    .build();
//            menuService.createMenu(breakfastMenu);
//        }
//    }
}
