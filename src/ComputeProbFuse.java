import java.util.ArrayList;
import java.util.HashMap;

class ComputeProbFuse {

    //main class

    private static final String pathRun = "";
    private static final String pathQrels = "";
    private static final String pathOutput = "";

    public static void main(String[] args) {


        ArrayList<ArrayList<Item>> listRun = new ArrayList<>();
        ArrayList<String> filePath = Util.getFileList(pathRun);

        int i = 0;
        //Population of the list of runs
        for (String path : filePath) {
            listRun.add(Util.minMaxNormalize(Parser.parse(path, Integer.toString(i))));
            i++;
        }
        ArrayList<HashMap<Integer, ResQuery>> temp = Util.groupByTopic(listRun);
        HashMap<Integer, QrelsTopic> qrels = Parser.parseQrels(pathQrels);
        ProbFuse probFuse = new ProbFuse(qrels, temp, 50,30);
        probFuse.compute(pathOutput,true);

    }
}
