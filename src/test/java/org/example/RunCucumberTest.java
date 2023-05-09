package org.example;

import org.junit.platform.suite.api.*;


import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")

@SelectClasspathResource("features")

@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,value = "json:target/json-report/cucumber.json")
//mvn verify --fn
public class RunCucumberTest {

}
