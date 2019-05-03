package keabank.kea.dk.demo.Controller;


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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static keabank.kea.dk.demo.Logic.AgeCalculator.getAge;

@RestController
public class SendingAndReceivingController {

    @Autowired
    UserLoginRepo userLoginRepo;


    @PostMapping("/sendmoneyToOtherAccount")
    public ResponseEntity<UserLogin> Sendmoney(@RequestParam(name = "Email") String Email, @RequestParam(name = "TranceActionName") String TranceActionName, @RequestParam(name = "fromAccount") String Faccount, @RequestParam(name = "ToAccount") String toAccount, @RequestParam(name = "value") Double value, @RequestParam(name = "sendingorReciving") boolean sendingorrecing) {
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


        return new ResponseEntity<>(userLoginRepo.findByEmail(Email), HttpStatus.OK);


    }


    @GetMapping("/validateAge")
 public ResponseEntity getCostumerAge(@RequestParam(name = "Email") String Email) {
        UserLogin userLogin = userLoginRepo.findByEmail(Email);

        if (getAge(userLogin.getCpr()) >= 77) {
            System.out.println("conditions is true");
            return new ResponseEntity(HttpStatus.OK);

        }
        System.out.println("conditions is false");


        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }


@GetMapping("/getCpr")
public ResponseEntity<UserLogin> getCpr(@RequestParam(name = "Email") String Email){
        UserLogin userLogin=new UserLogin(userLoginRepo.findByEmail(Email).getCpr());

        return new ResponseEntity<>(userLogin,HttpStatus.OK);
}

    public double remowedicimals(double amount ){
        double generated=Double.valueOf(String.valueOf(new DecimalFormat("#.##").format(amount)).replace(",","."));



        return generated;
    }

@GetMapping("/getcurrentdeposit")
    public ResponseEntity getdeposit(@RequestParam(name = "Email") String Email, @RequestParam(name = "account") String account){
    UserLogin userLogin =userLoginRepo.findByEmail(Email);


        return new ResponseEntity(userLogin,HttpStatus.OK);



}


}