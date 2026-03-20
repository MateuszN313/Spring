package User;

import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {
    private final IUserRepository userRepository;

    public Authentication(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }

    public User authenticate(String login, String password){
        String hashedPassword = hashPassword(password);
        User user = userRepository.getUser(login);
        if(user != null && hashedPassword.equals(user.getPassword())) return user;
        return null;
    }
}
