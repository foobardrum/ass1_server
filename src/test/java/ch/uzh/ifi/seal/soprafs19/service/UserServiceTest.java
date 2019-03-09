package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.List;

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

    private User testUser;
    private User testUser2;

    @Before
    public void initialize(){
        this.testUser = new User();
        this.testUser.setUsername("testUsername");
        this.testUser.setPassword("testPassword");

        this.testUser2 = new User();
        this.testUser2.setUsername("testUsername2");
        this.testUser2.setPassword("testPassword2");
    }

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername(this.testUser.getUsername()));
        User createdUser = userService.createUser(this.testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
        Assert.assertEquals(createdUser, userRepository.findByUsername(createdUser.getUsername()));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void getUsers(){
        User createdUser = userService.createUser(this.testUser);
        User createdUser2 = userService.createUser(this.testUser2);

        List<User> users = userRepository.findAll();
        List<User> foundUsers = userService.getUsers("");

        Assert.assertEquals(users,foundUsers);

        userService.deleteUser(createdUser.getId());
        userService.deleteUser(createdUser2.getId());
    }

    @Test
    public void getUsersWithSearch(){
        User createdUser = userService.createUser(this.testUser);
        User createdUser2 = userService.createUser(this.testUser2);

        List<User> foundUsers = userService.getUsers("username=="+createdUser2.getUsername());

        Assert.assertEquals(1, foundUsers.size());
        Assert.assertEquals(createdUser2,foundUsers.get(0));


        userService.deleteUser(createdUser.getId());
        userService.deleteUser(createdUser2.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void getUsersWithSearchInvalid(){
        List<User> foundUsers = userService.getUsers("username=;");
    }

    @Test
    public void getUser(){
        User createdUser = userService.createUser(this.testUser);

        Assert.assertNotNull(userService.getUser(createdUser.getId()));
        Assert.assertEquals(createdUser,userService.getUser(createdUser.getId()));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void getUserByUsername(){
        User createdUser = userService.createUser(this.testUser);

        Assert.assertNotNull(userService.getUser(createdUser.getId()));
        Assert.assertEquals(createdUser,userService.getUserByUsername(createdUser.getUsername()));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void updateUser() throws ParseException {
        User createdUser = userService.createUser(this.testUser);

        Assert.assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
        Assert.assertEquals("testUsername", createdUser.getUsername());
        Assert.assertEquals("testPassword", createdUser.getPassword());
        Assert.assertNull(createdUser.getBirthdayDate());

        User userUpdate = userService.getUser(createdUser.getId());
        userUpdate.setStatus(UserStatus.ONLINE);
        userUpdate.setUsername("testUsername2");
        userUpdate.setPassword("testPassword2");
        userUpdate.setBirthdayDate(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));

        Assert.assertEquals(userUpdate,userService.updateUser(userUpdate.getId(),userUpdate));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void updateUserNotExistent() {

        long wrongId = 1000000;

        Assert.assertNull(userService.getUser(wrongId));
        Assert.assertNull(userService.updateUser(wrongId,this.testUser));
    }


    @Test
    public void deleteUser(){
        User createdUser = userService.createUser(this.testUser);

        Assert.assertNotNull(userService.getUser(createdUser.getId()));

        userService.deleteUser(createdUser.getId());

        Assert.assertNull(userService.getUser(createdUser.getId()));
    }

    @Test
    public void authenticateUser(){
        User createdUser = userService.createUser(this.testUser);
        User userFromDb = userService.getUser(createdUser.getId());

        Assert.assertEquals(userFromDb, userService.authenticateUser(this.testUser));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void authenticateUserWrong(){
        User createdUser = userService.createUser(this.testUser);

        Assert.assertNotEquals(this.testUser2.getUsername(),createdUser.getUsername());
        Assert.assertNotEquals(this.testUser2.getPassword(),createdUser.getPassword());
        Assert.assertNull(userService.authenticateUser(this.testUser2));

        userService.deleteUser(createdUser.getId());
    }

    @Test
    public void isAuthorized(){
        User createdUser = userService.createUser(this.testUser);

        Assert.assertTrue(userService.isAuthorized(createdUser.getToken()));

        userService.deleteUser(createdUser.getId());
    }

    public void isNotAuthorized(){
        User createdUser = userService.createUser(this.testUser);

        String wrongToken = "Wrong-Token-I-Am";

        Assert.assertNotEquals(createdUser.getToken(),wrongToken);
        Assert.assertTrue(!userService.isAuthorized(wrongToken));

        userService.deleteUser(createdUser.getId());
    }
}
