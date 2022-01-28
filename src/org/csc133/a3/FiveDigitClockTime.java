package org.csc133.a3;

public class FiveDigitClockTime {

    private int minuets;
    private int seconds;
    private int milliseconds;
    private int numberOfDigits = 5;

    public FiveDigitClockTime() {
        minuets = 0;
        seconds = 0;
        milliseconds = 0;
    }

    public FiveDigitClockTime(int min, int sec, int mill) {
        minuets = min;
        seconds = sec;
        setMilliseconds(mill);
    }


    public int getMinuets() {
        return minuets;
    }

    public void setMinuets(int minuets) {
        this.minuets = minuets;
    }

    public String getMinuetsAsString(){
        if (minuets < 10)
            return '0' + Integer.toString(minuets);
        return Integer.toString(minuets);
    }

    public int getSeconds() {
        return seconds;
    }

    public  String getSecondsAsString(){
        if (seconds < 10)
            return '0' + Integer.toString(seconds);
        return Integer.toString(seconds);
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public String getMillisecondsAsString() {
        return Integer.toString(milliseconds);
    }

    public void setMilliseconds(int milliseconds) {
        if(milliseconds < 10){
            this.milliseconds = milliseconds;
        }
    }

    public int getFirstDigit(){
        String timeValueAsString = this.getTimeAsString();
        return Integer.parseInt(timeValueAsString.substring(0,1));
    }

    public int getSecondDigit(){
        String timeValueAsString = this.getTimeAsString();
        return Integer.parseInt(timeValueAsString.substring(1,2));
    }

    public int getThirdDigit(){
        String timeValueAsString = this.getTimeAsString();
        return Integer.parseInt(timeValueAsString.substring(2,3));
    }

    public int getFourthDigit(){
        String timeValueAsString = this.getTimeAsString();
        return Integer.parseInt(timeValueAsString.substring(3,4));
    }

    public int getFifthDigit(){
        String timeValueAsString = this.getTimeAsString();
        return Integer.parseInt(timeValueAsString.substring(4));
    }

    public String getTimeAsString() {
        String min = (minuets >= 10) ?
                Integer.toString(minuets) : "0" + Integer.toString(minuets);
        String sec = (seconds >= 10) ?
                Integer.toString(seconds) : "0" + Integer.toString(seconds);
        String mill = Integer.toString(milliseconds);

        return min + sec + mill;
    }

    public int getTimeAsInt() {
        return Integer.parseInt(getTimeAsString());
    }

    public FiveDigitClockTime differenceBetweenTimes(
            FiveDigitClockTime otherTime) {

        int min = Math.abs(this.getMinuets() - otherTime.getMinuets());
        int sec = Math.abs(this.getSeconds() - otherTime.getSeconds());
        int mill =
                Math.abs(this.getMilliseconds() - otherTime.getMilliseconds());

        return new FiveDigitClockTime(min, sec, mill);
    }

    private String fillTimeValueWithZeros(String timeValue) {
        int zerosToAdd = numberOfDigits - timeValue.length();
        for (int i = zerosToAdd; i > 0; i--) {
            timeValue = "0" + timeValue;
        }
        return timeValue;
    }
}
