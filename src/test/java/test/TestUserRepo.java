//package test;
//
//
//import com.example.demo.entity.State;
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepo;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//public class TestUserRepo {
//    @Test
//    public void addUser(){
//        UserRepo userRepo = new UserRepo();
//      User user =  userRepo.addUser("Pham Huu hai","haih70872@gmail.com","3123123", State.ACTIVE);
//      assertThat(user).isNotNull();
//        System.out.println(user.getId());
//
//    }
//
//
//    @Test
//    public void addUserWithPendingState(){
//        UserRepo userRepo = new UserRepo();
//        User user =  userRepo.addUser("Pham Huu hai","haih70872@gmail.com","3123123");
//        assertThat(user).isNotNull();
//        assertThat(user.getState()).isEqualTo(State.PENDING);
//    }
//
//    @Test
//    public void isUserNameExist(){
//        UserRepo userRepo = new UserRepo();
//        userRepo.addUser("Phamhuuhai","haih70872@gmail.com","3123123");
//         userRepo.addUser("Phamhhh","haih70872@gmail.com","3123123");
//         assertThat(userRepo.isUserNameExist("Phamhuuhai")).isTrue();
//         assertThat(userRepo.isUserNameExist("SSSSSS")).isFalse();
//
//    }
//
//    @Test
//    public void fimdByUserName(){
//        UserRepo userRepo = new UserRepo();
//        userRepo.addUser("Phamhuuhai","haih70872@gmail.com","3123123");
//        userRepo.addUser("Phamhhh","haih70872@gmail.com","3123123");
//        Optional<User> user = userRepo.fimdByUserName("Phamhuuhai");
//        assertThat(user.get()).isNotNull();
//
//    }
//}
