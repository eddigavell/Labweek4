package lab4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Original {

    public Original(String inputFile) {
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length-1]; //Bug 1 does not use the whole array (not reading whole file)
            for(int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the inputfile is" + getTotalFeeCost(dates));
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        }
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        for(LocalDateTime date: dates) {
            System.out.println(date.toString());
            long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
            if(diffInMinutes > 60) {
                totalFee += getTollFeePerPassing(date);
                System.out.println("Passage cost: " + getTollFeePerPassing(date));
                intervalStart = date;

            } else {
                int diff = Math.max(getTollFeePerPassing(date), getTollFeePerPassing(intervalStart)) - Math.min(getTollFeePerPassing(date), getTollFeePerPassing(intervalStart));
                totalFee += diff;  //Bug Does not take in consideration the max toll fee and subtract the lowest.
                System.out.println("Passage cost: " + diff);
            }
        }
        System.out.println(totalFee);
        return Math.max(totalFee, 60); //Bugg2 returnerar alltid största värdet, största värdet skall alltid vara max 60.
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) return 8; //Bug3 Not right return value during 9:00-14:29
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
        new Original("src/lab4/Lab4.txt");
    }
}
