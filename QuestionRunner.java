import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Runs through the questions.
 * @author Kyle Cooney
 */
public class QuestionRunner implements AutoCloseable {
	private static final String errorMessage = "This QuestionRunner is closed.";
	private final Scanner input;
	private String name;
	private boolean closed;
	private StringBuilder printedText;
	/**
	 * Creates a QuestionRunner with an InputStream
	 * @param stream the InputStream to use
	 * @throws NullPointerException if {@code stream} is null
	 */
	public QuestionRunner(InputStream stream) {
		input = new Scanner(Objects.requireNonNull(stream,"stream should not be null"));
		printedText = new StringBuilder();
		closed = false;
	}

	private void println(String message) {
		System.out.println(message);
		printedText.append(message);
		printedText.append("\n");
	}

	private void print() {
		println("");
	}

	private void printf(String format, Object... args) {
		String message = String.format(format, args);
		println(message);
	}

	/**
	 * Gets the delimeter used by the internal scanner
	 * @return the delimeter being used
	 */
	public Pattern getDelimeter() {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		return input.delimiter();
	}

	/**
	 * Promps the user for their name and introduces them to the quiz
	 * @throws IllegalStateException if this object is closed
	 */
	public void askName() {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		println("What is your name?");
		name = input.nextLine();
		printf("Hello, %s!%n", name);
		println("Welcome to the interest quiz!");
		println("Based on your interests, this quiz will reccomend an activity to join.");
	}

	/**
	 * Runs through all questions possible
	 * @param startingQuestion the first question to ask
	 * @throws IllegalStateException if this object is closed
	 * @throws NullPointerException if {@code startingQuestion} is null
	 */
	public void start(Questions startingQuestion) {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		Questions currentQuestion = Objects.requireNonNull(startingQuestion);
		boolean running = true;
		while (running) {
			print();
			println(currentQuestion.getPrompt());
			if (currentQuestion.hasAnswers()) {
				String response = input.nextLine();
				Optional<Questions> next = currentQuestion.getNext(response);
				if (next.isPresent()) {
					currentQuestion = next.get();
					continue;
				} else {
					println("Invalid answer");
					running = false;
				}
			} else if (currentQuestion.hasNext()) {
				currentQuestion = currentQuestion.getNextQuestion();
				continue;
			} else {
				running = false;
			}
			
		}
	}

	public String getPrintedText() {
		return printedText.toString();
	}

	@Override
	public void close() {
		if (closed) {
			throw new IllegalStateException("This QuestionRunner is already closed");
		}
		closed = true;
		input.close();
		name = null;
	}
}
