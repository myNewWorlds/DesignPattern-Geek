package com.ljt.ratelimiter.utils;

import com.ljt.ratelimiter.exception.ConfigurationResolveException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Test
public class JsonUtilsTest {
    private static final String VALID_JSON_1 = "{ \"persons\": [\n" +
            "{\n" +
            "  \"name\": \"zheng\"," +
            "  \"age\": 29," +
            "  \"male\": true" +
            "}," +
            "{\n" +
            "  \"name\": \"xiaoli\"," +
            "  \"age\": 20," +
            "  \"male" +
            "\": false" +
            "}" +
            "]}";

    private static final String VALID_JSON_2 = "{ \"persons\": [\n" +
            "{\n" +
            "  \"name\": \"zheng\"," +
            "  \"age\": 29," +
            "  \"male\": true" +
            "}," +
            "{\n" +
            "  \"name\": \"xiaoli\"," +
            "  \"age\": 20" +
            "}" +
            "]}";

    private static final String INVALID_JSON_1 = "{ \"persons\": [\n" +
            "{\n" +
            "  \"name\": \"zheng\"," +
            "  \"age\": 29," +
            "  \"male\": true," +
            "  \"other\": \"other-v\"" +
            "}," +
            "{\n" +
            "  \"name\": \"xiaoli\"," +
            "  \"age\": 20," +
            "  \"male\": false" +
            "}" +
            "]}";

    private static final String INVALID_JSON_2 = "{ \"persons\": [\n" +
            "{\n" +
            "  \"name\": \"zheng\"," +
            "  \"age\": \"error-v\"," +
            "  \"male\": true," +
            "  \"other\": \"other-v\"" +
            "}," +
            "{\n" +
            "  \"name\": \"xiaoli\"," +
            "  \"age\": 20," +
            "  \"male\": false" +
            "}" +
            "]}";

    public void testJson2Object() {
        MappingClass4Test result = JsonUtils.json2Object(VALID_JSON_1, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);

        result = JsonUtils.json2Object(VALID_JSON_2, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);
    }

    public void testJson2Object_withEmpty() {
        MappingClass4Test result = JsonUtils.json2Object("", MappingClass4Test.class);
        Assert.assertNull(result);

        result = JsonUtils.json2Object(null, MappingClass4Test.class);
        Assert.assertNull(result);
    }

    @Test(expectedExceptions = {ConfigurationResolveException.class})
    public void testJson2Object_withInvalidJson() {
        JsonUtils.json2Object(INVALID_JSON_1, MappingClass4Test.class);

        JsonUtils.json2Object(INVALID_JSON_2, MappingClass4Test.class);
    }

    public void testStream2Object() {
        InputStream inpustream = new ByteArrayInputStream(VALID_JSON_1.getBytes());
        MappingClass4Test result = JsonUtils.stream2Object(inpustream, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);

        inpustream = new ByteArrayInputStream(VALID_JSON_2.getBytes());
        result = JsonUtils.stream2Object(inpustream, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);
    }

    @Test(expectedExceptions = { ConfigurationResolveException.class })
    public void testStream2Object_withInvalidInputStream() {
        InputStream inpustream = new ByteArrayInputStream(INVALID_JSON_1.getBytes());
        JsonUtils.stream2Object(inpustream, MappingClass4Test.class);
    }


    private void print(MappingClass4Test yamlMappingClass4Test) {
        for (MappingClass4Test.MappingClassUnit4Test person : yamlMappingClass4Test.getPersons()) {
            System.out.println(person);
        }
        System.out.println();
    }
}
