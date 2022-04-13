import java.util.ArrayList;

public class Waiter extends Employee implements Comparable<Waiter> {

    // -----------------------------------------------------------------------------
    // Properties
    // -----------------------------------------------------------------------------

	private ArrayList<Food> order = new ArrayList<>();



    // -----------------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------------

    /**
     * Constructs a waiter with the specified
     * name, salary, and total tip amount
     */
	public Waiter(String name, double salary, double totalTips) {
	    super(name, salary, totalTips);
    }



    // -----------------------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------------------

    /**
     * Returns the food order
     * @return an ArrayList of Food objects
     */
    public ArrayList<Food> getOrder() {
        return order;
    }

    /**
     * Adds a food item to the waiter's list
     * @param food the food to order
     */
	public void orderFood(Food food) {
		order.add(food);
	}

    /**
     * Ends a meal by collecting payment for the bill and clearing the order ArrayList
     * @param refund whether the customer is requesting a refund
     * @return the total amount paid
     */
	public double closeTable(boolean refund) {
	    double sales = refund ? 0.0 : calcPrice();
	    order.clear();
	    return sales;
    }

    /**
     * Calculates the total price of the current order
     * @return the total price
     */
    public double calcPrice() {
	    double totalPrice = 0;
        for (int i = 0; i < order.size(); i++) {
            totalPrice += order.get(i).getPrice();
        }
        return totalPrice;
    }



    // -----------------------------------------------------------------------------
    // Implement: Comparable
    // -----------------------------------------------------------------------------

    /**
     * Compares two waiters
     * Returns 0 if the waiters are equal
     * It checks the following properties in sequential order:
     * Name, salary, and then total tips
     * @param w the waiter to compare against
     * @return the difference
     */
    @Override
    public int compareTo(Waiter w) {
        if (this.equals(w)) {
            return 0;
        } else if(!(getName().equals(w.getName()))) { // Compare name first
            return getName().compareTo(w.getName());
        } else if(!(getSalary() == w.getSalary())) { // Compare name first
            return Double.compare(getSalary(), w.getSalary());
        } else {
            return Double.compare(getTotalTips(), w.getTotalTips());
        }
    }

    /**
     * Checks if two waiters are equal based on
     * their name, salary, and total tips
     * @param o the waiter to compare
     * @return whether they are equal
     */
    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Waiter)) {
            return false;
        } else {
            Waiter s = (Waiter) o;
            return getName().equals(s.getName()) && getSalary() == s.getSalary() && getTotalTips() == s.getTotalTips();
        }
    }



    // -----------------------------------------------------------------------------
    // Other
    // -----------------------------------------------------------------------------

    /**
     * Creates a string representation that displays the
     * name, salary, and total amount of tips
     * @return
     */
    @Override
    public String toString() {
        return "Waiter Name: " + getName() + "\nSalary: " + getSalary() + "\nTotal Tips: " + getTotalTips();
    }
}
