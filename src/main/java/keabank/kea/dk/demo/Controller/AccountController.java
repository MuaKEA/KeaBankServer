package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.*;
import keabank.kea.dk.demo.Repositories.ITransactionsQuaue;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static keabank.kea.dk.demo.Logic.GenerateRegistrationNumber.getregistrationNumber;

@RestController
public class AccountController {
@Autowired
    UserLoginRepo userLoginRepo;
@Autowired
    ITransactionsQuaue iTransactionsQuaue;

    public Long RegistrationNumber=4444L;

    @PostMapping("/newAccount")
    public ResponseEntity<String> newAccount(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String accountname, @RequestParam(name = "AccountType") String AccountType){
        List<TransActions> transActions= new ArrayList<>();
        UserLogin user=userLoginRepo.findByEmail(Email);
        AccountNumberAndRegistration accountNumberAndRegistration = new AccountNumberAndRegistration(getregistrationNumber(RegistrationNumber), RegistrationNumber);
            Accounts account = new Accounts(accountname, AccountType, 0.0, transActions, accountNumberAndRegistration);
            user.getAccountsList().add(account);
            userLoginRepo.save(user);


        return new ResponseEntity<>("senior", HttpStatus.OK);
    }
    @GetMapping("/getaccounts")
    public ResponseEntity<UserLogin> getaccounts(@RequestParam(name = "Email") String Email){

        UserLogin user= userLoginRepo.findByEmail(Email);
        List<Accounts> accountsArrayList= user.getAccountsList();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<UserLogin>> getallaccounts(){
       Iterable<UserLogin> user= userLoginRepo.findAll();

        return new ResponseEntity<>(user,HttpStatus.OK);
    }


 @GetMapping("/AccountTransfers")
    public ResponseEntity getallAccountTranactions(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String Accountname){
       UserLogin user= userLoginRepo.findByEmail(Email);
      Accounts transActions;

     for (int i = 0; i <user.getAccountsList().size() ; i++) {

         if (user.getAccountsList().get(i).getAccount().equals(Accountname)){
             transActions=user.getAccountsList().get(i);
             return new ResponseEntity<>(transActions,HttpStatus.OK);

         }
     }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
 }

@GetMapping("getaccount")
public ResponseEntity<Accounts> getaccount(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String account){
        UserLogin userLogin = userLoginRepo.findByEmail(Email);
        for (int i = 0; i <userLogin.getAccountsList().size() ; i++) {

            if (userLogin.getAccountsList().get(i).getAccount().equals(account)){
            Accounts accounts = userLogin.getAccountsList().get(i);
            return new ResponseEntity<>(accounts,HttpStatus.OK);

        }
    }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}


@GetMapping("createsavingsacc")
    public ResponseEntity createsavingsacc(@RequestParam(name = "Email") String Email, @RequestParam(name = "Accountname") String accountname,
                                           @RequestParam(name = "AccountType") String AccountType,
                                           @RequestParam(name="Fromaccount") String Fromaccount,
                                           @RequestParam(name="ammount") double ammount,
                                           @RequestParam(name="automatictype") String automatictype ){


    List<TransActions> transActions= new ArrayList<>();
    AccountNumberAndRegistration accountNumberAndRegistration = new AccountNumberAndRegistration(getregistrationNumber(RegistrationNumber),RegistrationNumber);
    UserLogin user=userLoginRepo.findByEmail(Email);
    Accounts account= new Accounts(accountname,AccountType,0.0,transActions,accountNumberAndRegistration);
    user.getAccountsList().add(account);
    userLoginRepo.save(user);

    for (int i = 0; i <user.getAccountsList().size() ; i++) {

        if (user.getAccountsList().get(i).getAccount().equals(Fromaccount)) {
            Long accountNumber = user.getAccountsList().get(i).getAccountNumberAndRegistration().getAccountNumber();
            Long registration = user.getAccountsList().get(i).getAccountNumberAndRegistration().getRegistrationNumber();


            for (int j = 4; j < 12; j++) {
                String datenow = " yyyy-MM-dd";
                LocalDate l = LocalDate.of(2019, j, 01); //specify year, month, date directly
                SimpleDateFormat simpleDate = new SimpleDateFormat(datenow);
                String date = simpleDate.format(l);
                iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, date));

            }


        }
    }



        return new ResponseEntity(HttpStatus.OK);

        }

    @GetMapping("/getdate")
    public void date(){

        for (int j = 4; j < 12; j++) {
            String datenow = " yyyy-MM-dd";
            LocalDate l = LocalDate.of(2019, j, 01); //specify year, month, date directly
            SimpleDateFormat simpleDate = new SimpleDateFormat(datenow);
            String date = simpleDate.format(l.toString());
            System.out.println(date);
        }
        }









    public void datediffence(int year , int moth , int day) {
        LocalDate l = LocalDate.of(year, moth,day); //specify year, month, date directly
        LocalDate now = LocalDate.now(); //gets localDate
        Period diff = Period.between(l, now); //difference between the dates is calculated
        System.out.println(diff.getYears());

    }

}