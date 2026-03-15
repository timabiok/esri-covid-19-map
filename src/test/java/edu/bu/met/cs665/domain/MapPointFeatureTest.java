package edu.bu.met.cs665.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.symbology.ClassBreaksRenderer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * Summary.
 * 
 * @author tim_abiok
 * @course CS-665
 * @term Summer 2
 * @assignment PROJECT
 * @date 20 AUG 2020
 */
class MapPointFeatureTest {

  /** ArcGIS Runtime requires native libs (JNI); skip tests when not available (e.g. CI). */
  private static boolean arcgisAvailable;

  @BeforeAll
  static void checkArcGISRuntime() {
    try {
      new FeatureCollection();
      arcgisAvailable = true;
    } catch (Throwable e) {
      arcgisAvailable = false;
    }
  }

  @BeforeEach
  void assumeArcGIS() {
    assumeTrue(arcgisAvailable, "ArcGIS Runtime native libs not available (skip when not set up)");
  }

  String json = "{\n" + "    \"updated\": 1596924624020,\n" + "    \"country\": \"Italy\",\n"
      + "    \"countryInfo\": {\n" + "        \"_id\": 380,\n" + "        \"iso2\": \"IT\",\n"
      + "        \"iso3\": \"ITA\",\n" + "        \"lat\": 42.8333,\n"
      + "        \"long\": 12.8333,\n"
      + "        \"flag\": \"https://disease.sh/assets/img/flags/it.png\"\n" + "    },\n"
      + "    \"cases\": 249756,\n" + "    \"todayCases\": 552,\n" + "    \"deaths\": 35190,\n"
      + "    \"todayDeaths\": 3,\n" + "    \"recovered\": 201642,\n"
      + "    \"todayRecovered\": 319,\n" + "    \"active\": 12924,\n" + "    \"critical\": 42,\n"
      + "    \"casesPerOneMillion\": 4131,\n" + "    \"deathsPerOneMillion\": 582,\n"
      + "    \"tests\": 7158909,\n" + "    \"testsPerOneMillion\": 118422,\n"
      + "    \"population\": 60452326,\n" + "    \"continent\": \"Europe\",\n"
      + "    \"oneCasePerPeople\": 242,\n" + "    \"oneDeathPerPeople\": 1718,\n"
      + "    \"oneTestPerPeople\": 8,\n" + "    \"activePerOneMillion\": 213.79,\n"
      + "    \"recoveredPerOneMillion\": 3335.55,\n" + "    \"criticalPerOneMillion\": 0.69\n"
      + "}";

  String data = "[{\n" + "    \"updated\": 1596924624020,\n" + "    \"country\": \"Italy\",\n"
      + "    \"countryInfo\": {\n" + "        \"_id\": 380,\n" + "        \"iso2\": \"IT\",\n"
      + "        \"iso3\": \"ITA\",\n" + "        \"lat\": 42.8333,\n"
      + "        \"long\": 12.8333,\n"
      + "        \"flag\": \"https://disease.sh/assets/img/flags/it.png\"\n" + "    },\n"
      + "    \"cases\": 249756,\n" + "    \"todayCases\": 552,\n" + "    \"deaths\": 35190,\n"
      + "    \"todayDeaths\": 3,\n" + "    \"recovered\": 201642,\n"
      + "    \"todayRecovered\": 319,\n" + "    \"active\": 12924,\n" + "    \"critical\": 42,\n"
      + "    \"casesPerOneMillion\": 4131,\n" + "    \"deathsPerOneMillion\": 582,\n"
      + "    \"tests\": 7158909,\n" + "    \"testsPerOneMillion\": 118422,\n"
      + "    \"population\": 60452326,\n" + "    \"continent\": \"Europe\",\n"
      + "    \"oneCasePerPeople\": 242,\n" + "    \"oneDeathPerPeople\": 1718,\n"
      + "    \"oneTestPerPeople\": 8,\n" + "    \"activePerOneMillion\": 213.79,\n"
      + "    \"recoveredPerOneMillion\": 3335.55,\n" + "    \"criticalPerOneMillion\": 0.69\n"
      + "}]";



  @Test
  void testMapPointFeature() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    assertNotNull(mapPointFeature.toString());
  }

  @Test
  void testCreateFeatureCollection() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    mapPointFeature.createFeatureCollection(data);

    assertEquals("Deaths", mapPointFeature.createPointFields().get(1).getName());



  }

  @Test
  void testGetJsonObject() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);
    assertEquals("Italy", mapPointFeature.getJsonObject().get("country").getAsString());

  }

  @Test
  void testCreatePointFields() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    assertEquals("Deaths", mapPointFeature.createPointFields().get(1).getName());
  }

  @Test
  void testCreatePointsTable() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    List<Field> pointFields = mapPointFeature.createPointFields();
    FeatureCollectionTable pointsTable = mapPointFeature.createPointsTable(pointFields);
    assertEquals("Country", pointsTable.getField("country").getName());

  }

  @Test
  void testSetJsonObject() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    assertEquals("Italy", mapPointFeature.getJsonObject().get("country").getAsString());
  }

  @Test
  void testCreateSymbology() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    ClassBreaksRenderer symbology = mapPointFeature.createSymbology("Cases");

    assertEquals("Cases", symbology.getFieldName());
    assertNotEquals("Deaths", symbology.getFieldName());
  }


  @Test
  void testGetCountry() throws InterruptedException, ExecutionException {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals("Italy", mapPointFeature.getCountry());


  }

  @Test
  void testGetLon() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(12, mapPointFeature.getLon());
  }

  @Test
  void testGetLat() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(42, mapPointFeature.getLat());

  }

  @Test
  void testGetCases() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(249756, mapPointFeature.getCases());

  }

  @Test
  void testGetDeaths() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(35190, mapPointFeature.getDeaths());

  }

  @Test
  void testSetCountry() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals("Italy", mapPointFeature.getCountry());
  }

  @Test
  void testSetLon() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(12, mapPointFeature.getLon());
  }

  @Test
  void testSetLat() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(42, mapPointFeature.getLat());

  }

  @Test
  void testSetCases() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(249756, mapPointFeature.getCases());
  }

  @Test
  void testSetDeaths() {
    FeatureCollection featureCollection = new FeatureCollection();
    MapPointFeature mapPointFeature = new MapPointFeature(featureCollection);
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    mapPointFeature.setJsonObject(jsonObject);

    mapPointFeature.setCountry(jsonObject.get("country").getAsString());
    mapPointFeature.setLat(jsonObject.get("countryInfo").getAsJsonObject().get("lat").getAsLong());
    mapPointFeature.setLon(jsonObject.get("countryInfo").getAsJsonObject().get("long").getAsLong());
    mapPointFeature.setDeaths(jsonObject.get("deaths").getAsInt());
    mapPointFeature.setCases(jsonObject.get("cases").getAsInt());

    assertEquals(35190, mapPointFeature.getDeaths());
  }

}
