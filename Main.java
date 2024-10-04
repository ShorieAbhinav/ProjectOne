// Abhinav Shorie
// AXS240124

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for input files
        System.out.print("Enter the regular customer file name: ");
        String regularCustomerFilePath = scanner.nextLine();

        System.out.print("Enter the preferred customer file name: ");
        String preferredCustomerFilePath = scanner.nextLine();

        System.out.print("Enter the purchase file name: ");
        String ordersFilePath = scanner.nextLine();

        // Read customer data from files
        Customer[] regularCustomers = readRegularCustomers(regularCustomerFilePath);
        Customer[] preferredCustomers = readPreferredCustomers(preferredCustomerFilePath);

        // Process orders from file
        processOrders(ordersFilePath, regularCustomers, preferredCustomers);

        // Write customer data back to files
        writeCustomersToFile(regularCustomers, "customer.dat");
        writeCustomersToFile(preferredCustomers, "preferred.dat");

        scanner.close();
    }

    // Method to read regular customers from a file
public static Customer[] readRegularCustomers(String filePath) {
    try {
        File file = new File(filePath);
        Scanner fileScanner = new Scanner(file);
        int customerCount = 0;

        // Counting lines (customers)
        while (fileScanner.hasNextLine()) {
            customerCount++;
            fileScanner.nextLine();
        }

        // Reinitialize scanner to read customers
        fileScanner = new Scanner(file);
        Customer[] customers = new Customer[customerCount];
        int index = 0;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] data = line.split(" ");
            String guestID = data[0];
            String firstName = data[1];
            String lastName = data[2];
            float amountSpent = Float.parseFloat(data[3]);

            customers[index++] = new Customer(firstName, lastName, guestID, amountSpent);
        }

        fileScanner.close();
        return customers;

    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + filePath);
        return new Customer[0];
    }
}

// Method to read preferred customers from a file
public static Customer[] readPreferredCustomers(String filePath) {
    try {
        File file = new File(filePath);
        if (!file.exists()) return new Customer[0]; 

        Scanner fileScanner = new Scanner(file);
        int customerCount = 0;

        // Counting lines (customers)
        while (fileScanner.hasNextLine()) {
            customerCount++;
            fileScanner.nextLine();
        }

        // Reinitialize scanner to read customers
        fileScanner = new Scanner(file);
        Customer[] customers = new Customer[customerCount];
        int index = 0;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] data = line.split(" ");
            String guestID = data[0];              
            String firstName = data[1];            
            String lastName = data[2];             
            float amountSpent = Float.parseFloat(data[3]); 
            double discountOrBonus = Double.parseDouble(data[4]); 

            // Determine whether customer is Gold or Platinum based on the amount spent
            if (amountSpent < 200) {
                customers[index++] = new Gold(firstName, lastName, guestID, amountSpent, discountOrBonus);
            } else {
                customers[index++] = new Platinum(firstName, lastName, guestID, amountSpent, (int) discountOrBonus);
            }
        }

        fileScanner.close();
        return customers;

    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + filePath);
        return new Customer[0];
    }
}

    // Method to process orders from file
