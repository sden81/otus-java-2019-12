package msHandlers.request;

import com.google.gson.Gson;
import core.service.DBServiceUser;
import ru.otus.Message;
import ru.otus.MessageType;
import ru.otus.RequestHandler;
import ru.otus.helpers.Serializers;

public class GetUserDataRequestHandler implements RequestHandler {
    private final DBServiceUser userService;

    public GetUserDataRequestHandler(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    public Message handle(Message msg) {
        long id = Serializers.deserialize(msg.getPayload(), Long.class);
        String data = new Gson().toJson(userService.getUser(id).get());

        return new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(data));
    }

    @Override
    public boolean isNeedResponse() {
        return true;
    }
}
