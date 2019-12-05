import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("SameParameterValue")
class ProbFuse {

    private final int numSegment;
    private final int percentage;
    private final ArrayList<HashMap<Integer, ResQuery>> trainingSet;
    private final ArrayList<HashMap<Integer, ResQuery>> listArrayList;
    private final HashMap<Integer, QrelsTopic> qrels;
    private int firstTopic;
    private HashMap<Integer, Object> selectedValue;
    private int numQuery;
    private int lastTopic;
    private double[][] probability;
    private int numTopic;


    ProbFuse(HashMap<Integer, QrelsTopic> qrels, ArrayList<HashMap<Integer, ResQuery>> listArrayList, int percentage, int numSegment) {
        trainingSet = new ArrayList<>();
        this.qrels = qrels;
        this.listArrayList = listArrayList;
        this.percentage = percentage;
        this.numSegment = numSegment;
        findBoundedQuery();
    }

    //if the judgedProbfuse value is set to true the probfuseJudged is computed else
    // probFuseAll is computed
    //this method computes all the steps to realize the prob fuse algorithm solution
    void compute(String pathOutput, boolean judgedProbFuse) {

        if (judgedProbFuse) getTrainingSetJudged();
        else getTrainingSetAll();
        System.out.println();
        training();
        printProb();
        System.out.println();
        ArrayList<Item> aaa = finalCompute();
        Util.writeOutput(aaa, pathOutput);
    }

    // this method is used to compute the first and the last topic in the run
    private void findBoundedQuery() {
        int maxIndexTopic = Integer.MIN_VALUE;
        int minIndexTopic = Integer.MAX_VALUE;
        for (Integer tempIndex : qrels.keySet()) {
            if (maxIndexTopic < tempIndex) {
                maxIndexTopic = tempIndex;
            }
            if (minIndexTopic > tempIndex) {
                minIndexTopic = tempIndex;
            }
        }
        firstTopic = minIndexTopic;
        lastTopic = maxIndexTopic;
        numTopic = lastTopic - firstTopic + 1;
        System.out.println("firstTopic = " + firstTopic);
        System.out.println("lastTopic = " + lastTopic);
        System.out.println("numTopic = " + numTopic);
    }

    private void getTrainingSetJudged() {
        getTrainingSet(-1);
    }

    private void getTrainingSetAll() {
        getTrainingSet(0);
    }

    //the topics to be used as training are randomly extracted
    //topics used as training are removed from those used for evaluation
    //in the case of not judged probFuse the not judged documents are set to relevance -1 and
    //subsequently not compute in the training
    private void getTrainingSet(int notJudgedScore) {

        selectedValue = new HashMap<>();
        numQuery = (numTopic * percentage) / 100;
        Random random = new Random();
        for (int i = 0; i < numQuery; i++) {
            int queryId = random.nextInt(lastTopic - firstTopic) + firstTopic;
            while (selectedValue.containsKey(queryId)) {
                queryId = random.nextInt(lastTopic - firstTopic) + firstTopic;
            }
            selectedValue.put(queryId, null);
        }


        for (int j = 0; j < listArrayList.size(); j++) {
            HashMap<Integer, ResQuery> tempMap = new HashMap<>();
            for (Map.Entry<Integer, Object> entry : selectedValue.entrySet()) {
                ResQuery resQuery = listArrayList.get(j).remove(entry.getKey());
                tempMap.put(entry.getKey(), resQuery);
            }
            trainingSet.add(j, tempMap);
        }

        //int these nested for loop, it is made the assessment of the training query, if an relevance
        //judgments is not present in the qrels, the relevance is set to minus one.
        for (HashMap<Integer, ResQuery> map : trainingSet) {
            for (Map.Entry<Integer, ResQuery> entry : map.entrySet()) {
                for (Record record : entry.getValue().getListRecord()) {
                    QrelsTopic qrelsTopic = qrels.get(entry.getKey());
                    if (qrelsTopic.getQrels().containsKey(record.getIdRecord())) {
                        int relevance = qrelsTopic.getQrels().get(record.getIdRecord());
                        record.setRelevance(relevance);
                    } else {
                        record.setRelevance(notJudgedScore);
                    }

                }
            }

        }

        System.out.println("Training set dimension : " + trainingSet.get(0).size());
        System.out.println("Evaluation set dimension:  " + listArrayList.get(0).size());

        int minDimension = Integer.MAX_VALUE;
        for (HashMap<Integer, ResQuery> run : listArrayList) {
            for (Map.Entry<Integer, ResQuery> entry : run.entrySet()) {
                if (entry.getValue().getListRecord().size() < minDimension) {
                    minDimension = entry.getValue().getListRecord().size();
                }
            }
        }

        System.out.println("Min number of documents retrieval by the queries" + minDimension);
        System.out.println("Number of segment : " + numSegment);


    }

