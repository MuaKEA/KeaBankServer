package keabank.kea.dk.demo.Logic;

import java.time.LocalDate;
import java.time.Period;


public class AgeCalculator {

    public static int getAge(String Cpr)  {

        LocalDate l = LocalDate.of(Integer.valueOf("19" + Cpr.substring(4, 6)), Integer.valueOf(Cpr.substring(2, 4)), Integer.valueOf(Cpr.substring(0, 2))); //specify year, month, date directly
        LocalDate now = LocalDate.now(); //gets localDate
        Period diff = Period.between(l, now); //difference between the dates is calculated
        System.out.println(diff.getYears() + "years" + diff.getMonths() + "months" + diff.getDays() + "days");



        return diff.getYears();

    }
}