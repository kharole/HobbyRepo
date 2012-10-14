package calc;

import java.util.*;

import static java.lang.Math.abs;

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
    private Experiment[] searchSpace;

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

    public Experiment[] search() {
        int[] experimentTable = buildExperimentTable();
        Experiment[] experiments = new Experiment[attemptsCount];

        try {
            search(0, experiments, experimentTable);
            System.out.println("No result");
            return null;
        } catch (RuntimeException e) {
            BitSet[] experimentCombinations = new BitSet[experiments.length];
            for(int i=0; i< experiments.length; i++) {
                experimentCombinations[i] = experiments[i].combination;
            }
            printExperimentTable(experimentCombinations);
            return experiments;
        }
    }

    private void search(int attempt, Experiment[] experiments, int[] experimentTable) throws RuntimeException {
        System.out.println(attempt);

        if (attempt == attemptsCount) {
            throw new RuntimeException("Eureka");
        }

        for (Experiment experiment : sortSearchSpace(experiments, attempt)) {
            experiments[attempt] = experiment;
            recordResult(attempt, experimentTable, experiment.result);

            if (canPerformNextAttempt(attempt, experimentTable)) {
                search(attempt + 1, experiments, experimentTable);
            }
        }
    }

    protected Experiment[] sortSearchSpace(Experiment[] experiments, int attempt) {
        final Experiment[] origins = Arrays.copyOf(experiments, attempt);
        Experiment[] result = Arrays.copyOf(searchSpace, searchSpace.length);
        Arrays.sort(result, new Comparator<Experiment>() {

            @Override
            public int compare(Experiment r1, Experiment r2) {
                return -compareExperiments(origins, r1, r2);
            }
        });

        return Arrays.copyOf(result, 50);
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

    protected int[] order(Experiment[] origin, Experiment point) {
        Map<String, Integer> outcomeCountMap = new TreeMap<String, Integer>();
        int[] result = new int[2];

        for (int i = 0; i < radioactiveCombinations.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.setLength(origin.length + 1);
            for (int j = 0; j < origin.length; j++) {
                sb.setCharAt(j, origin[j].result[i] ? '1' : '0');
            }
            sb.setCharAt(origin.length, point.result[i] ? '1' : '0');

            String key = sb.toString();
            if (!outcomeCountMap.containsKey(key)) {
                outcomeCountMap.put(key, 0);
            }
            outcomeCountMap.put(key, outcomeCountMap.get(key) + 1);
        }

        result[0] = outcomeCountMap.keySet().size();

        result[1] = 0;
        for (int count : outcomeCountMap.values()) {
            result[1] += -count*count;
        }

        return result;
    }

    protected int compareExperiments(Experiment[] origin, Experiment p1, Experiment p2) {
        int[] o1 = order(origin, p1);
        int[] o2 = order(origin, p2);

        if(o1[0] != o2[0])
            return o1[0]-o2[0];
        else
            return o1[1]-o2[1];
    }

    protected Experiment runExperiment(BitSet combination) {
        Experiment result = new Experiment(radioactiveCombinations.length, combination);
        for (int i = 0; i < radioactiveCombinations.length; i++) {
            result.result[i] = combination.hasIntersection(radioactiveCombinations[i]);
            if(result.result[i])
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

    protected Experiment findSpaceExperimentByCombination(BitSet combination) {
        for(int i=0; i<searchSpace.length; i++) {
            if(searchSpace[i].combination.equals(combination)) {
                return searchSpace[i];
            }
        }
        throw new IllegalArgumentException("No such combination is space");
    }

    protected Experiment[] buildSearchSpace() {
        List<Experiment> result = new ArrayList<Experiment>();
        int i = 0;
        for (int l = 0; l < balls.size(); l++) {
            if(!isSufficient(l)) {
                System.out.println("size " + l + " skipped");
                continue;
            }
            System.out.println("size " + l + " accepted");
            for (BitSet combination : balls.combinations(l)) {
                Experiment r = runExperiment(combination);
                result.add(r);
                System.out.println(r);
            }
        }
        return result.toArray(new Experiment[] {});
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

    public Experiment[]  getSearchSpace() {
        return searchSpace;
    }

}
