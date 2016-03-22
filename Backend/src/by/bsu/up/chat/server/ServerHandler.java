package by.bsu.up.chat.server;

import by.bsu.up.chat.Constants;
import by.bsu.up.chat.InvalidTokenException;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.LogInFile;
import by.bsu.up.chat.utils.MessageHelper;
import by.bsu.up.chat.utils.StringUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandler implements HttpHandler {

    private static final Logger logger = LogInFile.create("serverlog.txt");

    private List<String> messageStorage = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Response response;

        logger.info("request begin");

        try {
            response = dispatch(httpExchange);
        } catch (Throwable e) {
            // WARNING! It's not a good practice to catch all exceptions via Throwable
            // or Exception classes. But if you want to handle and you know
            // how to handle them correctly, you may use such approach.
            // Useful when you use thread pool and don't want to corrupt a thread
            logger.error("An error occurred when dispatching request.", e);
            response = new Response(Constants.RESPONSE_CODE_INTERNAL_SERVER_ERROR, "Error while dispatching message");
        }
        sendResponse(httpExchange, response);
        logger.info("request end");

    }

    private Response dispatch(HttpExchange httpExchange) {
        if (Constants.REQUEST_METHOD_GET.equals(httpExchange.getRequestMethod())) {
            return doGet(httpExchange);
        } else if (Constants.REQUEST_METHOD_POST.equals(httpExchange.getRequestMethod())) {
            return doPost(httpExchange);
        } else {
            return new Response(Constants.RESPONSE_CODE_METHOD_NOT_ALLOWED,
                    String.format("Unsupported http method %s", httpExchange.getRequestMethod()));
        }
    }

    private Response doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            logger.warn("Absent query in request");
            return Response.badRequest("Absent query in request");
        }
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            logger.warn("Token query parameter is required");
            return Response.badRequest("Token query parameter is required");
        }
        try {
            int index = MessageHelper.parseToken(token);
            if (index > messageStorage.size()) {
                logger.warn(String.format("Incorrect token in request: %s. Server does not have so many messages", token));
                return Response.badRequest(
                        String.format("Incorrect token in request: %s. Server does not have so many messages", token));
            }
            logger.info(" request method: GET");
            logger.info(" request parameters: token: " + token);
            logger.info(" response: history size=" + messageStorage.size());
            String responseBody = MessageHelper.buildServerResponseBody(messageStorage.subList(index, messageStorage.size()));
            return Response.ok(responseBody);
        } catch (InvalidTokenException e) {
            logger.error(e.getMessage(), e);
            return Response.badRequest(e.getMessage());
        }
    }

    private Response doPost(HttpExchange httpExchange) {
        try {
            String message = MessageHelper.getClientMessage(httpExchange.getRequestBody());
            logger.info(" request method: POST");
            logger.info(String.format(" request new message from user: %s", message));
            System.out.println(String.format("Received new message from user: %s", message));
            messageStorage.add(message);
            return Response.ok();
        } catch (ParseException e) {
            logger.error("Could not parse message.", e);
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    private void sendResponse(HttpExchange httpExchange, Response response) {
        try (OutputStream os = httpExchange.getResponseBody()) {

            byte[] bytes = response.getBody().getBytes();

            Headers headers = httpExchange.getResponseHeaders();
            headers.add(Constants.REQUEST_HEADER_ACCESS_CONTROL, "*");
            httpExchange.sendResponseHeaders(response.getStatusCode(), bytes.length);

            os.write(bytes);
            logger.info(" response sent");
            // there is no need to close stream manually
            // as try-catch with auto-closable is used
            /**
             * {@see http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html}
             */
        } catch (IOException e) {
            logger.error("Could not send response", e);
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        for (String queryParam : query.split(Constants.REQUEST_PARAMS_DELIMITER)) {
            String paramKeyValuePair[] = queryParam.split("=");
            if (paramKeyValuePair.length > 1) {
                result.put(paramKeyValuePair[0], paramKeyValuePair[1]);
            } else {
                result.put(paramKeyValuePair[0], "");
            }
        }
        return result;
    }

    /**
     * This method does absolutely the same as
     * {@link ServerHandler#queryToMap(String)} one, but uses
     * Java's 8 Stream API and lambda expressions
     * <p>
     *     It's just as an example. Bu you can use it
     * @param query the query to be parsed
     * @return the map, containing parsed key-value pairs from request
     */
//    private Map<String, String> queryToMap2(String query) {
//        return Stream.of(query.split(Constants.REQUEST_PARAMS_DELIMITER))
//                .collect(Collectors.toMap(
//                        keyValuePair -> keyValuePair.split("=")[0],
//                        keyValuePair -> keyValuePair.split("=")[1]
//                ));
//    }
}
