import java.util.ArrayList;
import java.util.HashMap;

class ComputeRankFusion {


    public static void main(String[] args) {
        String pathInput = "";
        String pathOutput = "";
        ArrayList<ArrayList<Item>> listRun = new ArrayList<>();
        //Get the name of the files which are present in the directory pathInput
        ArrayList<String> filePath = Util.getFileList(pathInput);
        int i = 0;
        for (String path : filePath) {
            //normalization of the relevance score
            listRun.add(Util.minMaxNormalize(Parser.parse(path, Integer.toString(i))));
            i++;
        }

        //Group the Item (row of run) by the topic
        HashMap<Integer, Topic> map = Util.groupRunByTopic(listRun);

        //Execution of the CombFusion method and following printing of the result
        ArrayList<Item> runCombSum = RankFusion.combFusion(map, "CombSum");

        Util.writeOutput(runCombSum, pathOutput + "combsum.txt");

        ArrayList<Item> runCombMnz = RankFusion.combFusion(map, "CombMnz");
        Util.writeOutput(runCombMnz, pathOutput + "combmnz.txt");

        ArrayList<Item> runCombMin = RankFusion.combFusion(map, "CombMin");
        Util.writeOutput(runCombMin, pathOutput + "combmin.txt");

        ArrayList<Item> runCombMax = RankFusion.combFusion(map, "CombMax");
        Util.writeOutput(runCombMax, pathOutput + "combmax.txt");

        ArrayList<Item> runCombAnz = RankFusion.combFusion(map, "CombAnz");
        Util.writeOutput(runCombAnz, pathOutput + "combanz.txt");

        ArrayList<Item> runCombMed = RankFusion.combFusion(map, "CombMed");
        Util.writeOutput(runCombMed, pathOutput + "combmed.txt");

    }


}