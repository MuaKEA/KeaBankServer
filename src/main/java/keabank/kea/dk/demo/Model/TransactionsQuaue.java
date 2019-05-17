package keabank.kea.dk.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class TransactionsQuaue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transactionName;
    private String texttoReciever;
    private Long FromaccountNumber;
    private Long FromregistrationNumber;
    private Long TocountNumber;
    private Long TOgistrationNumber;
    private double transactionAmmount;
    private LocalDate date;

    public TransactionsQuaue(){


    }

    public TransactionsQuaue(String transactionName,String texttoReciever ,Long fromaccountNumber, Long fromregistrationNumber, Long tocountNumber, Long TOgistrationNumber, double transactionAmmount, LocalDate date) {
        this.transactionName = transactionName;
        this.texttoReciever = texttoReciever;
        this.FromaccountNumber = fromaccountNumber;
        this.FromregistrationNumber = fromregistrationNumber;
        this.TocountNumber = tocountNumber;
        this.TOgistrationNumber = TOgistrationNumber;
        this.transactionAmmount = transactionAmmount;
        this.date = date;
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

    public Long getFromaccountNumber() {
        return FromaccountNumber;
    }

    public void setFromaccountNumber(Long fromaccountNumber) {
        FromaccountNumber = fromaccountNumber;
    }

    public Long getFromregistrationNumber() {
        return FromregistrationNumber;
    }

    public void setFromregistrationNumber(Long fromregistrationNumber) {
        FromregistrationNumber = fromregistrationNumber;
    }

    public Long getTocountNumber() {
        return TocountNumber;
    }

    public void setTocountNumber(Long tocountNumber) {
        TocountNumber = tocountNumber;
    }

    public Long getTOgistrationNumber() {
        return TOgistrationNumber;
    }

    public void setTOgistrationNumber(Long TOgistrationNumber) {
        this.TOgistrationNumber = TOgistrationNumber;
    }

    public double getTransactionAmmount() {
        return transactionAmmount;
    }

    public void setTransactionAmmount(double transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTexttoReciever() {
        return texttoReciever;
    }

    public void setTexttoReciever(String texttoReciever) {
        this.texttoReciever = texttoReciever;
    }
}
