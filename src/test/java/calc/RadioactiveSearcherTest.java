package calc;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: oleg
 * Date: 16.11.11 18:25
 */
public class RadioactiveSearcherTest {

    @Test
    public void test() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);
        int[] experimentTable = s.buildExperimentTable();

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).items);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(2, 3)).items);

        assertEquals(4, experimentTable.length);
        assertEquals(1<<0, experimentTable[0]);
        assertEquals(1<<0, experimentTable[1]);
        assertEquals(1<<1, experimentTable[2]);
        assertEquals(1<<1, experimentTable[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notResolvable() {
        new RadioactiveSearcher(6, 15, 2);
    }

    @Test
    public void testDistance() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);

        System.out.println();
        System.out.println();

        RadioactiveSearcher.ExperimentResult origin = s.getSearchSpace()[0];
        RadioactiveSearcher.ExperimentResult point = s.getSearchSpace()[9];

        int d = s.distance(Arrays.asList(origin), point);

        assertEquals(0, d);
    }

    @Test
    public void testBuildSearchSpace() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        assertEquals(BitSet.c(8,4), s.getSearchSpace().length);
    }

    @Test
    public void testIsSufficient() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        RadioactiveSearcher.ExperimentResult testResults = s.runExperiment(BitSet.buildSet(3));

        int[] expCounts = s.experimentCounts(3);
        assertEquals(testResults.falseCount, expCounts[0]);
        assertEquals(testResults.trueCount, expCounts[1]);

        assertFalse(s.isSufficient(1));
        assertFalse(s.isSufficient(2));
        assertFalse(s.isSufficient(3));
        assertTrue(s.isSufficient(4));
        assertFalse(s.isSufficient(5));
    }

    @Test
    public void testCanProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);
        int[] experimentTable = s.buildExperimentTable();

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).items);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(0, 2)).items);

        assertEquals(true, s.canPerformNextAttempt(1, experimentTable));
    }

    @Test
    public void testCanNotProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);
        int[] experimentTable = s.buildExperimentTable();

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).items);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(2, 3)).items);

        assertEquals(false, s.canPerformNextAttempt(1, experimentTable));
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
