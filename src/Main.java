import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {

		// Creating a File object that represents the disk file
		PrintStream o = new PrintStream(new File("Output.txt"));

		PrintStream console = System.out;
		System.setOut(o);

		Scanner in = new Scanner(System.in);
		int counter = 0;
		File input_file = new File("E:\\curve_fitting_input.txt");
		Scanner scanner = new Scanner(input_file);
		//System.out.println(input_file);
		List<Integer> integers = new ArrayList<>();
		List<Float> floats = new ArrayList<>();
		while (scanner.hasNext()) {
			if (scanner.hasNextInt()) {
				integers.add(scanner.nextInt());
			} else if (scanner.hasNextFloat()) {
				floats.add(scanner.nextFloat());
			} else {
				scanner.next();
			}
		}

		int int_index = 1;
		int float_index = 0;
		while (counter < integers.get(0)) {
			System.out.println("******************");
			counter++;
			System.out.println("Data Set Index : " + counter);
			System.out.println("******************");

			// input the Data Points
			int Point_num = integers.get(int_index);
			int_index++;

			// input the Polynomial degree
			int polynomialDegree = integers.get(int_index);

			int_index++;
			DataPoint[] points = new DataPoint[Point_num];

			// Take the x and y of every point

			for (int i = 0; i < Point_num; i++) {
				float x = floats.get(float_index);
				float_index++;
				float y = floats.get(float_index);
				float_index++;
				points[i] = new DataPoint(x, y);

			}
			System.out.println("******************");

			GA ga = new GA();
			ga.run(polynomialDegree, points);
		}

	
		System.setOut(console);
		System.out.println("Check output.txt for the output!");
	}


}
