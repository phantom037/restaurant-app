import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class DigitalDining implements Restaurant {

    // -----------------------------------------------------------------------------
    // Properties
    // -----------------------------------------------------------------------------

    private ArrayList<Waiter> waiters = new ArrayList<>();  // The list of servers
    private ArrayList<Food> menu = new ArrayList<>();       // The list of available food items
    private double totalSales = 0;                                  // The total sales of the restaurant
    private static final String menuFileName = "menu.txt";                // The food menu file name
    private static final String employeeFileName = "employees.txt";       // The employee file name


    // -----------------------------------------------------------------------------
    // Main Method
    // -----------------------------------------------------------------------------

    public static void main(String[] args) {

        // Create the restaurant and read in old file data
        DigitalDining restaurant = new DigitalDining();
        restaurant.readMenuFile(menuFileName);
        restaurant.readEmployeeFile(employeeFileName);
        restaurant.bubbleSort();

        // Start the server
        String host = "localhost";
        int port = 1234;
        SocketServer server = new SocketServer(new InetSocketAddress(host, port), restaurant);
        server.run();
    }



    // -----------------------------------------------------------------------------
    // General Methods, for use by the SocketServer class
    // -----------------------------------------------------------------------------

    /**
     * Tips a server of the specified index
     * @param waiterIndex index of the server to tip
     * @param tip the amount to tip
     */
    public void tipServer(int waiterIndex, double tip) {
        waiters.get(waiterIndex).addTip(tip);
    }

    /**
     * Orders a food item from a specific waiter
     * @param waiterIndex index of the server order from
     * @param foodIndex the food order number
     */
    public void orderFood(int waiterIndex, int foodIndex) {
        waiters.get(waiterIndex).orderFood(menu.get(foodIndex));
    }

    /**
     * Closes a specific waiter's table
     * @param waiterIndex the waiter index whose table to close
     */
    public void closeTable(int waiterIndex, boolean refund) {
        totalSales += waiters.get(waiterIndex).closeTable(refund);
    }

    /**
     * Fires an employee and removes it from the ArrayList
     * @param waiterIndex the index of the waiter to fire
     */
    public void fireEmployee(int waiterIndex) {
        waiters.remove(waiterIndex);
    }




    // -----------------------------------------------------------------------------
    // Implement: Restaurant
    // -----------------------------------------------------------------------------

    /**
     * Reads the list of food items and builds the list
     * @param fileName The name of the input file
     */
    @Override
    public void readMenuFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                Food food = new Food(in.nextLine(), in.nextDouble());
                in.nextLine(); // Clear out extra \n
                menu.add(food);
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads the list of employees and builds the list
     * @param fileName The name of the input file
     */
    @Override
    public void readEmployeeFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String name = in.nextLine();
                if (name.equals("[totalSales]")) {
                    this.totalSales = in.nextDouble();
                    return;
                }
                double salary = in.nextDouble();
                in.nextLine(); // Clear out extra \n
                Waiter waiter = new Waiter(name, salary, in.nextDouble());
                if (in.hasNextLine()) {
                    in.nextLine(); // Clear out extra \n
                }
                waiters.add(waiter);
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void writeEmployeeFile(PrintWriter out) {


        for (int i = 0; i < waiters.size(); i++) {
            Waiter s = waiters.get(i);
            out.println(s.getName());
            out.println(s.getSalary());
            out.println(s.getTotalTips());
        }
        out.println("[totalSales]");
        out.println(this.totalSales);
        out.close();
    }

    /**
     * Sorts the list of servers
     */
    @Override
    public void bubbleSort() {
        int n = waiters.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (waiters.get(j).compareTo(waiters.get(j + 1)) > 0) {
                    Waiter tmp = waiters.get(j);
                    waiters.set(j, waiters.get(j + 1));
                    waiters.set(j + 1, tmp);
                }
            }
        }
    }


    /**
     * Finds the index of a server from the list
     * @param w The server to find
     * @return The index of the server
     */
    @Override
    public int binarySearch(Waiter w) {
        int first  = 0, mid;
        int last = waiters.size();

        while (first <= last){
            mid = (first + last)/2;
            if (waiters.get(mid).equals(w)){
                return mid;
            } else if (waiters.get(mid).compareTo(w) < 0){
                first = mid + 1;
            } else {
                last = mid - 1;
            }
        }
        return 0;
    }

}
