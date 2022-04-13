import java.io.PrintWriter;

public interface Restaurant {

	void readMenuFile(String fileName);
	void readEmployeeFile(String fileName);
	void bubbleSort();
	int binarySearch(Waiter w);
	void writeEmployeeFile(PrintWriter out);
	
}

