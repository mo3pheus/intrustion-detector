package hungry.monkey.intrusion.detector.db;

import hungry.monkey.intrusion.detector.domain.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
//
//@ContextConfiguration(classes = {UserDataRepository.class})
////@SpringBootTest
//public class UserDataSaveTest {
//    @Autowired
//    UserDataRepository userDataRepository;
//
//    @Test
//    public void testUserDataSave() {
//        UserData userData = new UserData();
//        userData.setUserName("sanket");
//        userData.setCount(10);
//        userData.setId(1234);
//
//
//        if (userDataRepository != null) {
//            userDataRepository.save(userData);
//
////            Optional<UserData> userData1 = userDataRepository.findById(1234);
////            Assert.isTrue(userData1.isPresent(), "User data with id 1234 not present");
////
////            Assert.isTrue(userData1.equals(userData), "Object retrieved not equal");
//
//            for (UserData userData1 : userDataRepository.findAll()) {
//                System.out.println("User data = " + userData1);
//            }
//        } else {
//            System.out.println("User Data repository is null.");
//        }
//    }
//}
