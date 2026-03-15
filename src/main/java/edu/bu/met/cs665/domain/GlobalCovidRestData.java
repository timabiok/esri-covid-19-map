package edu.bu.met.cs665.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;


/**
 * Loads global COVID-19 data from a REST API at startup and exposes it via a CompletableFuture
 * for safe consumption on the UI thread.
 *
 * @author tim_abiok
 * @course CS-665
 * @term Summer 2
 * @assignment PROJECT
 * @date 20 AUG 2020
 */
@Controller
public class GlobalCovidRestData implements RestViaHttp {

  private static final Logger log = Logger.getLogger(GlobalCovidRestData.class);

  @Value("${covid.api.url:https://corona.lmao.ninja/v2/countries?yesterday=&sort=}")
  private String apiUrl;

  @Value("${covid.api.timeout.connect:5000}")
  private int connectTimeoutMs;

  @Value("${covid.api.timeout.read:15000}")
  private int readTimeoutMs;

  private URI uri;

  /** Completed with response body when fetch succeeds; completedExceptionally on failure. */
  private final CompletableFuture<String> dataFuture = new CompletableFuture<>();

  /**
   * Returns a future that completes with the API response body when data is ready.
   * Use this instead of static result to avoid race conditions.
   */
  public CompletableFuture<String> getDataAsync() {
    return dataFuture;
  }

  /**
   * Generates uri for rest command line runner.
   */
  @Override
  public URI createUri(String url) {
    try {
      this.uri = new URI(url);
      return uri;
    } catch (URISyntaxException e) {
      log.error("Invalid API URI: " + url, e);
      throw new IllegalArgumentException("Invalid API URI", e);
    }
  }

  /**
   * Getter for URI.
   */
  @Override
  public URI getUri() {
    return uri;
  }

  /**
   * Builds rest template with timeouts for production reliability.
   */
  @Override
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
        .setReadTimeout(Duration.ofMillis(readTimeoutMs))
        .build();
  }

  /**
   * Command line runner: fetches COVID data at startup and completes dataFuture.
   */
  @Override
  @Bean
  public CommandLineRunner run(RestTemplate restTemplate) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    return args -> {
      try {
        ResponseEntity<String> result = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            new HttpEntity<String>(headers),
            String.class);
        if (result.getBody() != null) {
          dataFuture.complete(result.getBody());
          log.info("COVID-19 API data loaded successfully");
        } else {
          dataFuture.completeExceptionally(new IllegalStateException("API returned empty body"));
        }
      } catch (Exception e) {
        log.error("Failed to load COVID-19 data from " + apiUrl, e);
        dataFuture.completeExceptionally(e);
      }
    };
  }
}


