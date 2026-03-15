package edu.bu.met.cs665;

import com.esri.arcgisruntime.mapping.view.MapView;
import edu.bu.met.cs665.domain.GlobalCovidRestData;
import edu.bu.met.cs665.domain.MapConfiguration;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * JavaFX + Spring Boot entry point. Ensures COVID data is loaded before rendering the map.
 *
 * @author tim_abiok
 * @course CS-665
 * @term Summer 2
 * @assignment PROJECT
 * @date 20 AUG 2020
 */
@SpringBootApplication(scanBasePackages = {"edu.bu.met.cs665"})
public class App extends Application {

  private static final Logger log = Logger.getLogger(App.class);
  private static final int STAGE_HEIGHT = 700;
  private static final int STAGE_WIDTH = 800;
  private static final String MY_MAP_APP = "My Map App";
  private static final int DATA_LOAD_TIMEOUT_SECONDS = 30;

  private static ConfigurableApplicationContext applicationContext;

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(App.class);
    springApplication.setLogStartupInfo(false);
    applicationContext = springApplication.run(args);
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle(MY_MAP_APP);
    stage.setWidth(STAGE_WIDTH);
    stage.setHeight(STAGE_HEIGHT);
    stage.show();

    StackPane stackPane = new StackPane();
    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    MapConfiguration.mapView = new MapView();
    stackPane.getChildren().add(MapConfiguration.mapView);

    GlobalCovidRestData restData = applicationContext.getBean(GlobalCovidRestData.class);
    restData
        .getDataAsync()
        .orTimeout(DATA_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .thenAccept(
            body ->
                Platform.runLater(() -> MapConfiguration.setupMap(body)))
        .exceptionally(
            ex -> {
              log.error("Failed to load COVID data for map", ex);
              Platform.runLater(() -> MapConfiguration.setupMap(null));
              return null;
            });
  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    if (MapConfiguration.mapView != null) {
      MapConfiguration.mapView.dispose();
    }
  }



}
