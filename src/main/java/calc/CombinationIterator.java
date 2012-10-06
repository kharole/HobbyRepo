package calc;

import java.util.Iterator;

/**
 * Author: oleg
 * Date: 11.10.11 10:05
 */
public class CombinationIterator implements Iterator<BitSet> {

    private int[] indices;
    private BitSet[] singleElementBitSets;
    private boolean hasNext = true;

    public CombinationIterator(BitSet bitSet, int k) throws IllegalArgumentException {
        this.indices = new int[k];
        for(int i=0; i<k; i++)
            indices[i] = k-1-i;
        this.singleElementBitSets = bitSet.toSingleElementSets();
        if(k>this.singleElementBitSets.length)
            throw new IllegalArgumentException("Impossible to select " + k + " elements from bitSet of size " + singleElementBitSets.length);
    }

    public boolean hasNext() {
        return hasNext;
    }

    private int inc(int[] indices, int maxIndex, int depth) throws IllegalStateException {
        if(depth == indices.length) {
            throw new IllegalStateException("The End");
        }
        if(indices[depth] < maxIndex) {
            indices[depth] = indices[depth]+1;
        } else {
            indices[depth] = inc(indices, maxIndex-1, depth+1)+1;
        }
        return indices[depth];
    }

    private boolean inc() {
        try {
            inc(indices, singleElementBitSets.length - 1, 0);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public BitSet next() {
        BitSet result = new BitSet();
        for(int index: indices) {
            result = result.join(singleElementBitSets[index]);
        }
        hasNext = inc();
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
