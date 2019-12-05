import java.util.ArrayList;
/*This class is used to collects the documents that are retrieved by a specific topic*/
class ResQuery {
    private final int idTopic;
    private ArrayList<Record> listRecord = null;
    private int firstElement = 0;

    //initialization: the ogject resquery contains a topic and an arraylist of records.
    public ResQuery(int idTopic) {
        this.idTopic = idTopic;
        listRecord = new ArrayList<>();
    }

    public void addRecord(Record record) {
        listRecord.add(record);
    }

    public ArrayList<Record> getListRecord() {
        return listRecord;
    }


    @Override
    public String toString() {
        return "ResQuery{" +
                "idTopic=" + idTopic +
                ", listRecord=" + listRecord +
                '}';
    }

    //computation of segments.
    //In the creation of segments we calculate the number of item per segment using the whole division.
    //If the rest isn't equal to zero, document in excess are redistributed in each segment until all of its are paced in a segment
    public double relevanceRatio(int k, int numSegment) {
        int numItemPerSegment = listRecord.size() / numSegment;
        int resto = listRecord.size() % numSegment;
        if (k <= resto) {
            numItemPerSegment++;
        }
        //first element and last element are the first and the last elements of the list that belongs to the considered segment
        int lastElement = firstElement + numItemPerSegment - 1;
        if (k == numSegment) {
            lastElement = listRecord.size() - 1;
        }

        if (lastElement >= listRecord.size()) lastElement = listRecord.size() - 1;
        double numRelDoc = 0;
        double numNotRelDoc = 0;
        //get relevance judgement and accumulate values in vars numRelDoc and numNotRelDoc.
        for (int i = firstElement; i <= lastElement; i++) {
            if (listRecord.get(i).getRelevance() == 1) numRelDoc++;
            else if (listRecord.get(i).getRelevance() == 0) numNotRelDoc++;
        }
        firstElement = lastElement + 1;
        if (numRelDoc + numNotRelDoc == 0) return 0;
        return numRelDoc / (numRelDoc + numNotRelDoc);
    }


}