public static void processOrders(String filePath, Customer[] regularCustomers, Customer[] preferredCustomers) {
    try {
        File file = new File(filePath);
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] data = line.split(" ");
            if (data.length != 5) {
                continue; // Skip invalid orders
            }

            String guestID = data[0];  
            char size = data[1].charAt(0); 
            String drinkType = data[2];  
            float pricePerSquareInch = Float.parseFloat(data[3]);  
            int quantity = Integer.parseInt(data[4]); 

            // Find the customer by guestID
            Customer customer = findCustomerByID(guestID, regularCustomers, preferredCustomers);
            if (customer == null) {
                System.out.println("Invalid Customer ID: " + guestID);
                continue;
            }

            // Calculate the total order cost
            float orderTotal = calculateOrderTotal(size, drinkType, pricePerSquareInch, quantity);

            // Apply discount or bonus bucks if applicable
            if (customer instanceof Gold) {
                orderTotal = ((Gold) customer).applyDiscount(orderTotal);
            } else if (customer instanceof Platinum) {
                orderTotal = ((Platinum) customer).applyBonusBucks(orderTotal);
            }

            // Update the customer’s amount spent
            customer.setAmountSpent(customer.getAmountSpent() + orderTotal);

            // Handle promotion logic (Regular -> Gold, Gold -> Platinum)
            if (customer instanceof Customer && !(customer instanceof Gold || customer instanceof Platinum)) {
                if (customer.getAmountSpent() >= 50) {
                    promoteToGold(customer, regularCustomers, preferredCustomers);
                }
            } else if (customer instanceof Gold) {
                if (customer.getAmountSpent() >= 200) {
                    promoteToPlatinum((Gold) customer, preferredCustomers);
                }
            }

        }

        fileScanner.close();
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + filePath);
    }
}


    // Find a customer by guest ID
    public static Customer findCustomerByID(String guestID, Customer[] regularCustomers, Customer[] preferredCustomers) {
        for (Customer customer : regularCustomers) {
            if (customer.getGuestID().equals(guestID)) {
                return customer;
            }
        }
        for (Customer customer : preferredCustomers) {
            if (customer.getGuestID().equals(guestID)) {
                return customer;
            }
        }
        return null;
    }

    // Promote a regular customer to Gold status
    public static void promoteToGold(Customer customer, Customer[] regularCustomers, Customer[] preferredCustomers) {
        for (int i = 0; i < regularCustomers.length; i++) {
            if (regularCustomers[i] == customer) {
                Gold goldCustomer = new Gold(customer.getFirstName(), customer.getLastName(), customer.getGuestID(), customer.getAmountSpent(), 5);
                preferredCustomers = addCustomerToPreferred(goldCustomer, preferredCustomers);
                regularCustomers = removeCustomerFromRegular(i, regularCustomers);
                System.out.println("Customer promoted to Gold: " + customer.getGuestID());
                break;
            }
        }
    }

    // Promote a Gold customer to Platinum status
    public static void promoteToPlatinum(Gold goldCustomer, Customer[] preferredCustomers) {
        for (int i = 0; i < preferredCustomers.length; i++) {
            if (preferredCustomers[i] == goldCustomer) {
                Platinum platinumCustomer = new Platinum(goldCustomer.getFirstName(), goldCustomer.getLastName(), goldCustomer.getGuestID(), goldCustomer.getAmountSpent(), 0);
                preferredCustomers[i] = platinumCustomer;
                System.out.println("Customer promoted to Platinum: " + platinumCustomer.getGuestID());
                break;
            }
        }
    }

    // Add a customer to the preferred customer array
    public static Customer[] addCustomerToPreferred(Customer customer, Customer[] preferredCustomers) {
        Customer[] newPreferredCustomers = new Customer[preferredCustomers.length + 1];
        System.arraycopy(preferredCustomers, 0, newPreferredCustomers, 0, preferredCustomers.length);
        newPreferredCustomers[preferredCustomers.length] = customer;
        return newPreferredCustomers;
    }

    // Remove a customer from the regular customer array
    public static Customer[] removeCustomerFromRegular(int index, Customer[] regularCustomers) {
        Customer[] newRegularCustomers = new Customer[regularCustomers.length - 1];
        for (int i = 0, j = 0; i < regularCustomers.length; i++) {
            if (i != index) {
                newRegularCustomers[j++] = regularCustomers[i];
            }
        }
        return newRegularCustomers;
    }

    // Calculate order total based on drink size and type
    public static float calculateOrderTotal(char size, String drinkType, float pricePerSquareInch, int quantity) {
        int ounces = 0;
        switch (size) {
            case 'S': ounces = 12; break;
            case 'M': ounces = 20; break;
            case 'L': ounces = 32; break;
        }
        float pricePerOunce = switch (drinkType.toLowerCase()) {
            case "soda" -> 0.20f;
            case "tea" -> 0.12f;
            case "punch" -> 0.15f;
            default -> 0;
        };
        return (ounces * pricePerOunce + pricePerSquareInch) * quantity;
    }


    public static void writeCustomersToFile(Customer[] customers, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName);
            for (Customer customer : customers) {
                if (customer instanceof Gold) {
                    Gold goldCustomer = (Gold) customer;
                    // Format amountSpent and discountPercentage to two decimal places
                    writer.printf("%s %s %s %.2f %.2f\n", 
                        customer.getGuestID(), 
                        customer.getFirstName(), 
                        customer.getLastName(), 
                        customer.getAmountSpent(), 
                        goldCustomer.getDiscountPercentage());
                } else if (customer instanceof Platinum) {
                    Platinum platinumCustomer = (Platinum) customer;
                    // Format amountSpent to two decimal places and bonusBucks as an integer
                    writer.printf("%s %s %s %.2f %d\n", 
                        customer.getGuestID(), 
                        customer.getFirstName(), 
                        customer.getLastName(), 
                        customer.getAmountSpent(), 
                        platinumCustomer.getBonusBucks());
                } else {
                    // For regular customers, format amountSpent to two decimal places
                    writer.printf("%s %s %s %.2f\n", 
                        customer.getGuestID(), 
                        customer.getFirstName(), 
                        customer.getLastName(), 
                        customer.getAmountSpent());
                }
            }
         writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + fileName);
        }
    }
}
