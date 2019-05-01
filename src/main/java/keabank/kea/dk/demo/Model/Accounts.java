package keabank.kea.dk.demo.Model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Account;
    private String accounttype;
    private double Currentdeposit;
    @OneToMany(cascade = {CascadeType.ALL})
    List<TransActions> transActions;
    @OneToOne
    AccountNumberAndRegistration accountNumberAndRegistration;

    public Accounts() {


    }

    public Accounts(String account, String accounttype, double currentdeposit, List<TransActions> transActions, AccountNumberAndRegistration accountNumberAndRegistration) {
        Account = account;
        this.accounttype = accounttype;
        Currentdeposit = currentdeposit;
        this.transActions = transActions;
        this.accountNumberAndRegistration = accountNumberAndRegistration;
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

    public List<TransActions> getTransActions() {
        return transActions;
    }

    public void setTransActions(List<TransActions> transActions) {
        this.transActions = transActions;
    }

    public AccountNumberAndRegistration getAccountNumberAndRegistration() {
        return accountNumberAndRegistration;
    }

    public void setAccountNumberAndRegistration(AccountNumberAndRegistration accountNumberAndRegistration) {
        this.accountNumberAndRegistration = accountNumberAndRegistration;
    }
}