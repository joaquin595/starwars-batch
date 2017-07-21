package com.starwars.batch.reader;

import com.starwars.batch.domain.Planet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starwars.batch.domain.Planet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by joaquinanton on 21/7/17.
 */
@Component
@StepScope
@Getter
@Setter
public class RestReader implements ItemReader<Planet>{

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    private Queue<Planet> planets;

    private String url = "http://swapi.co/api/planets/?format=json";

    @Override
    public Planet read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return getPlanets().poll();
    }



    private Queue<Planet> getPlanets() throws IOException {
        if (planets == null) {
            planets = new LinkedList<>();
            JsonNode jsonNode = requestRest(url);
            int count = jsonNode.get("count").asInt();
            String next = jsonNode.get("next").asText();
            restResult(jsonNode);

            while (!"null".equalsIgnoreCase(next)) {
                jsonNode = requestRest(next);
                restResult(jsonNode);
                next = jsonNode.get("next").asText();
            }
        }

        return planets;
    }



    private void restResult(JsonNode jsonNode) {
        JsonNode content = jsonNode.get("results");
        content.forEach(node -> {
            try {
                Planet planet = objectMapper.treeToValue(node, Planet.class);
                planets.add(planet);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private JsonNode requestRest(String url) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> forEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return objectMapper.readTree(forEntity.getBody());
    }
}
