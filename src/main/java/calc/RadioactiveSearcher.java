package calc;

import java.util.*;

/**
 * Author: oleg
 * Date: 03.10.11 9:14
 */
public class RadioactiveSearcher {

    private int attemptsCount;
    private int ballsCount;

    public int getAttemptsCount() {
        return attemptsCount;
    }

    public int getRadiocativeCount() {
        return radiocativeCount;
    }

    public int getBallsCount() {
        return ballsCount;
    }

    public BitSet getBalls() {
        return balls;
    }

    public BitSet[] getRadioactiveCombinations() {
        return radioactiveCombinations;
    }

    public BitSet[] getAllTestCombinations() {
        return allTestCombinations;
    }

    private int radiocativeCount;

    private BitSet balls;
    private BitSet[] radioactiveCombinations;


    private int[] testTable;
    private BitSet[] testCombinations;
    private BitSet[] allTestCombinations;

    public RadioactiveSearcher(int attemptsCount, int ballsCount, int radiocativeCount) {
        this.attemptsCount = attemptsCount;
        this.ballsCount = ballsCount;
        this.radiocativeCount = radiocativeCount;

        balls = BitSet.buildSet(ballsCount);
        radioactiveCombinations = buildRadioactiveCombinations();
        testTable = new int[radioactiveCombinations.length];
        allTestCombinations = buildAllTestCombination(balls);
        testCombinations = new BitSet[attemptsCount];
    }

    public static BitSet[] buildAllTestCombination(BitSet balls) {
        BitSet[] result = new BitSet[twoToThe(balls.size())];
        int i = 0;
        for (int l = 0; l < balls.size(); l++) {
            for (BitSet testCombination : balls.combinations(l)) {
                result[i++] = testCombination;
            }
        }
        return result;
    }

    protected static boolean isSufficient(int ballsCount, int radiocativeCount, int testCombinationSize) {
        return false;
    }

    private static int twoToThe(int n) {
        return 1 << n;
    }

    protected BitSet[] buildRadioactiveCombinations() {
        List<BitSet> result = new ArrayList<BitSet>();
        for (BitSet radioactiveCombination : balls.combinations(radiocativeCount)) {
            result.add(radioactiveCombination);
        }
        return result.toArray(new BitSet[]{});
    }

    private void search(int attempt) throws RuntimeException {
        //System.out.println(attempt);
        if (attempt == attemptsCount) {
            throw new RuntimeException("Eurika");
        }

        int total = twoToThe(ballsCount) - 1;

        int i = 0;
        for (BitSet testCombination : allTestCombinations) {
            if (attempt == 0)
                System.out.println("progres:" + i * 100 / total + " %");
            i++;
            testCombinations[attempt] = testCombination;
            testAndRecord(testCombination, attempt);
            if (testCombination.size() == 0 && !canProceedWithCurrentAmountOfBall(attempt)) {
                break;
            }
            if (canPerformNextAttempt(attempt)) {
                search(attempt + 1);
            }
        }
    }

    protected boolean canProceedWithCurrentAmountOfBall(int attempt) {
        Map<Integer, Integer> outcomeCountMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < testTable.length; i++) {
            int key = testTable[i] & 1 << attempt;
            if (!outcomeCountMap.containsKey(key)) {
                outcomeCountMap.put(key, 0);
            }
            outcomeCountMap.put(key, outcomeCountMap.get(key) + 1);
        }

        for (int count : outcomeCountMap.values()) {
            if (count > (1 << (attemptsCount - 1)))
                return false;
        }

        return true;
    }

    protected boolean canPerformNextAttempt(int attempt) {
        Map<Integer, Integer> outcomeCountMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < testTable.length; i++) {
            int key = testTable[i] & mask(attempt + 1);
            if (!outcomeCountMap.containsKey(key)) {
                outcomeCountMap.put(key, 0);
            }
            outcomeCountMap.put(key, outcomeCountMap.get(key) + 1);
        }

        for (int count : outcomeCountMap.values()) {
            if (count > (1 << (attemptsCount - 1 - attempt)))
                return false;
        }

        return true;
    }

    protected int mask(int size) {
        int result = 0;
        for (int i = 0; i < size; i++) {
            result |= 1 << i;
        }
        return result;
    }

    public static class ExperimentResult {

        public boolean[] items;
        public int trueCount;
        public int falseCount;

        public ExperimentResult(int size) {
            this.items = new boolean[size];
        }
    }

    protected ExperimentResult test(BitSet testCombination) {
        ExperimentResult result = new ExperimentResult(radioactiveCombinations.length);
        for (int i = 0; i < radioactiveCombinations.length; i++) {
            result.items[i] = testCombination.hasIntersection(radioactiveCombinations[i]);
            if(result.items[i])
                result.trueCount++;
            else
                result.falseCount++;
        }
        return result;
    }

    protected void testAndRecord(BitSet testCombination, int attempt) {
        boolean[] testResult = test(testCombination).items;
        for (int i = 0; i < testResult.length; i++) {
            if (testResult[i]) {
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
