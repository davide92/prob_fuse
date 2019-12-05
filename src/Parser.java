import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Parser {
    //This method is used to import the run in the system, the method returns an ArrayList where every Item
    //represents a row in a run.
    static ArrayList<Item> parse(String path, String runID) {
        ArrayList<Item> list = new ArrayList<>();
        File file = new File(path);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (input.hasNextLine()) {
            String[] temp = input.nextLine().split(" ");
            list.add(new Item(Integer.parseInt(temp[0]), temp[2], Integer.parseInt(temp[3]), Double.parseDouble(temp[4]), runID));
        }
        return list;
    }

    //This method is used to import the qrels in the system, the path String indicates the location where
    //it is saved the qrels in the computer. The method returns an HashMap where the key is the id of the query,
    //and the value is an instance of the class QrelsTopic. The QrelsTopic groups all the relevance judgments
    //about of a specific topic.
    static HashMap<Integer, QrelsTopic> parseQrels(String path) {
        HashMap<Integer, QrelsTopic> retMap = new HashMap<>();
        File file = new File(path);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (input.hasNextLine()) {
            String[] temp = input.nextLine().split(" ");
            if (retMap.containsKey(Integer.parseInt(temp[0]))) {
                retMap.get(Integer.parseInt(temp[0])).put(temp[2], Integer.parseInt(temp[3]));
            } else {
                retMap.put(Integer.parseInt(temp[0]), new QrelsTopic(temp[2], Integer.parseInt(temp[3])));
            }
        }
        return retMap;
    }

}
