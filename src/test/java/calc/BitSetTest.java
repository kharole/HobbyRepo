package calc;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: oleg
 * Date: 07.10.11 13:33
 */
public class BitSetTest {

    // comment
    @Test
    public void testToString() {
        BitSet bitSet = new BitSet(new int[] {0, 1, 7, 28, 29});
        Assert.assertEquals("0,1,7,28,29", bitSet.toString());

        Assert.assertEquals("", new BitSet().toString());

    }

    @Test
    public void testJoin() {
        BitSet bitSet1 = new BitSet(new int[] {0, 1, 7});
        BitSet bitSet2 = new BitSet(new int[] {1, 7, 28, 29});
        Assert.assertEquals(new BitSet(new int[] {28, 29, 7, 0, 1}), bitSet1.join(bitSet2));
    }

    @Test
    public void testToSingleElementSets() {
        BitSet bitSet = new BitSet(new int[] {0, 1, 7});
        BitSet[] singleElementsBitSets = bitSet.toSingleElementSets();
        Assert.assertEquals(3, singleElementsBitSets.length);
        Assert.assertEquals(new BitSet(new int[] {0}), singleElementsBitSets[0]);
        Assert.assertEquals(new BitSet(new int[] {1}), singleElementsBitSets[1]);
        Assert.assertEquals(new BitSet(new int[]{7}), singleElementsBitSets[2]);
    }

    @Test
    public void testCombination2Iterator() {
        BitSet bitSet = new BitSet(new int[] {1,2,3,4,5});
        List<BitSet> combinations = new ArrayList<BitSet>();
        for(BitSet combination: bitSet.combinations(2)) {
            combinations.add(combination);
        }

        Assert.assertEquals(combinations.size(), bitSet.combinationsCount(2));

        Assert.assertTrue(combinations.contains(new BitSet(new int[] {1,2})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {1,3})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {1,4})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {1,5})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {2,3})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {2,4})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {2,5})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {3,4})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {3,5})));
        Assert.assertTrue(combinations.contains(new BitSet(new int[] {4,5})));
    }


    @Test
    public void testCombinationCount() {
        int n = 15;
        BitSet s = BitSet.buildSet(n);
        Assert.assertEquals(s.combinationsCount(10), 3003);
    }

    @Test
    public void testCombinationCount2toN() {
        int n = 15;
        int total = 0;
        BitSet s = BitSet.buildSet(n);

        for(int i=0; i<n; i++) {
            int c = s.combinationsCount(i);
            System.out.println(i + " " + c);
            total += c;
        }

        Assert.assertEquals((1<<n)-1, total);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombinationIteratorException() {
        BitSet bitSet = new BitSet(new int[] {1,2,3,4,5});
        bitSet.combinations(6).iterator();
    }

    @Test
    public void testCombinationNNIterator() {
        BitSet bitSet = new BitSet(new int[] {1,2,3});
        List<BitSet> combinations = new ArrayList<BitSet>();
        for(BitSet combination: bitSet.combinations(3)) {
            combinations.add(combination);
        }

        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(new BitSet(new int[] {1,2,3}), combinations.get(0));
    }

    @Test
    public void testCombinationZeroIterator() {
        BitSet bitSet = new BitSet(new int[] {1,2,3});
        List<BitSet> combinations = new ArrayList<BitSet>();
        for(BitSet combination: bitSet.combinations(0)) {
            combinations.add(combination);
        }

        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(new BitSet(new int[] {}), combinations.get(0));
    }

    @Test
    public void testCombinationZeroZeroIterator() {
        BitSet bitSet = new BitSet(new int[] {});
        List<BitSet> combinations = new ArrayList<BitSet>();
        for(BitSet combination: bitSet.combinations(0)) {
            combinations.add(combination);
        }

        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(new BitSet(new int[] {}), combinations.get(0));
    }


}
