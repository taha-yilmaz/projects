import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;

public class MenagamentSystem{
    private HashTable<String, Customer> idCustomerHashTable;

    public MenagamentSystem(String path, HashingMode hashingMode, ProbingMode probingMode, double loadFactor) {
        idCustomerHashTable = new HashTable<>(hashingMode, probingMode, loadFactor);

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                // Verileri sırasıyla id, name, date, product değişkenlerine ata
                String id = data[0].trim();
                String name = data[1].trim();
                String date = data[2].trim();
                String product = data[3].trim();

                Transaction transaction = new Transaction(date, product);
                Customer existingCustomer = idCustomerHashTable.get(id);
                if (existingCustomer == null)
                    idCustomerHashTable.add(id, new Customer(id, name, transaction));
                else    // Var olan müşteriyi güncelle
                    existingCustomer.setTransactions(transaction);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void add(String id, Customer customer) {
        id = id.trim(); // Bu satır eklenerek gereksiz trim işlemleri önceden yapılır
        Customer existingCustomer = idCustomerHashTable.get(id);
        if (existingCustomer == null) {
            // Yeni müşteriyi oluştur ve hash tablosuna ekle
            idCustomerHashTable.add(id, customer);
        } else {
            // Var olan müşteriyi güncelle
            existingCustomer.setTransactions(customer.getTransactions().peek());
        }
    }

    public void get(String id)
    {
        Customer customer = idCustomerHashTable.get(id.trim());
        if (customer == null)
            System.out.println(id + " No user with that id");
        else
            System.out.println(customer.toString());
    }
    public Customer remove(String id)
    {
        Customer customer = idCustomerHashTable.remove(id.trim());
        if(customer == null) {
            System.out.println("No user with that id to remove");
        }
        return customer;
    }
    public long getCollCount()
    {
        return idCustomerHashTable.getCollCount();
    }

    public void display(){
        for (HashEntry<String, Customer> entry : idCustomerHashTable) {
            if(entry.getKey() != null)
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().toString());
        }
    } // end display






}