    //this method compute the matrix of the probability, the matrix have dimensions number of segment and
    //number of run.
    private void training() {
        probability = new double[numSegment][trainingSet.size()];
        //for every retrieval model
        for (int i = 0; i < trainingSet.size(); i++) {
            //for every segment
            for (int j = 0; j < numSegment; j++) {
                double sum = 0;
                for (Map.Entry<Integer, ResQuery> entry : trainingSet.get(i).entrySet()) {
                    sum += trainingSet.get(i).get(entry.getKey()).relevanceRatio(j + 1, numSegment);
                }
                probability[j][i] = sum / numQuery;
            }
        }

    }

    //this method plot the probability matrix
    private void printProb() {
        System.out.println();
        System.out.println("Probability Matrix");
        System.out.println();
        for (double[] line : probability) {
            for (double prob : line) System.out.format("%.3f  ", prob);
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    //this method compute the score for every document retrieved from every topic.
    private ArrayList<Item> finalCompute() {
        HashMap<Integer, TransverseResQuery> store = new HashMap<>();
        for (HashMap<Integer, ResQuery> run : listArrayList) {
            for (Map.Entry<Integer, ResQuery> query : run.entrySet()) {
                for (Record record : query.getValue().getListRecord()) {
                    int[] thresholdSegment;
                    thresholdSegment = computeThreshold(numSegment, query.getValue().getListRecord().size());
                    int rankDoc = record.getRank();
                    int segmentIndex = getSegment(thresholdSegment, rankDoc);
                    record.setSegmentIndex(segmentIndex);
                    if (store.containsKey(query.getKey())) {
                        store.get(query.getKey()).put(record);
                    } else {
                        TransverseResQuery newTransverseResQuery = new TransverseResQuery(query.getKey());
                        newTransverseResQuery.put(record);
                        store.put(query.getKey(), newTransverseResQuery);
                    }
                }
            }
        }

        ArrayList<Item> result = new ArrayList<>();
        for (int i = firstTopic; i <= lastTopic; i++) {
            System.out.println("Processing query: " + i);
            if (!selectedValue.containsKey(i)) {
                System.out.println("The query is not in the training set");
                result.addAll(store.get(i).computeResult(probability));
            } else {
                System.out.println("The query is in the training set");
            }
        }

        return result;

    }

    // this method return the segment index taken the rank and the array of the bound.
    private int getSegment(int[] segmentThreshold, int rank) {
        for (int i = 0; i < segmentThreshold.length; i++) {
            if (segmentThreshold[i] >= rank) {
                return i;
            }
        }
        return segmentThreshold.length;
    }

    //this method compute the upper threshold that divide the segment
    private int[] computeThreshold(int numSegment, int numElement) {
        int[] ret = new int[numSegment];
        ret[0] = -1;
        int rest = numElement % numSegment;
        int division = numElement / numSegment;
        int i = 1;
        while (i <= rest) {
            ret[i] = ret[i - 1] + division + 1;
            i++;
        }
        while (i < numSegment) {
            ret[i] = ret[i - 1] + division;
            i++;
        }
        return ret;


    }


}
