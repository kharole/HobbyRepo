package calc;

/**
* Created with IntelliJ IDEA.
* User: Admin
* Date: 14.10.12
* Time: 10:28
* To change this template use File | Settings | File Templates.
*/
public class Experiment {

    public boolean[] result;
    public int trueCount;
    public int falseCount;
    public BitSet combination;

    public Experiment(int size, BitSet combination) {
        this.result = new boolean[size];
        this.combination = combination;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(result.length);
        for(int i=0; i< result.length; i++)
            sb.append(result[i] ? "1" : "0");
        return combination + "->" + sb;
    }


}
