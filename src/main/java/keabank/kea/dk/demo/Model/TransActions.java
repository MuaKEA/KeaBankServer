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
    private double transactionAmmount;
    private double dopositBeforeTransaction;
    private double dopositAfterTransaction;
    private String date;
    private String time;
    private boolean sendingOrreciving;

    public TransActions(){

    }

    public TransActions(String transactionName, double transactionAmmount, double dopositBeforeTransaction, double dopositAfterTransaction,String date,String time, boolean sendingOrreciving) {
        this.transactionName = transactionName;
        this.transactionAmmount = transactionAmmount;
        this.dopositBeforeTransaction = dopositBeforeTransaction;
        this.dopositAfterTransaction = dopositAfterTransaction;
        this.time=time;
        this.date=date;
        this.sendingOrreciving = sendingOrreciving;
    }

    public TransActions(String transactionName, double transactionAmmount, double dopositBeforeTransaction, double dopositAfterTransaction,String date, boolean sendingOrreciving) {
        this.transactionName = transactionName;
        this.transactionAmmount = transactionAmmount;
        this.dopositBeforeTransaction = dopositBeforeTransaction;
        this.dopositAfterTransaction = dopositAfterTransaction;
        this.date=date;
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

    public double getTransactionAmmount() {
        return transactionAmmount;
    }

    public void setTransactionAmmount(double transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public double getDopositBeforeTransaction() {
        return dopositBeforeTransaction;
    }

    public void setDopositBeforeTransaction(double dopositBeforeTransaction) {
        this.dopositBeforeTransaction = dopositBeforeTransaction;
    }

    public double getDopositAfterTransaction() {
        return dopositAfterTransaction;
    }

    public void setDopositAfterTransaction(double dopositAfterTransaction) {
        this.dopositAfterTransaction = dopositAfterTransaction;
    }

    public boolean isSendingOrreciving() {
        return sendingOrreciving;
    }

    public void setSendingOrreciving(boolean sendingOrreciving) {
        this.sendingOrreciving = sendingOrreciving;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
