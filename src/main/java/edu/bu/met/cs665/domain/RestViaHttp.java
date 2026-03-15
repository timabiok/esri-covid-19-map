package edu.bu.met.cs665.domain;

import java.net.URI;
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

public interface RestViaHttp {

  /**
   * Generates uri for rest command line runner.
   */
  URI createUri(String url);

  /**
   * Getter for URI.
   */
  URI getUri();

  /**
   * Builds rest template for Bean.
   */
  RestTemplate restTemplate(RestTemplateBuilder builder);

  /**
   * Command line runner wired to the spring application and generates rest data upon receipt of all
   * local and global parameters.
   */
  CommandLineRunner run(RestTemplate restTemplate);

}
