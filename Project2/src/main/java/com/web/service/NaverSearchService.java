package com.web.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.web.dto.EventDTO;

@Service
public class NaverSearchService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public List<EventDTO> getWhiskyEvents(String query) {
            String jsonResponse = searchWhiskyEvents(query);
            return filterWhiskyEvents(jsonResponse);
    }

    private String searchWhiskyEvents(String query) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        String url = "https://openapi.naver.com/v1/search/news.json?query=" + query + "&display=20";

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    private List<EventDTO> filterWhiskyEvents(String jsonResponse) {
        List<EventDTO> whiskyEvents = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray items = root.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String description = item.getString("description");
            String link = item.getString("link");
            String imageUrl = extractImageUrl(link); // 링크에서 이미지 URL 추출하는 메소드 호출
            if (title.toLowerCase().contains("위스키") || description.toLowerCase().contains("위스키")) {
                whiskyEvents.add(new EventDTO(title, description, link, imageUrl));
            }
        }
        return whiskyEvents;
    }

    private String extractImageUrl(String link) {
    	 try {
    	        Document doc = Jsoup.connect(link).get();
    	        Element imgElement = doc.select("meta[property=og:image]").first(); // Open Graph 프로토콜로 이미지 추출
    	        if (imgElement != null) {
    	            return imgElement.attr("content");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	    return null;
    	}
}
