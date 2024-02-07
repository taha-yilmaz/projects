public class Customer {
    private String id;
    private String name;
    private HeapPriorityQueue<Transaction> transactions;

    public Customer(String id, String name, Transaction transaction) {
        this.id = id;
        this.name = name;
        transactions = new HeapPriorityQueue<>();
        transactions.add(transaction);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HeapPriorityQueue<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    public String toString() {
        String s = name + " | Transactions :";
        HeapPriorityQueue<Transaction> tempQueue = new HeapPriorityQueue<>();

        while (!transactions.isEmpty()) {
            Transaction currentTransaction = transactions.remove();
            s += currentTransaction.getDate() + " " + currentTransaction.getProductName() + ", ";
            tempQueue.add(currentTransaction);
        }

        // Restore transactions to the original queue
        while (!tempQueue.isEmpty()) {
            transactions.add(tempQueue.remove());
        }

        return s;
    }
}
