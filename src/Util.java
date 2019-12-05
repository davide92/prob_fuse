import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

class Util {
    //compute the min and the max value for a specific run
    private static MinMax getMinMax(ArrayList<Item> tmp) {
        double max = 0;
        double min = Double.MAX_VALUE;
        //The for loop cycles in every row(item) of a run and return the min and the max value
        //of the relevance score
        for (Item aTmp : tmp) {
            if (aTmp.getScore() > max) {
                max = aTmp.getScore();
            }
            if (aTmp.getScore() < min) {
                min = aTmp.getScore();
            }
        }

        return new MinMax(min, max);
    }

    //Normalization of the run's elements
    public static ArrayList<Item> minMaxNormalize(ArrayList<Item> tmp) {
        //Obtains the min and the max value of the relevance score for a run
        MinMax minMax = getMinMax(tmp);
        for (Item item : tmp) {
            item.minMaxNorm(minMax.getMin(), minMax.getMax());
        }
        return tmp;
    }

    //Return an HashMap where the key is the number of the topics and the value is an instance of a class Topic
    static HashMap<Integer, Topic> groupRunByTopic(ArrayList<ArrayList<Item>> listRun) {
        //The input parameters is composed by an external ArrayList that represent the different run,
        //instead the internal ArrayList contain all the item in a Run.
        HashMap<Integer, Topic> topicMap = new HashMap<>();
        //The for loop cycles in every run
        for (ArrayList<Item> run : listRun) {
            //Every item of that run is processed
            for (Item item : run) {
                //if the topic is already contained in the HashMap
                if (topicMap.containsKey(item.getTopicID())) {
                    //new item is added to the existent topic
                    topicMap.get(item.getTopicID()).addItem(item);
                } else {
                    //if the topic is not contained in the HashMap, a new instance of class Topic is
                    //created and added to the HashMap
                    Topic newTopic = new Topic(item.getTopicID());
                    newTopic.addItem(item);
                    topicMap.put(item.getTopicID(), newTopic);
                }
            }
        }
        return topicMap;
    }

    //Return an HashMap where the key is the id number of the topic and the value is an instance of the class
    //ResQuery.
    static ArrayList<HashMap<Integer, ResQuery>> groupByTopic(ArrayList<ArrayList<Item>> listRun) {
        ArrayList<HashMap<Integer, ResQuery>> retVal = new ArrayList<>();
        int numRun = 0;
        for (ArrayList<Item> run : listRun) {
            HashMap<Integer, ResQuery> map = new HashMap<>();
            for (Item item : run) {
                //Topic is already in the HashMap
                if (map.containsKey(item.getTopicID())) {
                    // New item is add to the HashMap
                    map.get(item.getTopicID()).addRecord(new Record(item.getDocumentID(), item.getRank(), numRun));
                } else {
                    //Topic is not already in the HashMap
                    ResQuery newResQuery = new ResQuery(item.getTopicID());
                    newResQuery.addRecord(new Record(item.getDocumentID(), item.getRank(), numRun));
                    map.put(item.getTopicID(), newResQuery);
                }
            }
            retVal.add(map);
            numRun++;
        }
        return retVal;

    }

    //This method take an ordered list of item and a filename that indicates the location where
    //save the output file.
    static void writeOutput(ArrayList<Item> list, String filename) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Item item : list) {
            printWriter.println(item.toTrecFormat());
        }
        System.out.println("*** Writing completed **** " + filename);
        printWriter.close();
    }

    //This method with the parameter that located the directory obtains the name of the file
    //contained in the directory
    static ArrayList<String> getFileList(String args) {
        ArrayList<String> list = new ArrayList<>();
        File folder = new File(args);
        File[] listOfFiles = folder.listFiles();
        System.out.println("List of file to process");
        System.out.println();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getName());
                list.add(listOfFile.getAbsolutePath());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory " + listOfFile.getName());
            }

        }
        return list;
    }


}
