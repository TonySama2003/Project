package vn.myhome.dto;

public class RevenueDataDTO {
    private int year ;
    private int month;
    private double totalPriceMonth;
    private double totalPriceBkCancelMonth;
    private long numCompleted;
    private long numCancelled;
    private long sumCompletedAndCancelled;


    public RevenueDataDTO() {
    }

    public RevenueDataDTO(int year, int month, double totalPriceMonth) {
        this.year = year;
        this.month = month;
        this.totalPriceMonth = totalPriceMonth;
    }


    public RevenueDataDTO(int year, int month, long numCompleted, long numCancelled, long sumCompletedAndCancelled) {
        this.year = year;
        this.month = month;
        this.numCompleted = numCompleted;
        this.numCancelled = numCancelled;
        this.sumCompletedAndCancelled = sumCompletedAndCancelled;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalPriceMonth() {
        return totalPriceMonth;
    }

    public void setTotalPriceMonth(double totalPriceMonth) {
        this.totalPriceMonth = totalPriceMonth;
    }

    public long getNumCompleted() {
        return numCompleted;
    }

    public void setNumCompleted(long numCompleted) {
        this.numCompleted = numCompleted;
    }

    public long getNumCancelled() {
        return numCancelled;
    }

    public void setNumCancelled(long numCancelled) {
        this.numCancelled = numCancelled;
    }

    public long getSumCompletedAndCancelled() {
        return sumCompletedAndCancelled;
    }

    public void setSumCompletedAndCancelled(long sumCompletedAndCancelled) {
        this.sumCompletedAndCancelled = sumCompletedAndCancelled;
    }

    public double getTotalPriceBkCancelMonth() {
        return totalPriceBkCancelMonth;
    }

    public void setTotalPriceBkCancelMonth(double totalPriceBkCancelMonth) {
        this.totalPriceBkCancelMonth = totalPriceBkCancelMonth;
    }
}
