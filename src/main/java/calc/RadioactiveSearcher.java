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
    private BitSet[] searchSpace;

    public RadioactiveSearcher(int attemptsCount, int ballsCount, int radiocativeCount) {
        this.attemptsCount = attemptsCount;
        this.ballsCount = ballsCount;
        this.radiocativeCount = radiocativeCount;

        balls = BitSet.buildSet(ballsCount);
        System.out.println("Built set of all balls: " + balls);

        radioactiveCombinations = buildRadioactiveCombinations();
        System.out.println("Built list of radioactive combinations of size " + radioactiveCombinations.length);

        searchSpace = buildSearchSpace();
        System.out.println("Built list of all valid experiments of size " + searchSpace.length);
    }

    protected void printExperimentTable(BitSet[] experimentCombinations) {
        System.out.println(Arrays.toString(experimentCombinations));
    }

    public void search() {
        int[] experimentTable = buildExperimentTable();
        BitSet[] experimentCombinations = new BitSet[attemptsCount];

        try {
            search(0, experimentTable, experimentCombinations);
            System.out.println("No result");
        } catch (RuntimeException e) {
            printExperimentTable(experimentCombinations);
        }
    }

    protected int[] buildExperimentTable() {
        return new int[radioactiveCombinations.length];
    }

    private void search(int attempt, int[] experimentTable, BitSet[] experimentCombinations) throws RuntimeException {
        if (attempt == attemptsCount) {
            throw new RuntimeException("Eureka");
        }

        int total = searchSpace.length;

        int i = 0;
        for (BitSet testCombination : searchSpace) {
            if (attempt == 0)
                System.out.println("progress:" + i * 100 / total + " %");
            i++;
            experimentCombinations[attempt] = testCombination;
            ExperimentResult experimentResult = runExperiment(testCombination);
            recordResult(attempt, experimentTable, experimentResult.items);
            if (canPerformNextAttempt(attempt, experimentTable)) {
                search(attempt + 1, experimentTable, experimentCombinations);
            }
        }
    }

    protected boolean canPerformNextAttempt(int attempt, int[] experimentTable) {
        Map<Integer, Integer> outcomeCountMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < experimentTable.length; i++) {
            int key = experimentTable[i] & mask(attempt + 1);
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

    protected ExperimentResult runExperiment(BitSet testCombination) {
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

    protected static void recordResult(int attempt, int[] experimentTable, boolean[] experimentResult) {
        for (int i = 0; i < experimentResult.length; i++) {
            if (experimentResult[i]) {
                experimentTable[i] = experimentTable[i] | (1 << attempt);
            } else {
                experimentTable[i] = experimentTable[i] & ~(1 << attempt);
            }
        }
    }

    protected BitSet[] buildSearchSpace() {
        List<BitSet> result = new ArrayList<BitSet>();
        int i = 0;
        for (int l = 0; l < balls.size(); l++) {
            if(!isSufficient(l))
                continue;
            for (BitSet experimentCombination : balls.combinations(l)) {
                result.add(experimentCombination);
            }
        }
        return result.toArray(new BitSet[] {});
    }

    protected int[] experimentCounts(int experimentCombinationSize) {
        int[] result = new int[2];
        result[0] = BitSet.c(ballsCount - experimentCombinationSize, radiocativeCount);
        result[1] = BitSet.c(ballsCount, radiocativeCount) - result[0];
        return result;
    }

    protected boolean isSufficient(int experimentCombinationSize) {
        int falseCount = experimentCounts(experimentCombinationSize)[0];
        int trueCount = experimentCounts(experimentCombinationSize)[1];
        int threshold = twoToThe(attemptsCount - 1);
        return falseCount <= threshold && trueCount <= threshold;
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

    public BitSet[] getSearchSpace() {
        return searchSpace;
    }

}
