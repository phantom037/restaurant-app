public abstract class Employee {

    // -----------------------------------------------------------------------------
    // Properties
    // -----------------------------------------------------------------------------

    private double salary;
    private String name;
    private double totalTips;



    // -----------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------

    public Employee() {
        this("Unknown Name",0.0, 0.0);
    }

    public Employee(String name, double salary, double totalTips) {
        this.salary = salary;
        this.name = name;
        this.totalTips = totalTips;
    }



    // -----------------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------------

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTips(double totalTips) {
        this.totalTips = totalTips;
    }

    public double getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }

    public double getTotalTips() {
        return totalTips;
    }



    // -----------------------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------------------

    public void addTip(double tip) {
        totalTips += tip;
    }

}