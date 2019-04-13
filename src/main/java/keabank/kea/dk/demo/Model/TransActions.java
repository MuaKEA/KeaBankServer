package keabank.kea.dk.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransActions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transactionName;
    private double transaction;
    private boolean sendingOrreciving;

    public TransActions(){

    }

    public TransActions(String transactionName, double transaction, boolean sendingOrreciving) {
        this.transactionName = transactionName;
        this.transaction = transaction;
        this.sendingOrreciving = sendingOrreciving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public double getTransaction() {
        return transaction;
    }

    public void setTransaction(double transaction) {
        this.transaction = transaction;
    }

    public boolean isSendingOrreciving() {
        return sendingOrreciving;
    }

    public void setSendingOrreciving(boolean sendingOrreciving) {
        this.sendingOrreciving = sendingOrreciving;
    }
}
