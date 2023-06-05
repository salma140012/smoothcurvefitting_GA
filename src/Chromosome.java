import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class Chromosome {
	float[] genes;// = new float[this.len];
	int len;
	float total_error;
	float fitness;

	Chromosome() {
	}
	public Chromosome(int PolynomialDegree) {
		this.len = PolynomialDegree + 1;
		this.genes=new float [this.len];
	}
	public Chromosome(float[] genes) {
		this.genes = genes;
	}

	public float[] geneGeneration(int PolynomialDegree) {
		this.len = PolynomialDegree + 1;
		this.genes = new float[this.len];
		int min = -10;
		int max = 10;
		Random r = new Random();
		for (int i = 0; i < len; i++) {
			float tempValue = min + r.nextFloat() * (max - min);
			float value = new BigDecimal(tempValue).setScale(2, RoundingMode.HALF_UP).floatValue();

			this.genes[i] = value;

		}

		return genes;
	}

	void setFitness(float fitness) {
		this.fitness = fitness;
	}

	float getFitness() {
		return this.fitness;
	}

	void setTotalError(float total_error) {
		this.total_error = total_error;
	}

	float getTotalError() {
		return this.total_error;
	}

	float[] getgenes() {
		return this.genes;
	}

	void print() {

		System.out.println(Arrays.toString(genes));

	}

}
