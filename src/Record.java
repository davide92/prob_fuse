/* The class Record is used to represent a document retrieved by an specific query in a specific
   run, in the instances of this class is reported the number of the run, the relevance weight of the
   document and the index of the segment where is located the documents.

 */
class Record {
    private final String idRecord;
    private final int run;
    private final int rank;
    private int relevance = -1;
    private int segmentIndex=-1;

    public Record(String idRecord, int rank, int run) {
        this.idRecord = idRecord;
        this.rank = rank;
        this.run = run;
    }

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public void setSegmentIndex(int num) {
        segmentIndex = num;
    }

    public String getIdRecord() {
        return idRecord;
    }

    public int getRank() {
        return rank;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public int getRun() {
        return run;
    }

    @Override
    public String toString() {
        return "Record{" +
                "idRecord='" + idRecord + '\'' +
                ", rank=" + rank +
                ", relevance=" + relevance +
                '}';
    }
}
