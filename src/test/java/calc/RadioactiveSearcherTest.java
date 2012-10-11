package calc;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: oleg
 * Date: 16.11.11 18:25
 */
public class RadioactiveSearcherTest {

    @Test
    public void test() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);

        s.testAndRecord(new BitSet(0, 1), 0);
        s.testAndRecord(new BitSet(2, 3), 1);

        assertEquals(4, s.getTestTable().length);
        assertEquals(1<<0, s.getTestTable()[0]);
        assertEquals(1<<0, s.getTestTable()[1]);
        assertEquals(1<<1, s.getTestTable()[2]);
        assertEquals(1<<1, s.getTestTable()[3]);
    }

    @Test
    public void isSufficient() {
        int testCombinationSize = 3;
        RadioactiveSearcher s = new RadioactiveSearcher(7, 15, 2);
        RadioactiveSearcher.ExperimentResult testResults = s.test(BitSet.buildSet(testCombinationSize));

        assertEquals(testResults.falseCount, BitSet.c(s.getBallsCount() - testCombinationSize, s.getRadiocativeCount()));
        //RadioactiveSearcher.isSufficient(s.getBallsCount(), s.getRadiocativeCount(), testCombinationSize);
    }

    @Test
    public void canProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);

        s.testAndRecord(new BitSet(0, 1), 0);
        s.testAndRecord(new BitSet(0, 2), 1);

        assertEquals(true, s.canPerformNextAttempt(1));
    }

    @Test
    public void canNotProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);

        s.testAndRecord(new BitSet(0, 1), 0);
        s.testAndRecord(new BitSet(2, 3), 1);

        assertEquals(false, s.canPerformNextAttempt(1));
    }
    @Test
    public void search3_8_1() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        s.search();
    }

    @Test
    @Ignore
    public void search7_15_2() {
        RadioactiveSearcher s = new RadioactiveSearcher(7, 15, 2);
        s.search();
    }

    @Test
    @Ignore
    public void search6_8_2() {
        RadioactiveSearcher s = new RadioactiveSearcher(6, 8, 2);
        s.search();
    }

}
