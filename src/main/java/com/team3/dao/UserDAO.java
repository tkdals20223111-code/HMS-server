package server.dao;

import server.model.User;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UserDAO {
    private static final String FILE_PATH = "data/users.txt";

    public List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                addUser(new User("admin", "1234", "관리자", "ADMIN"));
            } catch (IOException e) {}
            return list;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] t = line.split(",");
                if (t.length == 4) list.add(new User(t[0], t[1], t[2], t[3]));
            }
        } catch (Exception e) {}
        return list;
    }

    public boolean addUser(User u) {
        for(User existing : loadUsers()) {
            if(existing.getId().equals(u.getId())) return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH, true), StandardCharsets.UTF_8))) {
            bw.write(u.getId() + "," + u.getPw() + "," + u.getName() + "," + u.getRole());
            bw.newLine();
            return true;
        } catch (IOException e) { return false; }
    }
    
    // [SFR-3.8 & 3.9] 사용자 정보 수정 (비밀번호, 이름, 권한 등)
    public boolean updateUser(User updatedUser) {
        List<User> users = loadUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser); // 정보 교체
                found = true;
                break;
            }
        }
        if (found) saveAllUsers(users);
        return found;
    }

    public boolean deleteUser(String id) {
        List<User> users = loadUsers();
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) saveAllUsers(users);
        return removed;
    }
    
    public User loginCheck(String id, String pw) {
        for(User u : loadUsers()) {
            if(u.getId().equals(id) && u.getPw().equals(pw)) return u;
        }
        return null;
    }

    private void saveAllUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH, false), StandardCharsets.UTF_8))) {
            for (User u : users) {
                bw.write(u.getId() + "," + u.getPw() + "," + u.getName() + "," + u.getRole());
                bw.newLine();
            }
        } catch (IOException e) {}
    }
}