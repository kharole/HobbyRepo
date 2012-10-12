package calc;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

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

    @Test
    public void buildSearchSpace() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        Map<RadioactiveSearcher.ExperimentResult, BitSet> searchSpace = s.buildSearchSpace();
        assertEquals(BitSet.c(8,4), searchSpace.size());
    }

    @Test
    public void isSufficient() {
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
    public void canProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);
        int[] experimentTable = s.buildExperimentTable();

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).items);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(0, 2)).items);

        assertEquals(true, s.canPerformNextAttempt(1, experimentTable));
    }

    @Test
    public void canNotProceed() {
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
    public void search6_8_2() {
        RadioactiveSearcher s = new RadioactiveSearcher(6, 8, 2);
        s.search();
    }

}
