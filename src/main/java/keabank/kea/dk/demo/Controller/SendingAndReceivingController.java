package keabank.kea.dk.demo.Controller;


import keabank.kea.dk.demo.Model.TransActions;
import keabank.kea.dk.demo.Model.UserLogin;
import keabank.kea.dk.demo.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class SendingAndReceivingController {

    @Autowired
    UserLoginRepo userLoginRepo;


    @PostMapping("/sendmoneyToOtherAccount")
    public ResponseEntity Sendmoney(@RequestParam(name = "Email") String Email,@RequestParam(name = "TranceActionName") String TranceActionName, @RequestParam(name = "fromAccount") String Faccount, @RequestParam(name = "ToAccount") String toAccount, @RequestParam(name = "value") Double value, @RequestParam(name = "sendingorReciving") boolean sendingorrecing) {
        //finding account 1
        UserLogin user = userLoginRepo.findByEmail(Email);
       //finding account 2
        UserLogin user2 = userLoginRepo.findByEmail(Email);
        //pattern for the date
        String datenow = " yyyy-MM-dd";
        String timenow = " HH:mm:ss";
        //using simpleDateformat for formatting the date return value
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


        return new ResponseEntity(userLoginRepo.findByEmail(Email), HttpStatus.OK);


    }

    public double remowedicimals(double amount ){
        double generated=Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(amount)).replace(",","."));



        return generated;
    }
}