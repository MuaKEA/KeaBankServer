package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.Accounts;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {
@Autowired
    UserLoginRepo userLoginRepo;


    @PostMapping("/newAccount")
    public ResponseEntity<String> newAccount(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String accountname, @RequestParam(name = "AccountType") String AccountType){
        List<TransActions> transActions= new ArrayList<>();
        UserLogin user=userLoginRepo.findByEmail(Email);
        Accounts accounts= new Accounts(accountname,AccountType,0.0,transActions);
        user.getAccountsList().add(accounts);
        userLoginRepo.save(user);


        return new ResponseEntity<>("senior", HttpStatus.OK);
    }
    @GetMapping("/getaccounts")
    public ResponseEntity<UserLogin> getaccounts(@RequestParam(name = "Email") String Email){

        UserLogin user= userLoginRepo.findByEmail(Email);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<UserLogin>> getallaccounts(){
       Iterable<UserLogin> user= userLoginRepo.findAll();

        return new ResponseEntity<>(user,HttpStatus.OK);
    }


 @GetMapping("AccountTransfers")
    public ResponseEntity<List<TransActions>> getallAccountTranactions(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String Accountname){
       UserLogin user= userLoginRepo.findByEmail(Email);
      List<TransActions> transActions;

     for (int i = 0; i <user.getAccountsList().size() ; i++) {

         if (user.getAccountsList().get(i).getAccount().equals(Accountname)){
             transActions=user.getAccountsList().get(i).getTransActions();
             return new ResponseEntity<>(transActions,HttpStatus.OK);

         }
     }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
 }
}
