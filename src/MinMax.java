/*
This class is used in the computation of the min and the max value of the relevance score,
to return in a single element the two values.
 */
class MinMax {
    private double min = 0;
    private double max = 0;

    public MinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
