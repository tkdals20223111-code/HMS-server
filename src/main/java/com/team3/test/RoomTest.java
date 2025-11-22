package server.test;

import server.dao.RoomDAO;
import server.model.Room;
import java.util.List;

public class RoomTest {
    public static void main(String[] args) {
        System.out.println("=== RoomDAO 테스트 시작 ===");
        
        RoomDAO dao = new RoomDAO();
        
        // 1. 방 추가 테스트
        System.out.println("[1] 방 추가 시도...");
        // 201호, 80000원, 보증X
        boolean result1 = dao.addRoom(new Room(201, 80000, false));
        // 202호, 120000원, 보증O
        boolean result2 = dao.addRoom(new Room(202, 120000, true));
        
        System.out.println("201호 추가 결과: " + result1);
        System.out.println("202호 추가 결과: " + result2);
        
        // 2. 전체 방 목록 조회 테스트
        System.out.println("\n[2] 전체 방 목록 조회...");
        List<Room> rooms = dao.loadRooms();
        
        if (rooms.isEmpty()) {
            System.out.println("방이 없습니다.");
        } else {
            for (Room r : rooms) {
                System.out.println(r.toString());
            }
        }
        
        System.out.println("=== 테스트 종료 ===");
    }
}