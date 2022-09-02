package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import nextstep.jwp.Handler.LoginHandler;
import nextstep.jwp.Handler.StaticHandler;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(Socket connection) {
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {
            List<String> httpRequestLines = getHttpRequestLines(inputStream);
            HttpRequest request = HttpRequest.from(httpRequestLines);

            String response = handle(request);
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String handle(HttpRequest request) throws IOException {
        String path = request.path();
        if (path.contains(".")) {
            return StaticHandler.handleStatic(path);
        }
        return handleApi(request);
    }

    private String handleApi(HttpRequest request) throws IOException {
        if (request.path()
                .equals("/login")) {
            return LoginHandler.handle(request);
        }
        return StaticHandler.handleStatic("/404.html");
    }

    private List<String> getHttpRequestLines(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> collect = new ArrayList<>();
        while (bufferedReader.ready()) {
            collect.add(bufferedReader.readLine());
        }
        return collect;
    }
}
