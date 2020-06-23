package msHandlers.request;

import com.google.gson.Gson;
import core.model.User;
import core.service.DBServiceUser;
import ru.otus.Message;
import ru.otus.MessageType;
import ru.otus.RequestHandler;
import ru.otus.helpers.Serializers;

public class SaveUserDataRequestHandler implements RequestHandler {
    private final DBServiceUser userService;

    public SaveUserDataRequestHandler(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    public Message handle(Message msg) {
        String userJson = Serializers.deserialize(msg.getPayload(), String.class);
        User savingUSer = new Gson().fromJson(userJson, User.class);

        long result = userService.saveUser(savingUSer);

        return new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.SAVE_USER.getValue(), Serializers.serialize(result));
    }

    @Override
    public boolean isNeedResponse() {
        return true;
    }
}
