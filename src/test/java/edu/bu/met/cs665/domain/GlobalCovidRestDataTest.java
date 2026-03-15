package edu.bu.met.cs665.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * Summary.
 * 
 * @author tim_abiok
 * @course CS-665
 * @term Summer 2
 * @assignment PROJECT
 * @date 20 AUG 2020
 */
class GlobalCovidRestDataTest {

  String url =
      "https://corona.lmao.ninja/v2/countries/Ghana?yesterday=true&strict=true&query%20=&country";

  @Test
  void testCreateURI() {

    RestViaHttp restData = new GlobalCovidRestData();
    restData.createUri(url);
    assertEquals(url, restData.createUri(url).toString());
  }


  @Test
  void testGetUri() {
    RestViaHttp restData = new GlobalCovidRestData();
    restData.createUri(url);
    assertEquals(url, restData.createUri(url).toString());
    assertEquals(url, restData.getUri().toString());

  }

  @Test
  void testRestTemplate() {
    RestViaHttp restData = new GlobalCovidRestData();
    restData.createUri(url);

    RestTemplateBuilder builder = new RestTemplateBuilder();
    RestTemplate restTemplate = restData.restTemplate(builder);
    assertNotNull(restTemplate);
  }

  @Test
  void testRun() {
    RestViaHttp restData = new GlobalCovidRestData();
    restData.createUri(url);
    RestTemplateBuilder builder = new RestTemplateBuilder();
    RestTemplate restTemplate = restData.restTemplate(builder);
    assertNotNull(restTemplate);
    CommandLineRunner cli = restData.run(restTemplate);
    assertNotNull(cli);

  }


}
