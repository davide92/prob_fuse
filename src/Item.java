/*The Item class is used to represent a row of a query, this class contain the value of the unique identifier
of the topic,the run and the document, and is saved the value of the score and the rank. This class is used
in the import of the run and in the export of the fusion run.
 */
public class Item {
    private final int topicID;
    private final String documentID;
    private final String runID;
    private int rank;
    private double score;

    public Item(int topicID, String documentID, int rank, double score, String runID) {
        this.topicID = topicID;
        this.documentID = documentID;
        this.rank = rank;
        this.score = score;
        this.runID = runID;
    }

    public int getTopicID() {
        return topicID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int newrank) {
        rank = newrank;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Item{" +
                "topicID=" + topicID +
                ", documentID='" + documentID + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                ", runID=" + runID +
                '}';
    }

    public String toTrecFormat() {
        return topicID + " Q0 " + documentID + " " + rank + " " + score + " " + runID;
    }

    //this method compute the normalization of the relevance score using the min and the max number computed
    //with the method getMinMax in the class Util.
    public void minMaxNorm(double min, double max) {
        score = (score - min) / (max - min);
    }
}
