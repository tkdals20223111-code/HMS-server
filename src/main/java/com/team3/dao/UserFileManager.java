package server.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import server.model.User;

public class UserFileManager {
    private static final String FILE_PATH = "users.txt";

    // 사용자 정보를 파일에 저장
    public static void saveUser(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.getId() + "," + user.getPw());
            writer.newLine();
        }
    }

    // 파일에서 사용자 목록 불러오기
    public static List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        }
        return users;
    }

    // 특정 ID의 사용자 존재 여부 확인
    public static boolean userExists(String id) throws IOException {
        for (User user : loadUsers()) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}