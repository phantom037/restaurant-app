public class Food {

	// -----------------------------------------------------------------------------
	// Properties
	// -----------------------------------------------------------------------------

	private String name;
	private double price;



	// -----------------------------------------------------------------------------
	// Constructor
	// -----------------------------------------------------------------------------

	public Food(String name, double price) {
		this.name = name;
		this.price = price;
	}



	// -----------------------------------------------------------------------------
	// Getters and Setters
	// -----------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}



	// -----------------------------------------------------------------------------
	// Other
	// -----------------------------------------------------------------------------

	@Override
	public String toString() {
		return name + ", Price: $" + price;
	}

}
