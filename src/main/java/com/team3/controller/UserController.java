package server.controller;

import server.dao.UserDAO;
import server.model.*;

public class UserController {
    private UserDAO userDAO = new UserDAO();

    public Protocol handleRequest(Protocol req) {
        switch (req.getType()) {
            case Protocol.PT_USER_ADD_REQ:
                User u = (User) req.getData();
                if(u.getRole()==null) u.setRole("USER");
                return userDAO.addUser(u) ? new Protocol(Protocol.PT_SUCCESS, "추가 성공") 
                                          : new Protocol(Protocol.PT_FAIL, "추가 실패");
            case Protocol.PT_USER_LIST_REQ:
                return new Protocol(Protocol.PT_SUCCESS, userDAO.loadUsers());
            case Protocol.PT_USER_DELETE_REQ:
                return userDAO.deleteUser((String)req.getData()) ? new Protocol(Protocol.PT_SUCCESS, "삭제 성공")
                                                                 : new Protocol(Protocol.PT_FAIL, "삭제 실패");
            default: return new Protocol(Protocol.PT_FAIL, "알 수 없는 요청");
        }
    }
}