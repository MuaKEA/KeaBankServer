package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Logic.Mailservice;
import keabank.kea.dk.demo.Model.Accounts;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.ITransActions;
import keabank.kea.dk.demo.Repositories.Iaccountsrepository;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.DecimalFormat;
import java.util.*;

import static keabank.kea.dk.demo.Logic.GenerateRegistrationNumber.getaccountnumber;


@RestController
public class UserLoginController {
    @Autowired
    UserLoginRepo userLoginRepo;
    @Autowired
    Iaccountsrepository iaccountsrepository;
    @Autowired
    ITransActions iTransActions;
    @Autowired
    Mailservice mailservice;

    private Long RegistrationNumber = 4444L;
    private HashMap<Integer,String> temporaryPassword = new HashMap<>();


    @PostMapping("/sendValidationCode")
    public ResponseEntity validateemail(@RequestParam(name = "Email") String Email){
        Integer ConformEmailcode=generatepassword();
        mailservice.sendemail(Email,"Welcome to Kea bank!, input these numbers:"+ConformEmailcode +  " to, \nconform your eamil");
        temporaryPassword.put(ConformEmailcode,Email);


        return new ResponseEntity(HttpStatus.OK);
    }



    @PostMapping("/createuser")
    public ResponseEntity<UserLogin> saveLogin(@RequestParam(name = "fullname") String fullname, @RequestParam(name = "username") String username, @RequestParam(name = "Cpr") String Cpr, @RequestParam(name = "password") String password,@RequestParam(name = "conformationscode") Integer conformationscode) {
        List<TransActions> transActions = new ArrayList<>();
        List<Accounts> accountsArrayList = new ArrayList<>();

       if(temporaryPassword.get(conformationscode).equals(username)) {

           accountsArrayList.add(new Accounts("Keabank", "Standart", generatenumber(), RegistrationNumber, getaccountnumber(RegistrationNumber), transActions));
           accountsArrayList.add(new Accounts("Budget", "Standart", 0.0, RegistrationNumber, getaccountnumber(RegistrationNumber), transActions));
           UserLogin login = new UserLogin(fullname, username, Cpr, password, accountsArrayList);
           userLoginRepo.save(login);

           return new ResponseEntity<>(HttpStatus.OK);

       }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }


    @PostMapping("/loginvalidation")
    public ResponseEntity uservalidation(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        Optional<UserLogin> user = userLoginRepo.findByEmailAndPassword(username, password);
        System.out.println(user.isPresent());

        if (user.isPresent()) {

            return new ResponseEntity(HttpStatus.OK);

        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);


    }


    public double generatenumber() {
        double start = 10000;
        double end = 37000;
        double random = new Random().nextDouble();
        double result = start + (random * (end - start));
        double generated = Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(result)).replace(",", "."));


        return generated;
    }

    public Integer generatepassword() {
        Random ran= new Random();
        Integer generated=ran.nextInt(999999-100000);

        return generated;

    }


}