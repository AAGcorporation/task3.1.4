package ru.goryunov.springcource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import ru.goryunov.springcource.model.*;

import java.util.List;

@Component
public class Main {
    private final String url = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();

    public Main() {
        String sessionId = getAllUsers();
        headers.set("cookie", sessionId);
    }

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.addNewUser()+main.editUser()+main.deleteUser(3L));
    }

    public String getAllUsers() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        List<String> setCookieHeaders = responseEntity.getHeaders().get("Set-Cookie");
        return String.join(";", setCookieHeaders);
    }

    public String addNewUser() {
        User user = new User("James", "Brown", (byte) 11);
        user.setId(3L);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public String editUser() {
        User user = new User("Thomas", "Shelby", (byte) 22);
        user.setId(3L);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public String deleteUser(@PathVariable Long id) {
        HttpEntity<User> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
        return responseEntity.getBody();
    }
}