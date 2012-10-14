package calc;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

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

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).result);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(2, 3)).result);

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
    public void testOrder682() {
        RadioactiveSearcher s;
        Experiment[] origin;
        Experiment p1, p2;

        s = new RadioactiveSearcher(6, 8, 2);

        origin = new Experiment[] {s.findSpaceExperimentByCombination(new BitSet(0,1)),
                s.findSpaceExperimentByCombination(new BitSet(0,2))};   //0,1->1111111111111000000000000000, 0,2->1111111100000111110000000000

        p1 = s.findSpaceExperimentByCombination(new BitSet(0,2));     //0,2->1111111100000111110000000000
        p2 = s.findSpaceExperimentByCombination(new BitSet(3,5));     //3,5->0010100010100101001111100110

        System.out.println(Arrays.asList(origin));
        System.out.println(p1);
        System.out.println(p2);

        assertTrue(s.compareExperiments(origin, p1, p2) < 0);
    }

    @Test
    public void testOrder() {
        RadioactiveSearcher s;

        Experiment[] origin;
        Experiment p1, p2;

        s = new RadioactiveSearcher(3, 8, 1);

        origin = new Experiment[] {s.findSpaceExperimentByCombination(new BitSet(0,1,2,3))};    //0,1,2,3->11110000
        p1 = s.findSpaceExperimentByCombination(new BitSet(0,1,4,5));     //0,1,4,5->11001100
        p2 = s.findSpaceExperimentByCombination(new BitSet(0,1,2,4));     //0,1,2,4->11101000

        System.out.println(Arrays.asList(origin));
        System.out.println(p1);
        System.out.println(p2);

        assertEquals(1, s.compareExperiments(origin, p1, p2));

        origin = new Experiment[] {s.findSpaceExperimentByCombination(new BitSet(0,1,2,3)),
                s.findSpaceExperimentByCombination(new BitSet(0,1,4,5))};    //0,1,2,3->11110000 0,1,4,5->11001100
        p1 = s.findSpaceExperimentByCombination(new BitSet(0,2,4,6));     //0,2,4,6->10101010
        p2 = s.findSpaceExperimentByCombination(new BitSet(0,1,6,7));     //0,1,6,7->11000011

        System.out.println(Arrays.asList(origin));
        System.out.println(p1);
        System.out.println(p2);

        assertEquals(4, s.compareExperiments(origin, p1, p2));
    }

    @Test
    public void testBuildSearchSpace() {
        RadioactiveSearcher s;
        s = new RadioactiveSearcher(3, 8, 1);
        assertEquals(BitSet.c(8,4), s.getSearchSpace().length);

        s = new RadioactiveSearcher(5, 8, 2);
        assertEquals(BitSet.c(8,2), s.getSearchSpace().length);
    }

    @Test
    public void testIsSufficient() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        Experiment testResults = s.runExperiment(BitSet.buildSet(3));

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

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).result);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(0, 2)).result);

        assertEquals(true, s.canPerformNextAttempt(1, experimentTable));
    }

    @Test
    public void testCanNotProceed() {
        RadioactiveSearcher s = new RadioactiveSearcher(2, 4, 1);
        int[] experimentTable = s.buildExperimentTable();

        s.recordResult(0, experimentTable, s.runExperiment(new BitSet(0, 1)).result);
        s.recordResult(1, experimentTable, s.runExperiment(new BitSet(2, 3)).result);

        assertEquals(false, s.canPerformNextAttempt(1, experimentTable));
    }

    @Test
    public void search3_8_1() {
        RadioactiveSearcher s = new RadioactiveSearcher(3, 8, 1);
        s.search();
    }

    @Test
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
