package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.Bills;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.BillsRepo;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
public class BillsController {
    @Autowired
    BillsRepo billsRepo;
    @Autowired
    UserLoginRepo userLoginRepo;



    @GetMapping("/checkbillsexist")
    public ResponseEntity getBill(@RequestParam(name = "digits")String obligatoryDigits, @RequestParam(name = "accountnumber") Long accountNumber,
                                  @RequestParam(name = "registrationNumber") Long registrationNumber){


        System.out.println(obligatoryDigits +" " +  accountNumber + " " + "" + registrationNumber);

     Optional<Bills> bills=billsRepo.findByObligatoryDigitsAndAccountNumberAndRegistrationNumber( "+"+obligatoryDigits, accountNumber, registrationNumber);


                if(bills.isPresent()){
                    return new ResponseEntity(HttpStatus.OK);
                }

                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/fakebils")
    public ResponseEntity makefakebils(){

       Bills bills = new Bills("Dong-energy",100.0,"+71",102030405060L,10203014L);
       Bills bills1 = new Bills("3Mobil",350.0,"+71",203040506070L,10203015L);
       Bills bills2= new Bills("Asb-bolig",9764.0,"+71",304050607080L,10203016L);
       Bills bills3 = new Bills("parkio",750.0,"+71",405060708090L,10203017L);
       Bills bills4 = new Bills("radius",950.0,"+71",506070809000L,10203018L);

       billsRepo.save(bills);
       billsRepo.save(bills1);
       billsRepo.save(bills2);
       billsRepo.save(bills3);
       billsRepo.save(bills4);

     return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/paybill")
    public ResponseEntity<Bills> paybils(@RequestParam(name = "Email") String Email, @RequestParam(name = "TranceActionName") String TranceActionName, @RequestParam(name = "fromAccount") String Faccount, @RequestParam(name = "value") Double value,@RequestParam(name = "date") String date, @RequestParam(name = "sendingorReciving") boolean sendingorrecing) {

        String datenow = " yyyy-MM-dd";
        String timenow = " HH:mm:ss";
        //using simpleDateformat for formatting the date return value
        SimpleDateFormat simpleDate = new SimpleDateFormat(datenow);
        SimpleDateFormat simpletime = new SimpleDateFormat(timenow);
        Date datetime= new Date();
        String dateformated = simpleDate.format(datetime);
        String time = simpletime.format(datetime);


        UserLogin userLogin = userLoginRepo.findByEmail(Email);


        for (int i = 0; i <userLogin.getAccountsList().size() ; i++) {

            if(userLogin.getAccountsList().get(i).getAccount().equals(Faccount)){
                userLogin.getAccountsList().get(i).getTransActions().add(new TransActions(TranceActionName,value,userLogin.getAccountsList().get(i).getCurrentdeposit(),userLogin.getAccountsList().get(i).getCurrentdeposit()-value,date, time ,sendingorrecing));
                userLogin.getAccountsList().get(i).setCurrentdeposit(remowedicimals(userLogin.getAccountsList().get(i).getCurrentdeposit() - value));
                userLoginRepo.save(userLogin);
            }
        }

     return new ResponseEntity<>(HttpStatus.OK);
    }

    public double remowedicimals(double amount ){
        double generated=Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(amount)).replace(",","."));



        return generated;
    }
}
