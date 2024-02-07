public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;
    public Date(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public Boolean isValid(){
        if(month < 0 || month > 12)
            return false;
        if(day < 0 || day > MonthDayNum())
            return false;

        return true;
    }

    public int MonthDayNum(){
        int dayNum = switch (month){
            case 1, 3, 5, 7, 8, 10, 12-> 31;
            case 4, 6, 9, 11 -> 30;
            case 2 -> isLeapYear()? 29 : 28;
            default -> -1;
        };
        return dayNum;
    }

    public Boolean isLeapYear(){
        if(year % 4 == 0 && year % 1000 != 0)
            return true;
        return false;
    }
    @Override
    public int compareTo(Date other) {
        // Yıl karşılaştırması yap
        int yearComparison = Integer.compare(this.year, other.year);
        if (yearComparison != 0) {
            return yearComparison;
        }

        // Ay karşılaştırması yap
        int monthComparison = Integer.compare(this.month, other.month);
        if (monthComparison != 0) {
            return monthComparison;
        }

        // Gün karşılaştırması yap
        return Integer.compare(this.day, other.day);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
