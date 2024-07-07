package projectx.backend.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projectx.backend.service.StockService;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

@RestController
public class ApiClient {

    @Autowired
    private StockService stockService;

    @PostMapping("/python-api-predict")
    public String getStockData(
            @RequestParam String stockCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String apiUrl = "http://34.64.213.105:6000/predict"; // API URL을 여기에 입력하세요

        String stockData = stockService.getStockDataByDateRange(stockCode, startDate, endDate);
        String jsonInputString = stockData;
        //System.out.println(stockData);;


//        String jsonInputString = "{ \"data\": ["
//                + "{\"Date\": \"2023-07-01\", \"고가\": 63000, \"저가\": 62000, \"종가\": 62500, \"거래량\": 1234567},"
//                + "{\"Date\": \"2023-07-02\", \"고가\": 64000, \"저가\": 63000, \"종가\": 63500, \"거래량\": 2345678},"
//                + "{\"Date\": \"2023-07-03\", \"고가\": 65000, \"저가\": 64000, \"종가\": 64500, \"거래량\": 3456789},"
//                + "{\"Date\": \"2023-07-04\", \"고가\": 66000, \"저가\": 65000, \"종가\": 65500, \"거래량\": 4567890},"
//                + "{\"Date\": \"2023-07-05\", \"고가\": 70000, \"저가\": 30000, \"종가\": 40000, \"거래량\": 5678901}"
//                + "] }"; // 전송할 JSON 데이터를 여기에 입력하세요

        try {
            // URL 객체 생성
            URL url = new URL(apiUrl);
            // HttpURLConnection 객체 생성 및 초기화
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // JSON 데이터 전송
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 응답 코드 확인
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 응답 데이터 읽기
            try (Scanner scanner = new Scanner(con.getInputStream(), "UTF-8")) {
                String responseBody = scanner.useDelimiter("\\A").next();
                return responseBody;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
