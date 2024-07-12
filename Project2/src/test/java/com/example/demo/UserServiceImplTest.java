//package com.example.demo;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.web.repository.UserRepository;
//import com.web.repository.UserRepositoryCustomImpl;
//import com.web.service.UserServiceImpl;
//import com.web.user.User;
//
//@SpringBootTest
//public class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserRepositoryCustomImpl userRepositoryCustomImpl;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    public void testRegisterUser() {
//        User user = new User();
//        user.setUserId(1L);
//        user.setUserPassword("password");
//
//        userService.registerUser(user);
//
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    public void testFindByUserIdAndUserPassword() {
//        Long userId = 1L;
//        String userPassword = "password";
//        User user = new User();
//        user.setUserId(userId);
//        user.setUserPassword(userPassword);
//
//        when(userRepository.findByUserIdAndUserPassword(userId, userPassword)).thenReturn(user);
//
//        User foundUser = userService.findByUserIdAndUserPassword(userId, userPassword);
//
//        assertNotNull(foundUser);
//        assertEquals(userId, foundUser.getUserId());
//        assertEquals(userPassword, foundUser.getUserPassword());
//    }
//
//    @Test
//    public void testIsUserIdExists() {
//        Long userId = 1L;
//
//        when(userRepository.existsByUserId(userId)).thenReturn(true);
//
//        boolean exists = userService.isUserIdExists(userId);
//
//        assertTrue(exists);
//    }
//
//    @Test
//    public void testIsUserNickNameExists() {
//        String userNickName = "nickname";
//
//        when(userRepositoryCustomImpl.existsByUserNickNameCustom(userNickName)).thenReturn(true);
//
//        boolean exists = userService.isUserNickNameExists(userNickName);
//
//        assertTrue(exists);
//    }
//
//    @Test
//    public void testGetUserById() {
//        Long userId = 1L;
//        User user = new User();
//        user.setUserId(userId);
//
//        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
//
//        User foundUser = userService.getUserById(userId);
//
//        assertNotNull(foundUser);
//        assertEquals(userId, foundUser.getUserId());
//    }
//}
