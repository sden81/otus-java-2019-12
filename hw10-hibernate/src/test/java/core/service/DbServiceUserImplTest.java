package core.service;

import common.HibernateAbstractTest;
import core.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DbServiceUserImplTest extends HibernateAbstractTest {
    @Test
    @DisplayName("save user, update user")
    void saveUser() {
        User user = buildDefaultUser();
        dbServiceUser.saveUser(user);
        assertThat(user.getId()).isGreaterThan(0);
        assertThat(user.getAddress().getId()).isGreaterThan(0);
        assertThat(user.getFirstPhone().get().getId()).isGreaterThan(0);

//        //change phone number
        String newPhoneNumber = "789";
        user.getFirstPhone().get().setNumber(newPhoneNumber);
        dbServiceUser.saveUser(user);

        var loadedUser = dbServiceUser.getUser(user.getId());
//        assertThat(loadedUser.get().getFirstPhone().get().getNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("get user")
    void getUser() {
        User user = buildDefaultUser();
        dbServiceUser.saveUser(user);

        var loadedUser = dbServiceUser.getUser(user.getId());
        assertThat(loadedUser.get()).isNotNull();
    }
}