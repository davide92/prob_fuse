
import java.util.HashMap;
/*The class Topic represents the set of documents retrieved as answer of a specific topic,
  the topicID represent that topic and the HashMap is composed by a key that is the Id of a specific
  document and a instance of MyDocument that include of occurrences of that document in every run*/

class Topic {
    private final int topicID;
    private HashMap<String, MyDocument> documents;

     Topic(int topicID) {
        this.topicID = topicID;
        documents = new HashMap<>();
    }

    //When an item is added, the algorithm check if the id of the item is already contained
    //in the HashMap
     void addItem(Item tmp) {
        String docID = tmp.getDocumentID();
        if (documents.containsKey(docID)) {
            documents.get(docID).addItem(tmp);
        } else {
            MyDocument doc = new MyDocument(docID);
            doc.addItem(tmp);
            documents.put(docID, doc);
        }
    }

    HashMap<String, MyDocument> getItems() {
        return documents;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicID=" + topicID +
                ", documents=" + documents +
                '}';
    }

}
