package keabank.kea.dk.demo.Logic;

import java.util.Random;

public class GenerateRegistrationNumber {


    public static Long getregistrationNumber(Long RegistrationNumber ) {
        long x = 999999L;
        long y = 100000L;
        Random r = new Random();
        long number = x+((long)(r.nextDouble()*(y-x)));
        String putregistratonnr=String.valueOf(RegistrationNumber) + number;

        return Long.parseLong(putregistratonnr);
    }

}
