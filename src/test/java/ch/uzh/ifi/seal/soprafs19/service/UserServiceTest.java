package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
        Assert.assertEquals(createdUser, userRepository.findByUsername(createdUser.getUsername()));
    }

    @Test
    public void getUsers(){
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        User createdUser2 = userService.createUser(testUser2);

        Iterable<User> users = userRepository.findAll();
        Iterable<User> foundUsers = userService.getUsers("");

        Assert.assertEquals(users,foundUsers);
    }

    @Test
    public void getUser(){
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userService.getUser(createdUser.getId()));
        Assert.assertEquals(createdUser,userService.getUser(createdUser.getId()));
    }

    @Test
    public void updateUser() throws ParseException {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        Assert.assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
        Assert.assertEquals("testUsername", createdUser.getUsername());
        Assert.assertEquals("testPassword", createdUser.getPassword());
        Assert.assertNull(createdUser.getBirthdayDate());

        User userUpdate = userService.getUser(createdUser.getId());
        userUpdate.setStatus(UserStatus.ONLINE);
        userUpdate.setUsername("testUsername2");
        userUpdate.setPassword("testPassword2");
        userUpdate.setBirthdayDate(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));

        userService.updateUser(userUpdate.getId(),userUpdate.getToken(),userUpdate);

        Assert.assertEquals(userUpdate,userService.getUser(userUpdate.getId()));
    }

    @Test
    public void authenticateUser(){
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        User userFromDb = userService.getUser(createdUser.getId());

        Assert.assertEquals(userFromDb, userService.authenticateUser(testUser));
    }

    @Test(expected = ResponseStatusException.class)
    public void authenticateWrongUser(){
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");

        userService.authenticateUser(testUser2);
    }

    @Test(expected = ResponseStatusException.class)
    public void isAuthorized(){
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        String wrongToken = "Wrong-Token-I-Am";

        Assert.assertNotEquals(createdUser.getToken(),wrongToken);

    }
}
