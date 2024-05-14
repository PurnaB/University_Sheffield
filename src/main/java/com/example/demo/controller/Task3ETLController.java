package com.example.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class Task3ETLController {

    private static final Logger log = LoggerFactory.getLogger(Task3ETLController.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void testScheduler() {
        try {
            log.info("The time is now {}", dateFormat.format(new Date()));
            final String url = "http://localhost:8080/getall?status=active";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            List<JsonObject> list = stringToArray(result, JsonObject[].class);
            transformNProcess(list);
        } catch (Exception e) {
            log.error("No data available in the DB !!!");
        }

    }

    public <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    private void transformNProcess(List<JsonObject> list) {
        final String url = "http://localhost:8080/send";
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();
        if (list != null) {
            for (JsonObject jsonObject : list) {
                jsonObject.addProperty("name", jsonObject.get("firstname").getAsString() + " " + jsonObject.get("surname").getAsString());
                jsonObject.remove("firstname");
                jsonObject.remove("surname");
                ResponseEntity<String> result = restTemplate.postForEntity(url, gson.toJson(jsonObject), String.class);
                log.info("HTTP Response Code ::: " + result.getStatusCode() + " Message ::: " + result.getBody());
            }
        }
    }

    @PostMapping ("/send")
    public ResponseEntity<String> send (@RequestBody String json) {
        if (json != null) {
            if (json.contains("name")) {
                return new ResponseEntity<>("OK", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid data", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
        }
    }
}
