package server.controller;

import java.util.List;
import server.dao.RoomDAO;
import server.model.*;

public class RoomController {
    
    private final RoomDAO roomDAO = new RoomDAO();

    public Protocol handleRequest(Protocol req) {
        switch (req.getType()) {
            case Protocol.PT_ROOM_ADD_REQ -> {
                Room newRoom = (Room) req.getData();
                boolean isSuccess = roomDAO.addRoom(newRoom);
                if (isSuccess) {
                    return new Protocol(Protocol.PT_SUCCESS, "객실 추가 성공: " + newRoom.getRoomNumber() + "호");
                } else {
                    return new Protocol(Protocol.PT_FAIL, "객실 추가 실패: 이미 존재하는 번호입니다.");
                }
            }
            case Protocol.PT_ROOM_LIST_REQ -> {
                List<Room> rooms = roomDAO.loadRooms();
                return new Protocol(Protocol.PT_SUCCESS, rooms); // 리스트 자체를 응답으로 보냄
            }
            default -> {
                return new Protocol(Protocol.PT_FAIL, "알 수 없는 객실 요청");
            }
        }
        // [503] 방 추가
        // [501] 방 목록 조회 (여기 추가됨!)
            }
}