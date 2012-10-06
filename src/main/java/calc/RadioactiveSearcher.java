package calc;

import java.util.*;

/**
 * Author: oleg
 * Date: 03.10.11 9:14
 */
public class RadioactiveSearcher {

    private int attemptsCount;
    private int ballsCount;
    private int radiocativeCount;

    private BitSet balls;
    private BitSet[] radioactiveCombinations;


    private int[] testTable;
    private BitSet[] testCombinations;

    public RadioactiveSearcher(int attemptsCount, int ballsCount, int radiocativeCount) {
        this.attemptsCount = attemptsCount;
        this.ballsCount = ballsCount;
        this.radiocativeCount = radiocativeCount;

        balls = BitSet.buildSet(ballsCount);
        radioactiveCombinations = buildRadioactiveCombinations();
        testTable = new int[radioactiveCombinations.length];
        testCombinations = new BitSet[attemptsCount];
    }

    protected BitSet[] buildRadioactiveCombinations() {
        List<BitSet> result = new ArrayList<BitSet>();
        for(BitSet radioactiveCombination: balls.combinations(radiocativeCount)) {
            result.add(radioactiveCombination);
        }
        return result.toArray(new BitSet[] {});
    }

    private void search(int attempt) throws RuntimeException {
        //System.out.println(attempt);
        if(attempt == attemptsCount) {
            throw new RuntimeException("Eurika");
        }

        int total = (1<<ballsCount)-1;

        int i=0;
        for(int l=0; l<ballsCount; l++) {
            for(BitSet testCombination: balls.combinations(l)) {
                if(attempt == 0)
                    System.out.println("progres:" + i*100/total + " %");
                i++;
                //System.out.println(attempt + ", " + l + "," + testCombination.toString());
                testCombinations[attempt] = testCombination;
                test(testCombination, attempt);
                if(l==0 && !canProceedWithCurrentAmountOfBall(attempt)) {
                    //System.out.println("break:" + attempt + ", " + l);
                    break;
                }
                if(canPerformNextAttempt(attempt)) {
                    search(attempt+1);
                }
            }
        }
    }

    protected boolean canProceedWithCurrentAmountOfBall(int attempt) {
        Map<Integer, Integer> outcomeCountMap = new HashMap<Integer, Integer>();
        for(int i=0; i<testTable.length; i++) {
            int key = testTable[i] & 1 << attempt;
            if(!outcomeCountMap.containsKey(key)) {
                outcomeCountMap.put(key, 0);
            }
            outcomeCountMap.put(key, outcomeCountMap.get(key) + 1);
        }

        for(int count: outcomeCountMap.values()) {
            if(count > (1 << (attemptsCount - 1)))
                return false;
        }

        return true;
    }

    protected boolean canPerformNextAttempt(int attempt) {
        Map<Integer, Integer> outcomeCountMap = new HashMap<Integer, Integer>();
        for(int i=0; i<testTable.length; i++) {
            int key = testTable[i] & mask(attempt + 1);
            if(!outcomeCountMap.containsKey(key)) {
                outcomeCountMap.put(key, 0);
            }
            outcomeCountMap.put(key, outcomeCountMap.get(key) + 1);
        }

        for(int count: outcomeCountMap.values()) {
            if(count > (1 << (attemptsCount - 1 - attempt)))
                return false;
        }

        return true;
    }

    protected int mask(int size) {
        int result = 0;
        for(int i=0; i<size; i++) {
            result |= 1 << i;
        }
        return result;
    }

    protected void test(BitSet testCombination, int attempt) {
        for(int i=0; i<radioactiveCombinations.length; i++) {
            if(testCombination.hasIntersection(radioactiveCombinations[i])) {
                testTable[i] = testTable[i] | (1 << attempt);
            } else {
                testTable[i] = testTable[i] & ~(1 << attempt);
            }
        }
    }

    protected void printTestTable() {
        System.out.println(Arrays.toString(getTestCombinations()));
    }

    public void search() {
        try {
            search(0);
            System.out.println("No result");
        } catch (RuntimeException e) {
            printTestTable();
        }
    }


    public int[] getTestTable() {
        return testTable;
    }

    public void setTestTable(int[] testTable) {
        this.testTable = testTable;
    }

    public BitSet[] getTestCombinations() {
        return testCombinations;
    }

    public void setTestCombinations(BitSet[] testCombinations) {
        this.testCombinations = testCombinations;
    }
}
