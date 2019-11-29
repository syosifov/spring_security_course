package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter1.00
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserContextTests {

    @Autowired
    private UserContext userContext;

    private User owner = new User();


    @BeforeEach
    public void beforeEachTest() {
        owner.setId(1);
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    public void setCurrentUser() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(owner);
        });
    }

    @Test
    public void setCurrentUser_null_User() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(null);
        });

    }

    @Test
    public void setCurrentUser_invalid_User() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(new User());
        });
    }

} // The End...
