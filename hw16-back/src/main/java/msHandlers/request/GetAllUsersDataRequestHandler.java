package msHandlers.request;

import com.google.gson.Gson;
import core.service.DBServiceUser;
import ru.otus.Message;
import ru.otus.MessageType;
import ru.otus.RequestHandler;
import ru.otus.helpers.Serializers;

public class GetAllUsersDataRequestHandler implements RequestHandler {
    private final DBServiceUser userService;

    public GetAllUsersDataRequestHandler(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    public Message handle(Message msg) {
        String data = new Gson().toJson(userService.getAllUsers().get());

        return new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.ALL_USERS_DATA.getValue(), Serializers.serialize(data));
    }

    @Override
    public boolean isNeedResponse() {
        return true;
    }
}
