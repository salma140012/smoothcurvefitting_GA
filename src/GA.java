import java.util.Arrays;
import java.util.Random;

public class GA {
	int popSize = 10;
	Chromosome cr;
	Chromosome[] population;
	Chromosome[] Parents;
	Chromosome[] offsprings;
	double crossOverProbability = 0.7;
	double mutationProbability = 0.07;
	int t = 0; // current generation number
	int T = 5; // max num of generations

	public GA() {
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	void run(int PolynomialDegree, DataPoint[] DataPoints) {

		// populationGeneration
		System.out.println("****************** First Generation ******************");

		this.population = populationGeneration(PolynomialDegree);
		
		CalcFitness(DataPoints, this.population);
		
		print(this.population);

		// selection
		tournamentSelection();
		// crossover
		Crossover(PolynomialDegree);
		// mutation
		mutation();
		// replace old population with offsprings
		Chromosome[] newGen = replacement(DataPoints);

		for (int i = 0; i < 4; i++) {
			System.out.println("****************** Generation " + (i + 2) + " ******************");
			// calculate fitness
			CalcFitness(DataPoints, newGen);
			
			print(newGen);
			// selection
			tournamentSelection();
			// crossover
			Crossover(PolynomialDegree);
			// mutation
			mutation();
			// replace old population with offsprings
			newGen = replacement(DataPoints);
		}
	}

	public Chromosome[] populationGeneration(int PolynomialDegree) {
		setT(1);
		cr = new Chromosome();
		this.population = new Chromosome[popSize];
		for (int i = 0; i < popSize; i++) {
			this.population[i] = new Chromosome();
		}
		for (int j = 0; j < popSize; j++) {

			this.population[j].genes = cr.geneGeneration(PolynomialDegree);
		}
		return this.population;
	}

	public void CalcFitness(DataPoint[] Datapoints, Chromosome[] Chromosomes) {

		for (int i = 0; i < popSize; i++) {

			getMeanSquareError(Datapoints, Chromosomes[i]);
			Chromosomes[i].setFitness(1 / (Chromosomes[i].getTotalError()));

		}

	}

	public void getMeanSquareError(DataPoint[] DataPoints, Chromosome coefficents) {
		float x = 0;
		float y = 0;
		float observed_value = 0;
		float error = 0;
		float sum = 0;
		float total_error = 0;

		for (int i = 0; i < DataPoints.length; i++) {
			observed_value = 0;
			x = DataPoints[i].getX();
			y = DataPoints[i].getY();

			for (int j = 0; j < coefficents.genes.length; j++) {

				observed_value += (coefficents.genes[j] * (Math.pow(x, j)));

			}
			float temp = observed_value - y;
			error = (float) Math.pow(temp, 2);
			// System.out.println(error);
			sum += error;
		}
		total_error = sum / DataPoints.length;
		coefficents.setTotalError(total_error);

	}

	Chromosome tournamentSelection() {
		int cNum = 2; // random chromosomes/comparisons number
		int min = 0;
		int max = popSize - 1;
		int random_int = 0;
		Chromosome Parent = new Chromosome();
		random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		Parent = this.population[random_int];
		Chromosome temp_Parent = new Chromosome();

		// since fitness is 1/error we need to get max fitness (that's our fittest
		// chromosome)
		//and since parent in already initialized it's like k=3
		for (int k = 0; k < cNum; k++) {
			random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
			temp_Parent = this.population[random_int];
			if (temp_Parent.fitness > Parent.fitness) {
				Parent = temp_Parent;
			}

		}
		// System.out.println(Parent.getNormalizedFitness());
		return Parent;

	}

	// getting 10 parents from the tournament selection function and storing them in
	// parents array in class
	// to use in crossover function
	Chromosome[] parentSelected() {
		this.Parents = new Chromosome[10]; // 10 parents ---> 5 pairs of parents
		for (int i = 0; i < 10; i++) {
			this.Parents[i] = tournamentSelection();
		}
		return this.Parents;
	}

	Chromosome[] Crossover(int PolynomialDegree) {

		this.Parents = parentSelected();
		Chromosome[] Offsprings = new Chromosome[this.Parents.length]; // initialization of genes length in offsprings

		for (int init = 0; init < this.Parents.length; init++) {
			Offsprings[init] = new Chromosome(PolynomialDegree);
		}

		int min = 1;
		int max = (this.Parents[0].genes.length - 1);
		int Xc1 = (int) Math.floor(Math.random() * (max - min + 1) + min); // random from parent gene length -1
		int Xc2 = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// Ensure crosspoints are different...
		if (Xc1 == Xc2) {

			Xc1--;

		}
		// .. and crosspoin 1 is lower than crosspoint2
		if (Xc2 < Xc1) {
			int temp = Xc1;
			Xc1 = Xc2;
			Xc2 = temp;
		}

		for (int i = 0; i < this.Parents.length; i += 2) {

			Random rand = new Random();
			double double_random = rand.nextDouble();

			if (double_random <= this.crossOverProbability) {

				for (int j = 0; j < Xc1; j++) {

					Offsprings[i].genes[j] = this.Parents[i].genes[j];
					Offsprings[i + 1].genes[j] = this.Parents[i + 1].genes[j];

				}
				for (int k = Xc1; k < Xc2; k++) {
					Offsprings[i].genes[k] = this.Parents[i + 1].genes[k];
					Offsprings[i + 1].genes[k] = this.Parents[i].genes[k];
				}
				for (int f = Xc2; f < this.Parents[i].genes.length; f++) {

					Offsprings[i].genes[f] = this.Parents[i].genes[f];
					Offsprings[i + 1].genes[f] = this.Parents[i + 1].genes[f];
				}

			}

			else {
				Offsprings[i] = this.Parents[i];
				Offsprings[i + 1] = this.Parents[i + 1];
			}

		}

		this.offsprings = Offsprings;
		return Offsprings;

	}

	Chromosome[] mutation() {

		for (int i = 0; i < this.offsprings.length; i++) {
			float L = 0;
			float U = 0;
			float y = 0;
			float N = 0;
			float b = 1; // parameter that controls degree of non-uniformality
			float XNew = 0;

			for (int j = 0; j < this.offsprings[i].genes.length; j++) {

				Random rand = new Random();
				double Rm = rand.nextDouble();

				if (Rm <= mutationProbability) {
					// calculating delta L
					L = this.offsprings[i].genes[j] - (-10);

					// calculating delta U
					U = 10 - this.offsprings[i].genes[j];

					double r1 = rand.nextDouble();

					if (r1 <= 0.5) {
						y = L;

					} else if (r1 > 0.5) {
						y = U;

					}
					// generate a random value to calculate the value of mutation at current
					// generation(t)
					double r2 = rand.nextDouble();

					N = (float) (y * (1 - Math.pow(r2, Math.pow(1 - getT() / T, b))));

					if (y == L) {
						XNew = this.offsprings[i].genes[j] - N;

					} else if (y == U) {
						XNew = this.offsprings[i].genes[j] + N;

					}

					this.offsprings[i].genes[j] = XNew;

				}

			}

		}

		return this.offsprings;

	}

	// Elitist replacement
	Chromosome[] replacement(DataPoint[] DataPoints) {

		Chromosome[] Pop = this.population;
		Chromosome[] Off = this.offsprings;

		CalcFitness(DataPoints, Off);

		int fal = Pop.length; // determines length of firstArray
		int sal = Off.length; // determines length of secondArray
		Chromosome[] Combine = new Chromosome[fal + sal]; // resultant array of size first array and second array
		System.arraycopy(Pop, 0, Combine, 0, fal);
		System.arraycopy(Off, 0, Combine, fal, sal);

		for (int i = 0; i < Combine.length; i++) {
			for (int j = 0; j < Combine.length - 1; j++) {
				if (Combine[j].fitness > Combine[j + 1].fitness) {
					Chromosome temp = Combine[j];
					Combine[j] = Combine[j + 1];
					Combine[j + 1] = temp;
				}
			}
		}

		// take from combination the best 10 fitness chromosomes
		Chromosome[] newOffSpring = new Chromosome[fal];
		int take = 10;
		for (int k = 0; k < fal; k++) {
			newOffSpring[k] = Combine[take];
			take++;
		}

		setT(t++);
		this.population = newOffSpring;
		return newOffSpring;

	}

	void print(Chromosome[] Chromosomes) {

		for (int j = 0; j < Chromosomes.length; j++) {
			System.out.println("Coefficents array num " + (j + 1) + " : " + Arrays.toString(Chromosomes[j].genes));
			System.out.println("its mean square error: " + Chromosomes[j].getTotalError());
			System.out.println("******************");

		}

	}

}
