package com.ljt.ratelimiter.utils;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class UrlUtilsTest {

    @DataProvider(name = "urlsToTokenize")
    public Object[][] urlsToTokenize() {
        return new Object[][]{
                {"http://www.test.com/v1/user"},
                {"www.test.com/v1/user"},
                {"v1/user/*"}, {"v1/user"}, {"/v1/user?p1=1"}
        };
    }

    //正常测试
    @Test
    public void testTokenizeUrlPath() {
        String url = "/v1/user";
        try {
            List<String> actualSegments = UrlUtils.tokenizeUrlPath(url);
            assertEquals(actualSegments.size(), 2);
            assertThat(actualSegments, hasItems("v1", "user"));
        } catch (InvalidUrlException e) {
            fail("tokenize should not throw InvalidUrlException");
        }
    }

    @Test
    public void testTokenizeUrlPath_withUrlPattern() {
        String url = "/v1/user/{actorId}/balance/{column:(.*)}";
        try {
            List<String> actualSegments = UrlUtils.tokenizeUrlPath(url);
            assertEquals(actualSegments.size(), 5);
            assertThat(actualSegments,hasItems("v1", "user", "{actorId}", "balance", "{column:(.*)}"));
        } catch (InvalidUrlException e) {
            fail("tokenize should not throw InvalidUrlException");
        }
    }

    @Test
    public void testTokenizeUrlPath_withEmptyUrl() {
        try {
            List<String> actualSegments = UrlUtils.tokenizeUrlPath("");
            assertNotNull(actualSegments);
            assertEquals(actualSegments.size(), 0);

            List<String> actualSegments2 = UrlUtils.tokenizeUrlPath("/");
            assertNotNull(actualSegments2);
            assertEquals(actualSegments2.size(), 0);

            List<String> actualSegments3 = UrlUtils.tokenizeUrlPath(null);
            assertNotNull(actualSegments3);
            assertEquals(actualSegments3.size(), 0);
        } catch (InvalidUrlException e) {
            fail("tokenize should not throw InvalidUrlException");
        }
    }

    @Test(expectedExceptions = {InvalidUrlException.class},dataProvider = "urlsToTokenize")
    public void testTokenizeUrlPath_withInvalidUrl(String invalidUrl) throws InvalidUrlException {
        UrlUtils.tokenizeUrlPath(invalidUrl);
    }

    @Test
    public void testGetUrlPath() {
        try {
            String actualPath = UrlUtils.getUrlPath("http://www.test.com/");
            assertEquals(actualPath, "/");

            actualPath = UrlUtils.getUrlPath("http://www.test.com");
            assertEquals(actualPath, "/");

            actualPath = UrlUtils.getUrlPath("http://www.test.com/v1/user");
            assertEquals(actualPath, "/v1/user");

            actualPath = UrlUtils.getUrlPath("http://www.test.com/v1/user?p1=2");
            assertEquals(actualPath, "/v1/user");

            actualPath = UrlUtils.getUrlPath("/v1/user");
            assertEquals(actualPath, "/v1/user");

            actualPath = UrlUtils.getUrlPath("/v1/user?p1=1");
            assertEquals(actualPath, "/v1/user");

            actualPath = UrlUtils.getUrlPath("/v1/user/");
            assertEquals(actualPath, "/v1/user/");
        } catch (InvalidUrlException e) {
            fail("getPath() should not throw exception here");
        }
    }

    @Test
    public void testGetUrlPath_withEmptyUrl() {
        try {
            String actualPath = UrlUtils.getUrlPath("");
            assertNull(actualPath);

            actualPath = UrlUtils.getUrlPath(null);
            assertNull(actualPath);
        } catch (InvalidUrlException e) {
            fail("getPath() should not throw exception here");
        }
    }

    @Test
    public void testValidUrl() {
        assertTrue(UrlUtils.validUrl("http://www.test.com/v1/user?a=1"));
        assertTrue(UrlUtils.validUrl("https://www.test.com/v1/user?a=1"));
        assertFalse(UrlUtils.validUrl("www.test.com/v1/user?a=1"));
        assertFalse(UrlUtils.validUrl("/v1/user?a=1"));
    }
}
