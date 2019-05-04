package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.*;
import keabank.kea.dk.demo.Repositories.ITransactionsQuaue;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.apache.catalina.User;
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
import java.time.temporal.ChronoUnit;
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


@PostMapping("/SaveSavingsAccount")
    public ResponseEntity createsavingsacc(@RequestParam(name = "Email") String Email,
                                           @RequestParam(name = "Accountname") String accountname,
                                           @RequestParam(name="Fromaccount") String Fromaccount,
                                           @RequestParam(name="AccountType") String AccountType,
                                           @RequestParam(name="ammount") double ammount,
                                           @RequestParam(name="date") String paymentdate,
                                           @RequestParam(name="automatedsetting") String automatedsetting ){



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

            switch (automatedsetting) {

                case "Yearly":
                    automatedyears(ammount, accountNumberAndRegistration, accountNumber, registration);

                    break;

                case "Monthly":
                    automatedmounth(ammount, accountNumberAndRegistration, accountNumber, registration);

                    break;


                case "Dayli":
                    daylipayment(ammount, accountNumberAndRegistration, accountNumber, registration);

                    break;

                case "weeakly":

                    automatedweeks(ammount, accountNumberAndRegistration, accountNumber, registration);

                    break;


                case "date":

                    //2019-05-04
                    // dd-MM-yyyy  //specify year, month, date directly
                    int day = Integer.valueOf(paymentdate.substring(9));
                    System.out.println(day);
                    int mouth = Integer.valueOf(paymentdate.substring(6, 7));
                    System.out.println(mouth);
                    int year = Integer.valueOf(paymentdate.substring(0,4));
                    System.out.println(year);


                    LocalDate date=LocalDate.of(year,mouth,day);

                    System.out.println(date);
                 paydate(ammount, accountNumberAndRegistration, accountNumber, registration,date);

                    break;










            }







        }


    }

        return new ResponseEntity(HttpStatus.OK);

        }

    private void automatedweeks(@RequestParam(name = "ammount") double ammount, AccountNumberAndRegistration accountNumberAndRegistration, Long accountNumber, Long registration) {


            LocalDate today = LocalDate.now();
            System.out.println("Current date: " + today);
            //add 2 week to the current date

        for (int i = 1; i <12 ; i++) {

            LocalDate weeaks = today.plus(i, ChronoUnit.WEEKS);
            iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, weeaks));
        }

    }


    private void automatedmounth(@RequestParam(name = "ammount") double ammount, AccountNumberAndRegistration accountNumberAndRegistration, Long accountNumber, Long registration) {


        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);
        //add 2 week to the current date

        for (int i = 1; i <12 ; i++) {


            LocalDate weeaks = today.plus(i, ChronoUnit.MONTHS);
            iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, weeaks));
        }



    }

    private void daylipayment(@RequestParam(name = "ammount") double ammount, AccountNumberAndRegistration accountNumberAndRegistration, Long accountNumber, Long registration) {


        LocalDate today = LocalDate.now();

        for (int i = 1; i <30 ; i++) {
            LocalDate weeaks = today.plus(i, ChronoUnit.DAYS);
            iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, weeaks));
        }



    }


    private void automatedyears(@RequestParam(name = "ammount") double ammount, AccountNumberAndRegistration accountNumberAndRegistration, Long accountNumber, Long registration) {


        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);
        //add 2 week to the current date

        for (int i = 1; i <12 ; i++) {


            LocalDate weeaks = today.plus(i, ChronoUnit.YEARS);
            iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, weeaks));
        }



    }

    private void paydate(@RequestParam(name = "ammount") double ammount, AccountNumberAndRegistration accountNumberAndRegistration, Long accountNumber, Long registration, LocalDate date) {
        iTransactionsQuaue.save(new TransactionsQuaue("savings", accountNumber, registration, accountNumberAndRegistration.getAccountNumber(), accountNumberAndRegistration.getRegistrationNumber(), ammount, date));
    }


    @GetMapping("/getdate")
    public void date() {

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