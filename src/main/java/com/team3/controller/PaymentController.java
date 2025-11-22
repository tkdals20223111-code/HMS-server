package server.controller;

import java.util.List;
import server.dao.PaymentDAO;
import server.model.*;

public class PaymentController {
    
    private final PaymentDAO paymentDAO;

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
    }

    public Protocol handleRequest(Protocol req) {
        switch (req.getType()) {
            case Protocol.PT_PAYMENT_LIST_REQ -> {
                // DAO에서 모든 결제 기록을 가져옴
                List<Payment> list = paymentDAO.getPayments();
                // 리스트를 담아서 응답 보냄
                return new Protocol(Protocol.PT_SUCCESS, list);
            }

            default -> {
                return new Protocol(Protocol.PT_FAIL, "알 수 없는 결제 요청");
            }
        }
        // [401] 결제 목록 조회 요청
            }
}