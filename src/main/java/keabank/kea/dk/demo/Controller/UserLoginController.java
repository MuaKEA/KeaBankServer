package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.AccountNumberAndRegistration;
import keabank.kea.dk.demo.Model.Accounts;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.ITransActions;
import keabank.kea.dk.demo.Repositories.Iaccountsrepository;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static keabank.kea.dk.demo.Logic.GenerateRegistrationNumber.getregistrationNumber;

@RestController
public class UserLoginController {
    @Autowired
    UserLoginRepo userLoginRepo;
    @Autowired
    Iaccountsrepository iaccountsrepository;
    @Autowired
    ITransActions iTransActions;
    private Long RegistrationNumber=4444L;


    @PostMapping("/createuser")
    public ResponseEntity<UserLogin> saveLogin(@RequestParam( name="fullname") String fullname,@RequestParam( name="username") String username,@RequestParam( name="Cpr") String Cpr, @RequestParam(name = "password") String password){
        List<TransActions> transActions= new ArrayList<>();
        List<Accounts> accountsArrayList= new ArrayList<>();
        AccountNumberAndRegistration accountNumberAndRegistration = new AccountNumberAndRegistration(getregistrationNumber(RegistrationNumber),RegistrationNumber);

        accountsArrayList.add(new Accounts("Keabank","Standart",generatenumber(),transActions,accountNumberAndRegistration));
        accountsArrayList.add(new Accounts("Budget","Standart",0.0,transActions,accountNumberAndRegistration));

        UserLogin login= new UserLogin(fullname,username,Cpr,password,accountsArrayList);

        userLoginRepo.save(login);


        return new ResponseEntity<>(login,HttpStatus.OK);


    }



    @PostMapping("/loginvalidation")
    public ResponseEntity uservalidation(@RequestParam( name="username") String username, @RequestParam(name = "password") String password){
        Optional<UserLogin>user=userLoginRepo.findByEmailAndPassword(username,password);
        System.out.println(user.isPresent());

        if (user.isPresent()){

            return new ResponseEntity(HttpStatus.OK);


        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);


    }


public double generatenumber(){
    double start = 10000;
    double end = 37000;
    double random = new Random().nextDouble();
    double result = start + (random * (end - start));
    double generated=Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(result)).replace(",","."));



        return generated;
}




}
