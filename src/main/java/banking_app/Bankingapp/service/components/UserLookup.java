package banking_app.Bankingapp.service.components;

import banking_app.Bankingapp.exception.UserNotFoundException;
import banking_app.Bankingapp.model.User;
import banking_app.Bankingapp.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserLookup {

    private final UserRepository userRepository;

    public UserLookup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

    }
}
