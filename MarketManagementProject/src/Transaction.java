public class Transaction implements Comparable<Transaction> {
    private String date;
    private String productName;

    public Transaction(String date, String productName) {
        this.date = date;
        this.productName = productName;
    }

    public String getDate() {
        return date;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public int compareTo(Transaction other) {
        // Date'ları karşılaştır
        return this.date.compareTo(other.date);
    }
}
