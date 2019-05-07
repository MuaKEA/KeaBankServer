package keabank.kea.dk.demo.Controller;


import keabank.kea.dk.demo.Model.Accounts;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.TransactionsQuaue;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.ITransactionsQuaue;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static keabank.kea.dk.demo.Logic.AgeCalculator.getAge;
import static keabank.kea.dk.demo.Logic.GenerateRegistrationNumber.getaccountnumber;



@RestController
public class SendingAndReceivingController {
    private Long RegistrationNumber = 4444L;
    private Boolean isautomatictrasactionsrunning;

    @Autowired
    UserLoginRepo userLoginRepo;
    @Autowired
    ITransactionsQuaue iTransactionsQuaue;
    @Autowired
    Iaccountsrepository iaccountsrepository;


    @PostMapping("/sendmoneyToOtherAccount")
    public ResponseEntity<UserLogin> Sendmoney(@RequestParam(name = "Email") String Email, @RequestParam(name = "TranceActionName") String TranceActionName, @RequestParam(name = "fromAccount") String Faccount, @RequestParam(name = "ToAccount") String toAccount, @RequestParam(name = "value") Double value, @RequestParam(name = "sendingorReciving") boolean sendingorrecing) {
        UserLogin user = userLoginRepo.findByEmail(Email);
        UserLogin user2 = userLoginRepo.findByEmail(Email);
        String datenow = " yyyy-MM-dd";
        String timenow = " HH:mm:ss";
        SimpleDateFormat simpleDate = new SimpleDateFormat(datenow);
        SimpleDateFormat simpletime = new SimpleDateFormat(timenow);
        Date datetime= new Date();
        String date = simpleDate.format(datetime);
        String time = simpletime.format(datetime);


        for (int i = 0; i < user.getAccountsList().size(); i++) {
            if (user.getAccountsList().get(i).getAccount().equals(Faccount)) {
                user.getAccountsList().get(i).getTransActions().add(new TransActions(TranceActionName,value,user.getAccountsList().get(i).getCurrentdeposit(),remowedicimals(user.getAccountsList().get(i).getCurrentdeposit()-value),date,time,sendingorrecing));
                user.getAccountsList().get(i).setCurrentdeposit(remowedicimals(user.getAccountsList().get(i).getCurrentdeposit() - value));
            }
        }

            for (int j = 0; j <user2.getAccountsList().size() ; j++) {
                if(user2.getAccountsList().get(j).getAccount().equals(toAccount)){
                    user2.getAccountsList().get(j).getTransActions().add(new TransActions(TranceActionName,value,user2.getAccountsList().get(j).getCurrentdeposit(),remowedicimals(user2.getAccountsList().get(j).getCurrentdeposit()+value),date,time,false));
                    user2.getAccountsList().get(j).setCurrentdeposit(remowedicimals(user.getAccountsList().get(j).getCurrentdeposit()+value));
                }
            }
            userLoginRepo.save(user);
            userLoginRepo.save(user2);

            return new ResponseEntity<>(userLoginRepo.findByEmail(Email), HttpStatus.OK);

    }


