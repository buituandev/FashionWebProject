package walkonmoon.fashion.service;

import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.repository.UserRepository;

import java.util.List;
@Service
public class UserService {
   private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getListUser() {
       return (List<User>) userRepo.findAll();
   }
}
