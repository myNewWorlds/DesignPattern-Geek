package com.ljt.ratelimiter.utils;


import com.ljt.ratelimiter.exception.ConfigurationResolveException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class YamlUtilsTest {
    private static final String VALID_YAML_STRING_1 =
            "persons:\n" +
            "- name: zheng\n" +
            "  age: 20\n" +
            "  male: true\n" +
            "- name: xiaoli\n" +
            "  age: 19\n" +
            "  male: false";

    private static final String VALID_YAML_STRING_2 =
            "persons:\n" +
            "- name: zheng\n" +
            "  age: 20\n" +
            "- name: xiaoli\n" +
            "  age: 19\n" +
            "  male: false";

    private static final String INVALID_YAML_STRING_1 = "persons:\n" +
            "- name: zheng\n" +
            "  age: 20\n" +
            "  male: true\n" +
            "  other: other-v\n" +
            "- name: xiaoli\n" +
            "  age: 19\n" +
            "  male: false";

    //测试String文本转换
    @Test
    public void testParse_withString() {
        MappingClass4Test result = YamlUtils.parse(VALID_YAML_STRING_1, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);

        result = YamlUtils.parse(VALID_YAML_STRING_2, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);
    }

    @Test
    public void testParse_withEmptyString() {
        MappingClass4Test result = YamlUtils.parse("", MappingClass4Test.class);
        Assert.assertNull(result);

        result = YamlUtils.parse((String) null, MappingClass4Test.class);
        Assert.assertNull(result);
    }

    @Test(expectedExceptions = {ConfigurationResolveException.class})
    public void testParse_withInvalidString() {
        YamlUtils.parse(INVALID_YAML_STRING_1, MappingClass4Test.class);
    }

    @Test
    public void testParse_withInputStream() {
        InputStream in = new ByteArrayInputStream(VALID_YAML_STRING_1.getBytes());
        MappingClass4Test result = YamlUtils.parse(in, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);

        in = new ByteArrayInputStream(VALID_YAML_STRING_2.getBytes());
        result = YamlUtils.parse(in, MappingClass4Test.class);
        Assert.assertNotNull(result);
        print(result);
    }

    @Test(expectedExceptions = {ConfigurationResolveException.class})
    public void testParse_withInvalidInputStream() {
        InputStream in = new ByteArrayInputStream(INVALID_YAML_STRING_1.getBytes());
        YamlUtils.parse(in, MappingClass4Test.class);
    }

    private void print(MappingClass4Test yamlMappingClass4Test) {
        for (MappingClass4Test.MappingClassUnit4Test person : yamlMappingClass4Test.getPersons()) {
            System.out.println(person);
        }
        System.out.println();
    }

}
