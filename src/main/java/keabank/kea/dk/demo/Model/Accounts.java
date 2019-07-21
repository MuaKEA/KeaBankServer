package keabank.kea.dk.demo.Model;


import javax.persistence.*;
import java.util.List;

@Entity
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Account;
    private String accounttype;
    private double Currentdeposit;
    private long registrationnumber;
    private long accountNumber;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<TransActions> transActions;

    public Accounts() {


    }

    public Accounts(String account, String accounttype, double currentdeposit, long registrationnumber, Long accountNumber, List<TransActions> transActions) {
        this.Account = account;
        this.accounttype = accounttype;
        this.Currentdeposit = currentdeposit;
        this. registrationnumber = registrationnumber;
        this.accountNumber = accountNumber;
        this.transActions = transActions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public double getCurrentdeposit() {
        return Currentdeposit;
    }

    public void setCurrentdeposit(double currentdeposit) {
        Currentdeposit = currentdeposit;
    }

    public Long getregistrationnumber() {
        return registrationnumber;
    }

    public void setregistrationnumber(Long registrationnumber) {
        this.registrationnumber = registrationnumber;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<TransActions> getTransActions() {
        return transActions;
    }

    public void setTransActions(List<TransActions> transActions) {
        this.transActions = transActions;
    }
}