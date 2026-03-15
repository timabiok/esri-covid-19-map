#!/bin/bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.bu.met.cs665.App" -Dlog4j.configuration="file:log4j.properties"
