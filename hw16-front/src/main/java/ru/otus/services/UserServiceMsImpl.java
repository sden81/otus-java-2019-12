package ru.otus.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.Message;
import ru.otus.MessageType;
import ru.otus.MsClient;
import ru.otus.MsClientSocketImpl;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceMsImpl implements UserService{
    private static Logger logger = LoggerFactory.getLogger(UserServiceMsImpl.class);
    private MsClient msClient;

    public UserServiceMsImpl(MsClient msClient) {
        this.msClient = msClient;
    }

    @Override
    public long saveUser(User user) {
        return 0;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUser(String login) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getRandomUser() {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getAllUsers() {
        var fakeObject = new ArrayList<Integer>();
        Message getAllOutMsg = msClient.produceMessage(MsClientSocketImpl.CLIENT_TYPE_BACK, fakeObject, MessageType.ALL_USERS_DATA);
        try {
            msClient.sendMessage(getAllOutMsg);
            Optional<Message> responseMsg = msClient.receiveMessage();
            logger.info("Receive all user message: {}", responseMsg);;
            if (!responseMsg.isPresent()){
                return Optional.empty();
            }
            String jsonAllUsers = Serializers.deserialize(responseMsg.get().getPayload(), String.class);
            List<User> usersList = (new Gson()).fromJson(jsonAllUsers, new TypeToken<List<User>>(){}.getType());

            return Optional.of(usersList);
        } catch (IOException e) {
            logger.error("getAllOutMsg error: {}", getAllOutMsg);
        }
        return Optional.empty();
    }
}
