package server.dao;

import server.model.Room;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RoomDAO {

    private static final String FILE_PATH = "data/rooms.txt";

    public List<Room> loadRooms() {
        List<Room> list = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                addRoom(new Room(101, 100000, false));
                addRoom(new Room(102, 150000, true));
            } catch (IOException e) {}
            return list; 
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] token = line.split(",");
                if (token.length >= 2) {
                    int no = Integer.parseInt(token[0].trim());
                    int price = Integer.parseInt(token[1].trim());
                    boolean guaranteed = token.length >= 3 && Boolean.parseBoolean(token[2].trim());
                    list.add(new Room(no, price, guaranteed));
                }
            }
        } catch (Exception e) {}
        return list;
    }

    public boolean addRoom(Room room) {
        if (findRoom(room.getRoomNumber()) != null) return false; 
        return appendToFile(room);
    }
    
    // [SFR-3.8] 객실 정보 수정 (가격 변경 등)
    public boolean updateRoom(Room updatedRoom) {
        List<Room> rooms = loadRooms();
        boolean found = false;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == updatedRoom.getRoomNumber()) {
                rooms.set(i, updatedRoom); // 리스트 교체
                found = true;
                break;
            }
        }
        if (found) saveAllRooms(rooms);
        return found;
    }

    // [SFR-3.7] 객실 삭제
    public boolean deleteRoom(int roomNumber) {
        List<Room> rooms = loadRooms();
        boolean removed = rooms.removeIf(r -> r.getRoomNumber() == roomNumber);
        if (removed) saveAllRooms(rooms);
        return removed;
    }

    public Room findRoom(int roomNumber) {
        for (Room r : loadRooms()) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }

    private boolean appendToFile(Room r) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH, true), StandardCharsets.UTF_8))) {
            bw.write(r.getRoomNumber() + "," + r.getPrice() + "," + r.isGuaranteed());
            bw.newLine();
            return true;
        } catch (IOException e) { return false; }
    }

    public void saveAllRooms(List<Room> rooms) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH, false), StandardCharsets.UTF_8))) {
            for (Room r : rooms) {
                bw.write(r.getRoomNumber() + "," + r.getPrice() + "," + r.isGuaranteed());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}