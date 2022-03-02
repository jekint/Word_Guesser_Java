package word_guesser_java;

import java.io.*;
import java.util.*;

class Rock {
	public Integer rock;
	
	public Rock(Integer rock){
		this.rock = rock;
	}
	
	@Override
	public String toString() {
		return String.valueOf(rock);
	}
}

class Pebble {
	public Integer pebble;
	
	public Pebble(Integer pebble){
		this.pebble = pebble;
	}
	
	@Override
	public String toString() {
		return String.valueOf(pebble);
	}
}

public class Word_Guesser {
	
	private static final int MINWORDLEN = 4;
	private static final int MAXWORDLEN = 6;
	
	//Counts how many rocks and pebbles are the probe word and outputs a string that shows where each is located
	public static String countRocksandPebbles(String probe, String secret, Rock rocks, Pebble pebbles) {
		
		final char rock_block = 'R';
		final char pebble_block = 'P';
		StringBuilder probeCopy = new StringBuilder(probe);
		StringBuilder secretCopy = new StringBuilder(secret);
		
		//Counts number of rocks
		int minLen = (probeCopy.length() < secretCopy.length()) ? (probeCopy.length()) : (secretCopy.length());
		rocks.rock = 0;
		for (int i = 0; i < minLen; i++) {
			if ((probeCopy.charAt(i)) == secretCopy.charAt(i)) {
				rocks.rock++;
				probeCopy.setCharAt(i, rock_block);
				secretCopy.setCharAt(i, rock_block);
			}
		}
		
		//Count number of pebbles
		pebbles.pebble = 0;
		for (int i = 0; i < probeCopy.length(); i++) {
			if (probeCopy.charAt(i) == rock_block) {
				continue;
			} else {
				for (int j = 0; j < secretCopy.length(); j++) {
					if (probeCopy.charAt(i) == secretCopy.charAt(j)) {
						pebbles.pebble++;
						probeCopy.setCharAt(i, pebble_block);
						secretCopy.setCharAt(j, pebble_block);
						break;
					}
				}
			}
		}
		
		//Return the location of the rocks and pebbles
		for (int i = 0; i < probeCopy.length(); i++) {
			if (probeCopy.charAt(i) != rock_block && probeCopy.charAt(i) != pebble_block) {
				probeCopy.setCharAt(i, ' ');
			}
		}
		return probeCopy.toString();
	}
	
	//Plays one round of the game
	public static int manageOneRound(List<String> words, String secret, Scanner input) {
		
		String probeWord = "";
		int nTries = 0;
		Rock rocks = new Rock(0);
		Pebble pebbles = new Pebble(0);
		String location = "";
		
		//Outputs number of rocks and pebbles for each probe word and continually asks for user input until the secret word is found
		while (true) {
			System.out.println("Probe word: ");
			probeWord = input.nextLine();
			probeWord = probeWord.toLowerCase();
			if (probeWord.length() < MINWORDLEN || probeWord.length() > MAXWORDLEN) {
				System.out.println("Your probe word must be a word of " + MINWORDLEN + " to " + MAXWORDLEN + " letters.");
				continue;
			}
			boolean inList = false;
			for (String word : words) {
				if(probeWord.equals(word)) {
					inList = true;
				}
			}
			if (!inList) {
				System.out.println("I don't know that word.");
				System.out.println();
				continue;
			}
			nTries++;
			if (probeWord.equals(secret)) {
				return nTries;
			}
			location = countRocksandPebbles(probeWord, secret, rocks, pebbles);
			System.out.println(location);
			System.out.println("Rocks: " + rocks + ", Pebbles: " + pebbles);
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		
		List<String> wordList = new LinkedList<>();
		
		//Read in all the words from the provided text file into a list
		File file = new File("C:/Users/Jekin Tilva/Desktop/Coding Projects/Java/word_guesser_java/words.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.length() >= MINWORDLEN && line.length() <= MAXWORDLEN)
					wordList.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file " + file.toString());
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Unable to read file " + file.toString());
			System.exit(0);
		}
		
		if (wordList.isEmpty()) {
			System.out.println("No words were loaded, so the game can't be played.");
			System.exit(0);
		}
		
		//Get the number of rounds from user input
		int numRounds = 0;
		Scanner input = new Scanner(System.in);
		do {
			try {
				System.out.println("How many rounds do you want to play? ");
				numRounds = input.nextInt();
				input.nextLine();
				if (numRounds <= 0) {
					System.out.println("Please enter a positive integer for the number of rounds.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter a positive integer for the number of rounds.");
				input.next();
				continue;
			}
		} while (numRounds <= 0);
		
		//Initialize variables for getting the secret word and for the number of tries
		Random rand = new Random();
		String secretWord = "";
		int numTries = 0;
		int totalTries = 0;
		int minTries = 0;
		int maxTries = 0;
		double average = 0.0;
		
		//Start each round
		for (int i = 1; i <= numRounds; i++) {
			System.out.println("===========================================================================");
			System.out.println("Round " + i);
			secretWord = wordList.get(rand.nextInt(wordList.size()));
			System.out.println("The secret word is " + secretWord.length() + " letters long.");
			numTries = manageOneRound(wordList, secretWord, input);
			System.out.print("You got it in " + numTries);
			if (numTries == 1) {
				System.out.println(" try.");
			} else {
				System.out.println(" tries.");
			}
			if (i == 1) {
				minTries = numTries;
				maxTries = numTries;
			} else if (numTries < minTries) {
				minTries = numTries;
			} else if (numTries > maxTries) {
				maxTries = numTries;
			}
			totalTries += numTries;
			average = (double) totalTries / i;
			System.out.printf("Average: %.2f, minimum: %d, maximum: %d", average, minTries, maxTries);
			System.out.println();
		}
		

	}
	
}