    @GetMapping("/validateAge")
 public ResponseEntity getCostumerAge(@RequestParam(name = "Email") String Email) {
        UserLogin userLogin = userLoginRepo.findByEmail(Email);

        if (getAge(userLogin.getCpr()) >= 77) {
            System.out.println("conditions is true");
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

@PostMapping("/startTransactionsService")
    public void startTransactionsService() {

      Thread transacions = new Thread(()->{





        List<TransactionsQuaue> transQ = iTransactionsQuaue.findByDateBeforeOrDate(LocalDate.now(), LocalDate.now());
        List<UserLogin> userLoginList = userLoginRepo.findAll();

        for (int i = 0; i < transQ.size(); i++) {
            Accounts Fromaccount = iaccountsrepository.findAllByRegistrationnumberAndAccountNumber(transQ.get(i).getFromregistrationNumber(), transQ.get(i).getFromaccountNumber());
            Accounts Toaccount = iaccountsrepository.findAllByRegistrationnumberAndAccountNumber(transQ.get(i).getTOgistrationNumber(), transQ.get(i).getTocountNumber());

            for (int j = 0; j < userLoginList.size(); j++) {

                for (int k = 0; k < userLoginList.get(j).getAccountsList().size(); k++) {

                    if (userLoginList.get(j).getAccountsList().get(k).equals(Fromaccount)) {
                        UserLogin Fromuser = userLoginRepo.findByEmail(userLoginList.get(j).getEmail());
                        Fromuser.getAccountsList().get(k).getTransActions().add(new TransActions("TO" + Toaccount.getAccount(), transQ.get(i).getTransactionAmmount(), Fromuser.getAccountsList().get(k).getCurrentdeposit(), remowedicimals(Fromuser.getAccountsList().get(k).getCurrentdeposit() - transQ.get(i).getTransactionAmmount()), LocalDate.now().toString(), true));
                        Fromuser.getAccountsList().get(k).setCurrentdeposit(remowedicimals(Fromuser.getAccountsList().get(k).getCurrentdeposit() - transQ.get(i).getTransactionAmmount()));

                        userLoginRepo.save(Fromuser);

                    }
                    if (userLoginList.get(j).getAccountsList().get(k).equals(Toaccount)) {
                        UserLogin TOuser = userLoginRepo.findByEmail(userLoginList.get(j).getEmail());
                        TOuser.getAccountsList().get(k).getTransActions().add(new TransActions("From" + Fromaccount.getAccount(), transQ.get(i).getTransactionAmmount(), TOuser.getAccountsList().get(k).getCurrentdeposit(), remowedicimals(TOuser.getAccountsList().get(k).getCurrentdeposit() + transQ.get(i).getTransactionAmmount()), LocalDate.now().toString(), false));
                        TOuser.getAccountsList().get(k).setCurrentdeposit(remowedicimals(TOuser.getAccountsList().get(k).getCurrentdeposit() + transQ.get(i).getTransactionAmmount()));
                        userLoginRepo.save(TOuser);
                    }
                }
            }
            iTransactionsQuaue.delete(transQ.get(i));

        }
          try {
              System.out.println("before sleep");
              Thread.sleep(60000);
              System.out.println("after sleep");
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      });
      transacions.start();

    }

    @PostMapping("/sendtootherusers")
    public ResponseEntity sendtootherusers(@RequestParam(name = "Email") String email, @RequestParam(name = "Fromaccout") String Fromaccout,@RequestParam(name = "Fromatype") String Fromatype, @RequestParam(name = "reg") Long reg,@RequestParam(name = "accountnb") Long accountnb,@RequestParam(name = "ammount") double ammount){
        Accounts accounts;
        UserLogin userLogin= userLoginRepo.findByEmail(email);
        for (int i = 0; i <userLogin.getAccountsList().size(); i++) {

            if(userLogin.getAccountsList().get(i).getAccount().equals(Fromaccout) && userLogin.getAccountsList().get(i).getAccounttype().equals(Fromatype)){
                accounts  =userLogin.getAccountsList().get(i);
                iTransactionsQuaue.save(new TransactionsQuaue("From:" + Fromaccout, accounts.getAccountNumber(), accounts.getregistrationnumber(), accountnb, reg, ammount, LocalDate.now()));

            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }




    @PostMapping("/SaveSavingsAccount")
    public ResponseEntity createsavingsacc(@RequestParam(name = "Email") String Email,
                                           @RequestParam(name = "Accountname") String accountname,
                                           @RequestParam(name = "Fromaccount") String Fromaccount,
                                           @RequestParam(name = "AccountType") String AccountType,
                                           @RequestParam(name = "ammount") double ammount,
                                           @RequestParam(name = "date") String paymentdate,
                                           @RequestParam(name = "automatedsetting") String automatedsetting) {

        List<TransActions> transActions = new ArrayList<>();
        UserLogin user = userLoginRepo.findByEmail(Email);
        Accounts savingsaccount = new Accounts(accountname, AccountType, 0.0, RegistrationNumber, getaccountnumber(RegistrationNumber), transActions);
        user.getAccountsList().add(savingsaccount);
        userLoginRepo.save(user);

        for (int i = 0; i < user.getAccountsList().size(); i++) {

            if (user.getAccountsList().get(i).getAccount().equals(Fromaccount)) {
                System.out.println(automatedsetting);
                switch (automatedsetting) {

                    case "Yearly":
                        System.out.println("Monthly Yearly");
                        automatedyears(ammount, user.getAccountsList().get(i), savingsaccount);

                        break;

                    case "Monthly":
                        System.out.println("Monthly");
                        automatedmounth(ammount, user.getAccountsList().get(i), savingsaccount);

                        break;


                    case "Dayli":
                        System.out.println("Dayli");
                        daylipayment(ammount, user.getAccountsList().get(i), savingsaccount);

                        break;

                    case "weekly":
                        System.out.println("weeakly");
                        automatedweeks(ammount, user.getAccountsList().get(i), savingsaccount);

                        break;


                    case "date":
                        // dd-MM-yyyy  //specify year, month, date directly
                        int day = Integer.valueOf(paymentdate.substring(9));
                        System.out.println(day);
                        int mouth = Integer.valueOf(paymentdate.substring(6, 7));
                        System.out.println(mouth);
                        int year = Integer.valueOf(paymentdate.substring(0, 4));
                        System.out.println(year);


                        LocalDate date = LocalDate.of(year, mouth, day + 1);
                        for (int j = 0; j < 10; j++) {
                            paydate(ammount, user.getAccountsList().get(i), savingsaccount, date);
                        }

                        break;
                }
            }
        }

        return new ResponseEntity(HttpStatus.OK);

    }

    private void automatedweeks(@RequestParam(name = "ammount") double ammount, Accounts From, Accounts TO) {


        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);

        for (int i = 1; i < 12; i++) {
            LocalDate weeaks = today.plus(i, ChronoUnit.WEEKS);
            System.out.println(weeaks + " weeks");
            iTransactionsQuaue.save(new TransactionsQuaue("From:" + From.getAccount(), From.getAccountNumber(), From.getregistrationnumber(), TO.getAccountNumber(), TO.getregistrationnumber(), ammount, weeaks));
        }

    }


    private void automatedmounth(@RequestParam(name = "ammount") double ammount, Accounts From, Accounts TO) {


        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);
        //add 2 week to the current date

        for (int i = 1; i < 12; i++) {
            LocalDate weeaks = today.plus(i, ChronoUnit.MONTHS);
            System.out.println(weeaks + " months");
            iTransactionsQuaue.save(new TransactionsQuaue("From:" + From.getAccount(), From.getAccountNumber(), From.getregistrationnumber(), TO.getAccountNumber(), TO.getregistrationnumber(), ammount, weeaks));
        }


    }

    private void daylipayment(@RequestParam(name = "ammount") double ammount, Accounts From, Accounts TO) {


        LocalDate today = LocalDate.now();

        for (int i = 1; i < 30; i++) {
            LocalDate weeaks = today.plus(i, ChronoUnit.DAYS);
            System.out.println(weeaks + " days");

            iTransactionsQuaue.save(new TransactionsQuaue("From:" + From.getAccount(), From.getAccountNumber(), From.getregistrationnumber(), TO.getAccountNumber(), TO.getregistrationnumber(), ammount, weeaks));
        }
    }


    private void automatedyears(@RequestParam(name = "ammount") double ammount, Accounts From, Accounts TO) {

        LocalDate today = LocalDate.now();
        System.out.println("Current date: " + today);

        for (int i = 1; i < 12; i++) {

            LocalDate weeaks = today.plus(i, ChronoUnit.YEARS);
            System.out.println(weeaks.toString());
            iTransactionsQuaue.save(new TransactionsQuaue("From:" + From.getAccount(), From.getAccountNumber(), From.getregistrationnumber(), TO.getAccountNumber(), TO.getregistrationnumber(), ammount, weeaks));
        }
    }

    private void paydate(@RequestParam(name = "ammount") double ammount, Accounts From, Accounts TO, LocalDate localDate) {


        iTransactionsQuaue.save(new TransactionsQuaue("From:" + From.getAccount(), From.getAccountNumber(), From.getregistrationnumber(), TO.getAccountNumber(), TO.getregistrationnumber(), ammount, localDate));

    }

    @GetMapping("/getCpr")
    public ResponseEntity<UserLogin> getCpr(@RequestParam(name = "Email") String Email){
        UserLogin userLogin=new UserLogin(userLoginRepo.findByEmail(Email).getCpr());

        return new ResponseEntity<>(userLogin,HttpStatus.OK);
}

    private double remowedicimals(double amount){
        double generated=Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(amount)).replace(",","."));
        return generated;
    }




}


