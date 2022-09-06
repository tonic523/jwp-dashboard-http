package org.apache.coyote.http11.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HttpRequestTest {

    private final String httpRequest = String.join(System.lineSeparator(),
            "GET " + "/index.html?account=tonic&password=password" + " HTTP/1.1 ",
            "Host: localhost:8080 ",
            "Connection: keep-alive ",
            "",
            "");

    @DisplayName("method를 반환한다.")
    @Test
    void method() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        HttpRequest request = HttpRequest.from(new BufferedReader(new InputStreamReader(inputStream)));
        HttpMethod method = request.method();

        // then
        assertThat(method).isEqualTo(HttpMethod.GET);
    }


    @DisplayName("request uri를 반환한다.")
    @ParameterizedTest
    @CsvSource({"/index.html,/index.html", "/index.html?name=value,/index.html"})
    void getPath(String uri, String expected) throws IOException {
        // given
        String httpRequestValue = String.join(System.lineSeparator(),
                "GET " + uri + " HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");
        InputStream inputStream = new ByteArrayInputStream(httpRequestValue.getBytes());

        // when
        HttpRequest request = HttpRequest.from(new BufferedReader(new InputStreamReader(inputStream)));

        // then
        assertThat(request.path()).isEqualTo(expected);
    }

    @DisplayName("query string을 반환한다.")
    @Test
    void queryString() throws IOException {
        // given
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        HttpRequest request = HttpRequest.from(new BufferedReader(new InputStreamReader(inputStream)));
        QueryString queryString = request.queryString();
        // then
        assertAll(
                () -> assertThat(queryString.get("account")).isEqualTo("tonic"),
                () -> assertThat(queryString.get("password")).isEqualTo("password")
        );
    }
}
