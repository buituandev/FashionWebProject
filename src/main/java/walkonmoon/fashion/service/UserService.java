package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.repository.UserRepository;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getListUser() {
        return (List<User>) userRepo.findAll();
    }
    @Transactional
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    @Transactional
    public User getUserByToken(String token) {return userRepo.findByToken(token);}

    public static String toSHA1(String password) {
        String salt = "chromashop";
        String result = null;
        password = password + salt;
        try {
            byte[] dataBytes = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            result = Base64.encodeBase64String(md.digest(dataBytes));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Transactional
    public void saveUser(User user) {
        userRepo.save(user);
    }
    @Transactional
    public User findUserById(int id) {
        Optional<User> user =  userRepo.findById(id);
        return user.orElse(null);
    }

    public String createPasswordResetToken(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            UUID uuid = UUID.randomUUID();
            user.setToken(uuid.toString());
            user.setTokenExpired(LocalDateTime.now().plusHours(1));
            System.out.println("test token");
            System.out.println(user.getToken());
            System.out.println(user.getTokenExpired());
            userRepo.save(user);
            return uuid.toString();
        }
        return null;
    }

}
