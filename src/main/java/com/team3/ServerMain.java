package server; // [중요] 패키지명이 server 여야 함!

import java.io.*;
import java.net.*;
import server.controller.*; // 다른 패키지 컨트롤러 가져오기
import server.model.Protocol;

public class ServerMain {
    // 컨트롤러들
    private static AuthController authController = new AuthController();
    private static UserController userController = new UserController();
    private static RoomController roomController = new RoomController(); 
    private static PaymentController paymentController = new PaymentController();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("=== [호텔 관리 시스템 서버] 시작됨 (포트: 3000) ===");

            while (true) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Protocol req = (Protocol) in.readObject();
                Protocol res = null;
                int type = req.getType();

                // 라우팅
                if (type >= 100 && type < 200) res = authController.handleRequest(req);
                else if (type >= 200 && type < 300) res = userController.handleRequest(req);
                else if (type >= 300 && type < 400) { // 보고서
                    server.dao.ReportDAO reportDAO = new server.dao.ReportDAO();
                    res = new Protocol(Protocol.PT_SUCCESS, reportDAO.getPriceChangeReport());
                }
                else if (type >= 400 && type < 500) res = paymentController.handleRequest(req);
                else if (type >= 500 && type < 600) res = roomController.handleRequest(req);
                else res = new Protocol(Protocol.PT_FAIL, "잘못된 요청 코드: " + type);

                if (res != null) { out.writeObject(res); out.flush(); }
                socket.close();
            }
        } catch (Exception e) { System.out.println("[서버 에러] " + e.getMessage()); }
    }
}