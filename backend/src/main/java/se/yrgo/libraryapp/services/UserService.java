package se.yrgo.libraryapp.services;

import java.util.Optional;
import javax.inject.Inject;

import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

public class UserService {
    private final UserDao userDao;
    private  PasswordEncoder encoder;

    @Inject
   public UserService(UserDao userDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    public Optional<UserId> validate(String username, String password) {
        Optional<LoginInfo> maybeLoginInfo = userDao.getLoginInfo(username);
        if (maybeLoginInfo.isEmpty()) {
            return Optional.empty();
        }

        LoginInfo loginInfo = maybeLoginInfo.get();

        if (!encoder.matches(password, loginInfo.getPasswordHash())) {
            return Optional.empty();
        }

        return Optional.of(loginInfo.getUserId());
    }

    public String hashPassword(String password){
        return encoder.encode(password);
    }

    public boolean nameQualifierCheck(String name) {
        if (name == null || name.trim().length() < 3) {
            return false;
        }
        else

            return userDao.isNameAvailable(name);

    }

    public boolean registerUser(String name, String realname, String password){
        if (!nameQualifierCheck(name)){
            return false;
        }

        // handle names like Ian O'Toole
        realname = realname.replace("'", "\\'");

        String hashedpassword = hashPassword(password);
        return userDao.register(name,realname,hashedpassword);
    }



    public UserDao getUserDao() {
        return userDao;
    }

    public PasswordEncoder getEncoder() {
        return encoder;
    }
}
