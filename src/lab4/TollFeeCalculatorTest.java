package lab4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeParseException;


@DisplayName("Testing TollFeeCalculator")
public class TollFeeCalculatorTest {

    private Object IOException;

    @Test
    @DisplayName("Check if Exception is thrown where it should be")
    void CheckIfThrowsIsThrownAsItShould() {
        //Test 1
        //Check if ToManyDaysInFileException is thrown

        //Test 2
        //Check if DateTimeParseException is thrown

        //Test 3
        //Check if IOException is thrown
        LocalDateTime[] dates = new LocalDateTime[2];
        dates[0] = LocalDateTime.of(2020,9,26,10,15); //Lördag
        dates[1] = LocalDateTime.of(2020,9,27,10,15); //Söndag



    }

    @Test
    @DisplayName("Check if tollfree works")
    void CheckIfTollFreeWorks() {
        //2020-09-26 -> Lördag
        //2020-09-27 -> Söndag
        LocalDateTime[] dates = new LocalDateTime[5];
        dates[0] = LocalDateTime.of(2020,9,26,10,15); //Lördag
        dates[1] = LocalDateTime.of(2020,9,27,10,15); //Söndag
        dates[2] = LocalDateTime.of(2020,7,1,10,15); //Juli månad
        dates[3] = LocalDateTime.of(2020,9,23,10,15); //onsdag
        dates[4] = LocalDateTime.of(2020,9,24,10,15); //Torsdag

        assertTrue(TollFeeCalculator.isTollFreeDate(dates[0]));
        assertTrue(TollFeeCalculator.isTollFreeDate(dates[1]));
        assertTrue(TollFeeCalculator.isTollFreeDate(dates[2]));
        assertFalse(TollFeeCalculator.isTollFreeDate(dates[3]));
        assertFalse(TollFeeCalculator.isTollFreeDate(dates[4]));
    }

    @Test
    @DisplayName("Check if tollFeeValue is right for specific times & totalmaximum is returned as 60")
    void RightTollFeeAndTotalMaximumReturned() throws ToManyDaysInFileException {
        //Before test, creates an array of times that i know specific values for.
        LocalDateTime[] dates = new LocalDateTime[13];
        dates[0] = LocalDateTime.of(2020,9,23,5,0); // 0
        dates[1] = LocalDateTime.of(2020,9,23,6,0); //8
        dates[2] = LocalDateTime.of(2020,9,23,6,30); //13
        dates[3] = LocalDateTime.of(2020,9,23,7,0); //18
        dates[4] = LocalDateTime.of(2020,9,23,8,0); //13
        dates[5] = LocalDateTime.of(2020,9,23,8,30); //8
        dates[6] = LocalDateTime.of(2020,9,23,13,30); //8
        dates[7] = LocalDateTime.of(2020,9,23,15,15); //13
        dates[8] = LocalDateTime.of(2020,9,23,15,30); //18
        dates[9] = LocalDateTime.of(2020,9,23,17,30); //13
        dates[10] = LocalDateTime.of(2020,9,23,18,13); //8
        dates[11] = LocalDateTime.of(2020,9,23,18,30); //0
        dates[12] = LocalDateTime.of(2020,9,23,21,0); //0

        //Test 1
        // Check if the right tollFee is set.
        //Check returned value for each specific array to control if the right value is returned.
        assertEquals(0, TollFeeCalculator.getTollFeePerPassing(dates[0])); //0
        assertEquals(8, TollFeeCalculator.getTollFeePerPassing(dates[1])); //8
        assertEquals(13, TollFeeCalculator.getTollFeePerPassing(dates[2])); //13
        assertEquals(18, TollFeeCalculator.getTollFeePerPassing(dates[3])); //18
        assertEquals(13, TollFeeCalculator.getTollFeePerPassing(dates[4])); //13
        assertEquals(8, TollFeeCalculator.getTollFeePerPassing(dates[5])); //8
        assertEquals(8, TollFeeCalculator.getTollFeePerPassing(dates[6])); //8
        assertEquals(13, TollFeeCalculator.getTollFeePerPassing(dates[7])); //13
        assertEquals(18, TollFeeCalculator.getTollFeePerPassing(dates[8])); //18
        assertEquals(13, TollFeeCalculator.getTollFeePerPassing(dates[9])); //13
        assertEquals(8, TollFeeCalculator.getTollFeePerPassing(dates[10])); //8
        assertEquals(0, TollFeeCalculator.getTollFeePerPassing(dates[11])); //0
        assertEquals(0, TollFeeCalculator.getTollFeePerPassing(dates[12])); //0

        //Test 3
        //Checking that the maximum of the file is 60 since i know the dates above will be 83.
        assertEquals(60, TollFeeCalculator.getTotalFeeCost(dates));
    }

    @Test
    @DisplayName("Check if totaldayfee is correct if many passages under 60minutes")
    void ManyPassagesUnder60Minutes() {
        //Check with a inputfiles with many passages under 60minutes that i know the value of.
        //Check with a inputfiles with many passages under 60minutes that i know the value of.

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call main to run lab4.txt to check output
        String[] src = new String[1];
        src[0] = "" + new TollFeeCalculator("src/lab4/januarimassapassagerunder60min.txt");

        TollFeeCalculator.main(src);

        // Deal with line breaks that are different between systems. Convert all other line breaks to \n.
        String actualOutput = outContent.toString();
        actualOutput = actualOutput.replace("\r\n", "\n"); // Windows uses \r\n
        actualOutput = actualOutput.replace("\r", "\n"); // Old MacOs uses \r

        //Now you have to validate the output
        String expectedOutput =
            "2020-01-01T06:34\n" +
            "Passage cost: 13\n" +
            "Total toll: 13\n" +
            "\n"+
            "2020-01-01T07:33\n" +
            "Passage inside 60minutes window, +5 due to more expensive toll\n" +
            "Total toll: 18\n" +
            "\n" +
            "2020-01-01T08:32\n" +
            "Passage cost: 8\n" +
            "Total toll: 26\n" +
            "\n" +
            "2020-01-01T09:31\n" +
            "Free passage due to inside 60minutes window\n" +
            "Total toll: 26\n" +
            "\n" +
            "2020-01-01T10:30\n" +
            "Passage cost: 8\n" +
            "Total toll: 34\n" +
            "\n" +
            "2020-01-01T11:29\n" +
            "Free passage due to inside 60minutes window\n" +
            "Total toll: 34\n" +
            "\n" +
            "2020-01-01T12:28\n" +
            "Passage cost: 8\n" +
            "Total toll: 42\n" +
            "\n" +
            "2020-01-01T13:27\n" +
            "Free passage due to inside 60minutes window\n" +
            "Total toll: 42\n" +
            "\n" +
            "2020-01-01T14:26\n" +
            "Passage cost: 8\n" +
            "Total toll: 50\n" +
            "\n" +
            "2020-01-01T15:25\n"+
            "Passage inside 60minutes window, +5 due to more expensive toll\n" +
            "Total toll: 55\n"+
            "\n"+
            "The total fee for the inputfile is 55\n";

        // Do the actual assertion
        assertEquals(expectedOutput, actualOutput);
    }
}
