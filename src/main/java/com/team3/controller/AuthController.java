package server.controller;

import server.dao.UserDAO;
import server.model.*;

public class AuthController {
    private UserDAO userDAO = new UserDAO();

    public Protocol handleRequest(Protocol req) {
        if (req.getType() == Protocol.PT_LOGIN_REQ) {
            User u = (User) req.getData();
            User result = userDAO.loginCheck(u.getId(), u.getPw());
            if (result != null) return new Protocol(Protocol.PT_LOGIN_RES, result);
            else return new Protocol(Protocol.PT_LOGIN_FAIL, null);
        }
        return new Protocol(Protocol.PT_FAIL, "인증 오류");
    }
}