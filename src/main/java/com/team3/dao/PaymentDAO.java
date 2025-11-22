package server.dao;

import server.model.Payment;
import java.util.ArrayList;

/**
 * 결제 기록을 저장하고 제공하는 DAO
 */
public class PaymentDAO {

    private final ArrayList<Payment> payments = new ArrayList<>();

    public boolean addPayment(Payment p) {
        return payments.add(p);
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }
}

