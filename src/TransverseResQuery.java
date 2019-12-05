/*This class is used to collect all the record related to a specific document's id, in this way is group
all the occurrences in the different run of a retrieved document related to a query
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class TransverseResQuery {
    private final HashMap<String, ArrayList<Record>> documents;
    private final int topicId;

    //This method is built in order to permit a fast access to the informations
    //that we need to calculate the score of the final run with S_d formula.

    public TransverseResQuery(int topicId) {
        documents = new HashMap<>();
        this.topicId = topicId;
    }

    public void put(Record record) {
        if (documents.containsKey(record.getIdRecord())) {
            documents.get(record.getIdRecord()).add(record);
        } else {
            ArrayList<Record> newArrayList = new ArrayList<>();
            newArrayList.add(record);
            documents.put(record.getIdRecord(), newArrayList);
        }
    }

    //this method computes the score of S_d
    public ArrayList<Item> computeResult(double[][] probTable) {
        ArrayList<Item> res = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Record>> entry : documents.entrySet()) {
            double somma = 0;
            //for each record
            for (Record record : entry.getValue()) {
                int segment=record.getSegmentIndex();
                //compute the p_k value for the S_d forumla
                double p_k = probTable[segment-1][record.getRun()] / (segment);
                somma = somma + p_k;
             }
            res.add(new Item(topicId, entry.getKey(), 0, somma, "probefuse"));
        }
        //sort of scores
        res.sort(new ScoreComparator());
        //add the rank and cut to the first 1000 results
        ArrayList<Item> retList=new ArrayList<>();
        for(int rank=0;rank<1000&&rank<res.size();rank++){
            Item item=res.get(rank);
            item.setRank(rank);
            retList.add(item);
        }

        return retList;

    }

    //in order to sort scores
    private class ScoreComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getScore() < o2.getScore()) return 1;
            else if (o1.getScore() > o2.getScore()) return -1;
            return 0;
        }
    }

}
