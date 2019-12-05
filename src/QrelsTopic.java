import java.util.HashMap;
/*This class is used to represent the set of relevance judgments about a specific topic, the HashMap
is composed by the key what is the id of the document and the value that is the relevance score, in this
way the access of a specific relevance judgements is of constant temporal complexity.
*/
class QrelsTopic {
    private final HashMap<String, Integer> qrels;

    public QrelsTopic(String idDoc, int relevance) {
        qrels = new HashMap<>();
        qrels.put(idDoc, relevance);
    }

    public void put(String docId, Integer relevance) {
        qrels.put(docId, relevance);
    }

    public HashMap<String, Integer> getQrels() {
        return qrels;
    }
}
