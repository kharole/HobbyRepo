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
    private ExperimentResult[] searchSpace;

    public RadioactiveSearcher(int attemptsCount, int ballsCount, int radiocativeCount) {

        if(BitSet.c(ballsCount, radiocativeCount) > twoToThe(attemptsCount))
            throw new IllegalArgumentException("Task is not resolvable");

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

    protected int[] buildExperimentTable() {
        return new int[radioactiveCombinations.length];
    }

    public ExperimentResult[] search() {
        int[] experimentTable = buildExperimentTable();
        ExperimentResult[] experimentResults = new ExperimentResult[attemptsCount];

        try {
            search(0, experimentResults, experimentTable);
            System.out.println("No result");
            return null;
        } catch (RuntimeException e) {
            BitSet[] experimentCombinations = new BitSet[experimentResults.length];
            for(int i=0; i<experimentResults.length; i++) {
                experimentCombinations[i] = experimentResults[i].combination;
            }
            printExperimentTable(experimentCombinations);
            return experimentResults;
        }
    }

    private void search(int attempt, ExperimentResult[] experimentResults, int[] experimentTable) throws RuntimeException {
        if (attempt == attemptsCount) {
            throw new RuntimeException("Eureka");
        }

        for (ExperimentResult experimentResult  : searchSpace) {

            experimentResults[attempt] = experimentResult;
            recordResult(attempt, experimentTable, experimentResult.items);

            if (canPerformNextAttempt(attempt, experimentTable)) {
                search(attempt + 1, experimentResults, experimentTable);
            }

        }
    }

    protected ExperimentResult[] sortSearchSpace(ExperimentResult[] experimentResults, int attempt) {
        final ExperimentResult[] base = Arrays.copyOf(experimentResults, attempt);
        ExperimentResult[] result = Arrays.copyOf(searchSpace, searchSpace.length);
        Arrays.sort(result, new Comparator<ExperimentResult>() {

            @Override
            public int compare(ExperimentResult r1, ExperimentResult r2) {
                return distance(r1) - distance(r2);
            }

            public int distance(ExperimentResult r) {
                return 0;
            }

        });
        return result;
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
        public BitSet combination;

        public ExperimentResult(int size, BitSet combination) {
            this.items = new boolean[size];
            this.combination = combination;
        }
    }

    protected ExperimentResult runExperiment(BitSet combination) {
        ExperimentResult result = new ExperimentResult(radioactiveCombinations.length, combination);
        for (int i = 0; i < radioactiveCombinations.length; i++) {
            result.items[i] = combination.hasIntersection(radioactiveCombinations[i]);
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

    protected ExperimentResult[] buildSearchSpace() {
        List<ExperimentResult> result = new ArrayList<ExperimentResult>();
        int i = 0;
        for (int l = 0; l < balls.size(); l++) {
            if(!isSufficient(l)) {
                System.out.println("size " + l + " skipped");
                continue;
            }
            System.out.println("size " + l + " accepted");
            for (BitSet combination : balls.combinations(l)) {
                ExperimentResult r = runExperiment(combination);
                result.add(r);
                //System.out.println(r.combination + " " + Arrays.toString(r.items));
            }
        }
        return result.toArray(new ExperimentResult[] {});
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
        System.out.println("falseCount=" + falseCount + " trueCount=" + trueCount + " threshold=" + threshold);
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

    public ExperimentResult[]  getSearchSpace() {
        return searchSpace;
    }

}
