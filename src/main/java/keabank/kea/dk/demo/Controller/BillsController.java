package keabank.kea.dk.demo.Controller;

import keabank.kea.dk.demo.Model.Bill;
import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.TransactionsQuaue;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.BillsRepo;
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
import java.util.Date;
import java.util.Optional;

@RestController
public class BillsController {
    @Autowired
    BillsRepo billsRepo;
    @Autowired
    UserLoginRepo userLoginRepo;
    @Autowired
    ITransactionsQuaue iTransactionsQuaue;



    @PostMapping("/checkbillsexist")
    public ResponseEntity getBill(@RequestParam(name = "digits")String obligatoryDigits, @RequestParam(name = "accountnumber") Long accountNumber,
                                  @RequestParam(name = "registrationNumber") Long registrationNumber){


        System.out.println(obligatoryDigits +" " +  accountNumber + " " + "" + registrationNumber);

     Optional<Bill> bills=billsRepo.findByObligatoryDigitsAndAccountNumberAndRegistrationNumber( "+"+obligatoryDigits, accountNumber, registrationNumber);


                if(bills.isPresent()){
                    System.out.println("bill exist");
                    return new ResponseEntity(HttpStatus.OK);
                }

                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/fakebils")
    public ResponseEntity makefakebils(){

       Bill bills = new Bill("Dong-energy",100.0,"+71",102030405060L,10203014L);
       Bill bills1 = new Bill("3Mobil",350.0,"+71",203040506070L,10203015L);
       Bill bills2= new Bill("Asb-bolig",9764.0,"+71",304050607080L,10203016L);
       Bill bills3 = new Bill("parkio",750.0,"+71",405060708090L,10203017L);
       Bill bills4 = new Bill("radius",950.0,"+71",506070809000L,10203018L);

       billsRepo.save(bills);
       billsRepo.save(bills1);
       billsRepo.save(bills2);
       billsRepo.save(bills3);
       billsRepo.save(bills4);

     return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/paybill")
    public ResponseEntity<Bill> paybils(@RequestParam(name = "Email") String Email,
                                        @RequestParam(name = "TranceActionName") String TranceActionName,
                                        @RequestParam(name = "fromAccount") String Faccount, @RequestParam(name = "value") Double value,
                                        @RequestParam(name = "date") String date,
                                        @RequestParam(name = "accountnumber") Long accountnumber,
                                        @RequestParam(name = "pbs",defaultValue = "false") boolean pbs,
                                        @RequestParam(name = "registrationNumber") Long registrationNumber,
                                        @RequestParam(name = "servicecode") String servicecode) {

//11-04-2019



        UserLogin userLogin = userLoginRepo.findByEmail(Email);


        for (int i = 0; i <userLogin.getAccountsList().size() ; i++) {

            if( userLogin.getSendserviceCode().equals(servicecode) && userLogin.getAccountsList().get(i).getAccount().equals(Faccount)){
                System.out.println("iam true");

               if(pbs){
                   LocalDate fromthefirstofmonth = LocalDate.of(Integer.valueOf(date.substring(6)), Integer.valueOf(date.substring(4, 5)),1);

                   for (int j = 1; j <12 ; j++) {

                       LocalDate paybillinthefurture = fromthefirstofmonth.plus(j, ChronoUnit.MONTHS);
                       TransactionsQuaue transactionsQuaue = new TransactionsQuaue(TranceActionName, userLogin.getAccountsList().get(i).getAccountNumber(), userLogin.getAccountsList().get(i).getregistrationnumber(), accountnumber, registrationNumber, value,paybillinthefurture );
                       iTransactionsQuaue.save(transactionsQuaue);
                   }
               }
               TransactionsQuaue transactionsQuaue = new TransactionsQuaue(TranceActionName, userLogin.getAccountsList().get(i).getAccountNumber(), userLogin.getAccountsList().get(i).getregistrationnumber(), accountnumber, registrationNumber, value, returnformateddate(date));
                iTransactionsQuaue.save(transactionsQuaue);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



    public LocalDate returnformateddate(String date){
        int day = Integer.valueOf(date.substring(0,2));
        System.out.println(day);
        int mouth = Integer.valueOf(date.substring(4, 5));
        System.out.println(mouth);
        int year = Integer.valueOf(date.substring(6));
        LocalDate localDate = LocalDate.of(year, mouth, day+1);

        return localDate;

    }
}
