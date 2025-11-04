package financeapp.service;

import financeapp.exception.InvalidCredentialsException;
import financeapp.exception.UserNotFoundException;
import financeapp.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users;
    private User currentUser;

    public UserService(Map<String, User> users) { this.users = users != null ? users : new HashMap<>(); }

    public void registerUser (String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }
        users.put(username, new User(username, password));
    }

    public User authenticateUser (String username, String password) {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException ("Неверный пароль");
        }
        currentUser = user;
        return user;
    }

    public User getCurrentUser () { return currentUser; }

    public void logout () { currentUser = null; }

    public boolean isUserLoggedIn () {
        return currentUser != null;
    }
}
