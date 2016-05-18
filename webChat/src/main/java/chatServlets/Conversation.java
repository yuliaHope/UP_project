package chatServlets;

import Storage.*;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conversation extends HttpServlet {

    private MessageStorage messageStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        messageStorage = new InFileMessageStorage();
        messageStorage.loadMessages("C:\\webChat\\src\\main\\resources\\messages.json");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getQueryString();
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        try {
            int index = MessageHelper.parseToken(token);
            if (index > messageStorage.size()) {
                resp.sendError(400,
                        String.format("Incorrect token in request: %s. Server does not have so many messages", token));
            }
            Portion portion = new Portion(index);
            List<Message> messages = messageStorage.getPortion(portion);
            String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size());
            resp.getOutputStream().println(responseBody);
        } catch (InvalidTokenException e) {
            resp.sendError(400, "Bad Request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {
            resp.sendError(400, "Bad Request");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.updateMessage(message);
        } catch (ParseException e) {
            resp.sendError(400, "Bad Request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getQueryString();
        Map<String, String> map = queryToMap(query);
        String messageId = map.get(Constants.REQUEST_PARAM_MESSAGE_ID);
        try {
            messageStorage.removeMessage(messageId);
        } catch (InvalidTokenException e) {
            resp.sendError(400, "Bad Request");
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
}
