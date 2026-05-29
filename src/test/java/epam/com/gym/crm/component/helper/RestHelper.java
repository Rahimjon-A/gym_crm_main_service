package epam.com.gym.crm.component.helper;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class RestHelper {

    private static final String PORT_PROPERTY = "local.server.port";
    private static final String BASE_URL      = "http://localhost:";

    private final RestTemplate restTemplate = new RestTemplate();
    private final Environment environment;

    public RestHelper(Environment environment) {
        this.environment = environment;
    }

    public <T> ResponseEntity<T> execute(String path, HttpMethod method,
                                          HttpEntity<?> entity, Class<T> responseType) {
        try {
            return restTemplate.exchange(baseUrl(path), method, entity, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
        return execute(path, HttpMethod.POST, new HttpEntity<>(body), responseType);
    }

    public <T> ResponseEntity<T> get(String path, String token, Class<T> responseType) {
        return execute(path, HttpMethod.GET, new HttpEntity<>(authHeaders(token)), responseType);
    }

    public <T> ResponseEntity<T> put(String path, Object body, String token, Class<T> responseType) {
        return execute(path, HttpMethod.PUT, new HttpEntity<>(body, authHeaders(token)), responseType);
    }

    public ResponseEntity<Void> delete(String path, String token) {
        return execute(path, HttpMethod.DELETE, new HttpEntity<>(authHeaders(token)), Void.class);
    }

    public HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    private String baseUrl(String path) {
        return BASE_URL + environment.getProperty(PORT_PROPERTY) + path;
    }
}
