package lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        try {
            calculator(inputFile);
        } catch (ToManyDaysInFileException | MinutesInWrongOrderException e) {
            System.err.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Could not read the file. Check over time format in the file.");
        } catch (FileNotFoundException e) {
            System.err.println("File is inaccesible: " + inputFile);
        } catch (Exception e) {
            System.err.println("Something else went wrong...");
        }
    }

    public static void calculator(String inputFile) throws ToManyDaysInFileException, FileNotFoundException, MinutesInWrongOrderException {
        Scanner sc = new Scanner(new File(inputFile));
        String[] dateStrings = sc.nextLine().split(", ");
        LocalDateTime[] dates = new LocalDateTime[dateStrings.length]; //Bug. Didnt use the whole file, skipped the last array place.
        for (int i = 0; i < dates.length; i++) {
            dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        sc.close(); //Added close of scanner.
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) throws ToManyDaysInFileException, MinutesInWrongOrderException {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int xCharge = 0;
        for(LocalDateTime date: dates) {
            long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
            System.out.println(date.toString());
            //System.out.println("Last passage was: " + diffInMinutes + " minutes ago");
            if (date.getDayOfWeek() != intervalStart.getDayOfWeek()) {
                //Bug i guess. was stated in labpm that only one day would be in each file.
                throw new ToManyDaysInFileException("Only one day per file is allowed."); //Illegal eller missmatch.
            } else if (date.isBefore(intervalStart)) { //Lösa detta argument.
                //Bug, Minutes in wrong order
                throw new MinutesInWrongOrderException("Minutes in wrong order. Check over the inputfile.");
            } else if (diffInMinutes >= 60 || date.equals(intervalStart)) {      //Bug doesnt charge first time., Bug2 added that diffMinutes need to be >= 60.
                totalFee += getTollFeePerPassing(date);
                System.out.println("Passage cost: " + getTollFeePerPassing(date));
                xCharge = 0;
                if (!date.equals(intervalStart)) {
                   intervalStart = date;
                }
            } else {
                //Bug Does not take in consideration the difference in tollfee if passage inside a 60minute window.
                if (getTollFeePerPassing(date) > getTollFeePerPassing(intervalStart)) {
                    if (xCharge == getTollFeePerPassing(date)) {
                        System.out.println("Free passage due to inside 60minutes window");
                    } else {
                        int diff = (getTollFeePerPassing(date) - getTollFeePerPassing(intervalStart));
                        System.out.println("Passage inside 60minutes window, +" + diff + " due to more expensive toll");
                        totalFee += diff;
                        xCharge = getTollFeePerPassing(date);
                    }
                } else {
                    System.out.println("Free passage due to inside 60minutes window");
                }
            }
            System.out.println("Total toll: " + totalFee);
            System.out.println();  
        }
        return Math.min(totalFee,60); //Bug: was returning max value always, but changed to always returning the lowest.
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;            //Mkt överflödig kod, men fungerar. så ingen bugg i mina ögon.
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 8 || hour >= 9 && hour <= 14 && minute <= 59) return 8; //Bug: Not right return value during 9:00-14:29
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        /*
        new TollFeeCalculator("src/lab4/txtfiler/10passager1h.txt");
        new TollFeeCalculator("src/lab4/wrongordertime.txt");
        new TollFeeCalculator("src/lab4/feloutput.txt");
        new TollFeeCalculator("src/lab4/januarimassapassagerunder60min.txt");
        new TollFeeCalculator("src/lab4/Lab4.txt");
        new TollFeeCalculator("src/lab4/RightandMax.txt");
        new TollFeeCalculator("src/lab4/shayantest.txt");
        new TollFeeCalculator("src/lab4/gustavtest2.txt");
        new TollFeeCalculator("src/lab4/söndagspassager.txt");
        new TollFeeCalculator("src/lab4/julifrimånad.txt");
        new TollFeeCalculator("src/lab4/jessicatest.txt");
        new TollFeeCalculator("src/lab4/gustavsproblem.txt");
        new TollFeeCalculator("src/lab4/datumifelordning.txt"); //löst genom att kasta ett DateTimeException.
        new TollFeeCalculator(("src/lab4/tredatumiföljdmedsammatider.txt"));
        */
    }
}