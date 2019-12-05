

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

class RankFusion {
    //This method executes the RankFusion Comb method
    public static ArrayList<Item> combFusion(HashMap<Integer, Topic> tmp, String type) {
        ArrayList<ArrayList<Item>> retval = new ArrayList<>();
        //the for loop cycles in every topicID and for topic added the documents to the HashMap item
        //topic, in this way is build an transverse visual of the element, because the key is the document's
        //id and the value is an istance of MyDocument, that collects all the occurrences related with that
        //document's id.
        for (Integer topicId : tmp.keySet()) {
            Topic topic = tmp.get(topicId);
            HashMap<String, MyDocument> itemTopic = topic.getItems();
            Iterator<String> documentIterator = itemTopic.keySet().iterator();
            ArrayList<Item> temporanea = new ArrayList<>();
            while (documentIterator.hasNext()) {
                String documentId = documentIterator.next();
                MyDocument document = itemTopic.get(documentId);
                switch (type) {
                    case "CombSum":
                        temporanea.add(document.getCombSumItem());
                        break;
                    case "CombMnz":
                        temporanea.add(document.getCombMnzItem());
                        break;
                    case "CombMin":
                        temporanea.add(document.getCombMinItem());
                        break;
                    case "CombMax":
                        temporanea.add(document.getCombMaxItem());
                        break;
                    case "CombAnz":
                        temporanea.add(document.getCombAnzItem());
                        break;
                    case "CombMed":
                        temporanea.add(document.getCombMedItem());
                        break;
                }

            }
            retval.add(temporanea);

        }
        retval.sort(new TopicComparator());
        //in this way is order the Item by Topic and by score
        for (ArrayList<Item> toOrder : retval) {
            toOrder.sort(new ScoreComparator());

        }

        return mergeAndRanked(retval);

    }

    //This method set the rank for every Item in the run, and it limits the number of documents retrieved
    //as a response to query at 1000 elements

    private static ArrayList<Item> mergeAndRanked(ArrayList<ArrayList<Item>> listArrayList) {
        ArrayList<Item> retList = new ArrayList<>();
        //prendo solo i primi 1000 valori per ogni item
        for (ArrayList<Item> tempo : listArrayList) {
            int rank = 0;
            for (int j = 0; j < 1000 && j < tempo.size(); j++) {
                tempo.get(j).setRank(rank++);
                retList.add(tempo.get(j));
            }
        }
        //devono essere ritornati solo i primi 1000 risultati per ogni topic

        return retList;
    }

    private static class TopicComparator implements Comparator<ArrayList<Item>> {

        @Override
        public int compare(ArrayList<Item> o1, ArrayList<Item> o2) {
            return Integer.compare(o1.get(0).getTopicID(), o2.get(0).getTopicID());

        }
    }

    private static class ScoreComparator implements Comparator<Item> {

        ScoreComparator() {

        }

        @Override
        public int compare(Item o1, Item o2) {
            return Double.compare(o2.getScore(), o1.getScore());
        }

    }
}
