import java.util.ArrayList;
import java.util.Arrays;

/*The class MyDocument is used to group all the occurrences of a specific document in all different run as
* answer of a specific topic. The ArrayList<Item> relatedItem is the list that collect all the item that
* represent the occurrence in all different run. Using this class is simply to compute the result of the
* Classic fusion run algorithm.*/
class MyDocument {

    private final String idDoc;
    private final ArrayList<Item> relatedItem;
    private int numItems;
    private double scoreMin;
    private double scoreMax;
    private double scoreSum;
    private int topicId;

    public MyDocument(String idDoc) {
        this.idDoc = idDoc;
        relatedItem = new ArrayList<>();

        numItems = 0;
        scoreMin = 1;
        scoreMax = 0;
        scoreSum = 0;


    }
    //Every time that an item is added to a relatedItem list, updateVars updates the value of score max,
    //score min and the score's sum, avoiding calculating them subsequently.
    public void addItem(Item tmp) {
        updateVars(tmp);
        relatedItem.add(tmp);
        topicId = tmp.getTopicID();
    }

    private void updateVars(Item item) {
        numItems++;

        if (item.getScore() < scoreMin) scoreMin = item.getScore();
        if (item.getScore() > scoreMax) scoreMax = item.getScore();
        scoreSum = scoreSum + item.getScore();
    }

    //this method return to score for the document using CombMin
    private Double getCombMin() {
        return scoreMin;
    }

    //this method return the item that represent the result of the CombMin computation
    public Item getCombMinItem() {
        return new Item(topicId, idDoc, 0, getCombMin(), "CombMin");
    }

    //this method return to score for the document using CombMax
    private Double getCombMax() {
        return scoreMax;
    }

    //this method return the item that represent the result of the CombMax computation
    public Item getCombMaxItem() {
        return new Item(topicId, idDoc, 0, getCombMax(), "CombMax");
    }

    //this method return to score for the document using CombSum
    private Double getCombSum() {
        return scoreSum;
    }

    //this method return the item that represent the result of the CombSum computation
    public Item getCombSumItem() {
        return new Item(topicId, idDoc, 0, getCombSum(), "CombSum");
    }

    //this method return to score for the document using CombAnz
    private Double getCombAnz() {
        return scoreSum / numItems;
    }

    //this method return the item that represent the result of the CombAnz computation
    public Item getCombAnzItem() {
        return new Item(topicId, idDoc, 0, getCombAnz(), "CombAnz");
    }

    //this method return to score for the document using CombMnz
    private Double getCombMnz() {
        return scoreSum * numItems;
    }

    //this method return the item that represent the result of the CombMnz computation
    public Item getCombMnzItem() {
        return new Item(topicId, idDoc, 0, getCombMnz(), "CombMnz");
    }

    //this method return to score for the document using CombMed
    private Double getCombMed() {

        Double[] val = new Double[numItems];
        int i = 0;

        for (Item tmp : relatedItem) {
            val[i] = tmp.getScore();
            i++;
        }

        Arrays.sort(val);
        double median;
        if (val.length % 2 == 0)
            median = (val[val.length / 2] + val[val.length / 2 - 1]) / 2;
        else
            median = val[val.length / 2];

        return median;
    }

    //this method return the item that represent the result of the CombMed computation
    public Item getCombMedItem() {
        return new Item(topicId, idDoc, 0, getCombMed(), "CombMed");
    }


}
