package financeapp.service;

import financeapp.model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    private static final String FILE_DATA = "users_data.dat";

    public static void saveData (Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_DATA))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, User> loadUsers() {
        File file = new File(FILE_DATA);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_DATA))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
