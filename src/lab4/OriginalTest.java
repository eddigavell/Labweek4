package lab4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;


public class OriginalTest {
    @Test
    @DisplayName("Check if tollfree works")
    void CheckIfTollFreeWorks() {
        LocalDateTime[] dates = new LocalDateTime[5];
        dates[0] = LocalDateTime.of(2020,9,26,10,15); //Lördag
        dates[1] = LocalDateTime.of(2020,9,27,10,15); //Söndag
        dates[2] = LocalDateTime.of(2020,7,1,10,15); //Juli månad
        dates[3] = LocalDateTime.of(2020,9,23,10,15); //onsdag
        dates[4] = LocalDateTime.of(2020,9,24,10,15); //Torsdag

        assertTrue(Original.isTollFreeDate(dates[0]));
        assertTrue(Original.isTollFreeDate(dates[1]));
        assertTrue(Original.isTollFreeDate(dates[2]));
        assertFalse(Original.isTollFreeDate(dates[3]));
        assertFalse(Original.isTollFreeDate(dates[4]));
    }

    @Test
    @DisplayName("Check if getTollFeePerPassing returning right Toll value")
    void getTollFeePerPassing() {
        //Write time to the method and check returned value.
        LocalDateTime[] dates = new LocalDateTime[13];
        dates[0] = LocalDateTime.of(2020,9,23,5,0); // 0
        dates[1] = LocalDateTime.of(2020,9,23,6,0); //8
        dates[2] = LocalDateTime.of(2020,9,23,6,30); //13
        dates[3] = LocalDateTime.of(2020,9,23,7,0); //18
        dates[4] = LocalDateTime.of(2020,9,23,8,0); //13
        dates[5] = LocalDateTime.of(2020,9,23,9,0); //8
        dates[6] = LocalDateTime.of(2020,9,23,13,30); //8
        dates[7] = LocalDateTime.of(2020,9,23,15,15); //13
        dates[8] = LocalDateTime.of(2020,9,23,15,30); //18
        dates[9] = LocalDateTime.of(2020,9,23,17,30); //13
        dates[10] = LocalDateTime.of(2020,9,23,18,13); //8
        dates[11] = LocalDateTime.of(2020,9,23,18,30); //0
        dates[12] = LocalDateTime.of(2020,9,23,16,40); //0
        assertEquals(0, Original.getTollFeePerPassing(dates[0])); //0
        assertEquals(8, Original.getTollFeePerPassing(dates[1])); //8
        assertEquals(13, Original.getTollFeePerPassing(dates[2])); //13
        assertEquals(18, Original.getTollFeePerPassing(dates[3])); //18
        assertEquals(13, Original.getTollFeePerPassing(dates[4])); //13
        assertEquals(8, Original.getTollFeePerPassing(dates[5])); //8 //Bugg 1
        assertEquals(8, Original.getTollFeePerPassing(dates[6])); //8
        assertEquals(13, Original.getTollFeePerPassing(dates[7])); //13
        assertEquals(18, Original.getTollFeePerPassing(dates[8])); //18
        assertEquals(13, Original.getTollFeePerPassing(dates[9])); //13
        assertEquals(8, Original.getTollFeePerPassing(dates[10])); //8
        assertEquals(0, Original.getTollFeePerPassing(dates[11])); //0
        assertEquals(18, Original.getTollFeePerPassing(dates[12])); //0
    }

    @Test
    @DisplayName("Testing if first passage is charged")
    void TestFirstPassageCharged() {
        LocalDateTime[] dates = new LocalDateTime[13];
        dates[0] = LocalDateTime.of(2020,6,30,5,45); // 0
        dates[1] = LocalDateTime.of(2020,6,30,6,34); //8
        assertEquals(13, Original.getTollFeePerPassing(dates[0])); //13
        assertEquals(8, Original.getTollFeePerPassing(dates[1])); //8



    }

    @Test
    @DisplayName("Checking if whole array is read and also if max value is 60 per day")
    void CheckIfWholeArrayLengthIsReadAndIfMaxValueOfTheDayIsMaximum60() {
        //Check through array.length if everything ís handled and output is the same.
        //Check with a inputfiles with many passages under 60minutes that i know the value of.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

       /*
        // Call main to run lab4.txt to check output
        String[] src = new String[1];
        src[0] = "" + new TollFeeCalculator("src/lab4/Lab4.txt");

        TollFeeCalculator.main(src);
        */
        Original.main(null);

        // Deal with line breaks that are different between systems. Convert all other line breaks to \n.
        String actualOutput = outContent.toString();
        actualOutput = actualOutput.replace("\r\n", "\n"); // Windows uses \r\n
        actualOutput = actualOutput.replace("\r", "\n"); // Old MacOs uses \r

        //Now you have to validate the output
        String expectedOutput =
                "2020-06-30T00:05\n" +
                "Passage cost: 0\n" +
                "2020-06-30T06:34\n" +
                "Passage cost: 13\n" +
                "2020-06-30T08:52\n" +
                "Passage cost: 8\n" +
                "2020-06-30T10:13\n" +
                "Passage cost: 8\n" +
                "2020-06-30T10:25\n" +
                "Free passage due to 60minute window\n" +
                "2020-06-30T11:04\n" +
                "Free passage due to 60minute window\n" +
                "2020-06-30T16:50\n" +
                "Passage cost: 18\n" +
                "2020-06-30T18:00\n" +
                "Passage cost: 8\n" +
                "2020-06-30T21:30\n" +
                "Passage cost: 0\n" +
                "--------------------\n" +
                "Total price for the day: 55\n" +
                "\n" +
                "----- New Day: -----\n" +
                "2020-07-01T00:00\n" +
                "Tull free month.\n" +
                "The total fee for the inputfile is 55\n";
        // Do the actual assertion
        assertEquals(expectedOutput, actualOutput);

    }

}
