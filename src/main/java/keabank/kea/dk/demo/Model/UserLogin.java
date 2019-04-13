package keabank.kea.dk.demo.Model;


import javax.persistence.*;
import java.util.List;

@Entity
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    @OneToMany(cascade = {CascadeType.ALL})
    List<Accounts> accountsList;

    public UserLogin() {

    }

    public UserLogin(String fullName, String email, String password, List<Accounts> accountsList) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.accountsList = accountsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Accounts> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(List<Accounts> accountsList) {
        this.accountsList = accountsList;
    }
}