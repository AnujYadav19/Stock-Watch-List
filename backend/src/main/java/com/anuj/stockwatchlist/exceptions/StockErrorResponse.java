package com.anuj.stockwatchlist.exceptions;

public class StockErrorResponse {
    private int status;
    private String message;
    private long timeStamp;

    public StockErrorResponse() {
    }

    public StockErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "StockErrorResponse [status=" + status + ", message=" + message + ", timeStamp=" + timeStamp + "]";
    }

}
