package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.Accounts;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.ITransActions;
import keabank.kea.dk.demo.Repositories.Iaccountsrepository;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

@RestController
public class UserLoginController {
    @Autowired
    UserLoginRepo userLoginRepo;
    @Autowired
    Iaccountsrepository iaccountsrepository;
    @Autowired
    ITransActions iTransActions;


    @PostMapping("/createuser")
    public ResponseEntity saveLogin(@RequestParam( name="fullname") String fullname,@RequestParam( name="username") String username, @RequestParam(name = "password") String password){
        List<TransActions> transActions= new ArrayList<>();
        List<Accounts> accountsArrayList= new ArrayList<>();

        accountsArrayList.add(new Accounts("Keabank",generatenumber(),transActions));
        UserLogin login= new UserLogin(fullname,username,password,accountsArrayList);

        userLoginRepo.save(login);


        return new ResponseEntity(login,HttpStatus.OK);


    }

    @GetMapping("/loginvalidation")
    public ResponseEntity uservalidation(@RequestParam( name="username") String username, @RequestParam(name = "password") String password){
        Optional<UserLogin>user=userLoginRepo.findByEmailAndPassword(username,password);

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
