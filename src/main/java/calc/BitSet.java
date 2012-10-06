package calc;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Author: oleg
 * Date: 07.10.11 13:22
 */
public class BitSet {

    private static final int SET_SIZE = 32;
    private int bitSet;

    public BitSet(){
        this(0);
    }

    public BitSet(int... elements){
        bitSet = 0;
        for(int i=0; i<elements.length; i++) {
            bitSet |= 1 << elements[i];
        }
    }

    private BitSet(int bitSet){
        this.bitSet = bitSet;
    }

    public BitSet join(BitSet bitSet) {
        return new BitSet(this.bitSet | bitSet.bitSet);
    }

    public BitSet intersect(BitSet bitSet) {
        return new BitSet(this.bitSet & bitSet.bitSet);
    }

    public boolean hasIntersection(BitSet bitSet) {
        return intersect(bitSet).bitSet != 0;
    }

    public BitSet[] toSingleElementSets() {
        int[] elements = toIntArray();
        BitSet[] result = new BitSet[elements.length];
        for(int i=0; i<elements.length; i++) {
            result[i] = new BitSet(1<<elements[i]);
        }
        return result;
    }

    public int[] toIntArray() {
        int[] result = new int[SET_SIZE];
        int j = 0;
        int s = 1;
        for(int i=0; i<SET_SIZE; i++) {
            if((bitSet & s) != 0) {
                result[j++] = i;
            }
            s = s<<1;
        }
        return Arrays.copyOf(result, j);
    };

    public int size() {
        return toIntArray().length;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(200);
        for(int i: toIntArray()) {
            result.append(i);
            result.append(",");
        }
        if(result.length() > 0)
            result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    public Iterable<BitSet> combinations(final int k) {
        return new Iterable<BitSet>() {
            public Iterator<BitSet> iterator() {
                return new CombinationIterator(BitSet.this, k);
            }
        };
    }

    public int combinationsCount(int k) {
        int numerator = 1;
        int n = size();
        k = min(k, n-k);

        for(int i=n - k + 1; i<=size(); i++) {
            numerator = numerator*i;
        }
        int denominator = 1;
        for(int i=1; i<=k; i++) {
            denominator = denominator*i;
        }
        return numerator/denominator;
    }

    private int min(int v1, int v2) {
        if(v1 < v2)
            return v1;
        else
            return v2;
    }

    public static BitSet buildSet(int size) {
        int[] elements = new int[size];
        for(int i=0; i<size; i++) {
            elements[i] = i;
        }
        BitSet result = new BitSet(elements);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitSet bitSet = (BitSet) o;

        if (this.bitSet != bitSet.bitSet) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bitSet;
    }
}
