import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Runs through the questions.
 * 
 * @author Kyle Cooney
 * @see <a href=
 *      "https://docs.google.com/drawings/d/1X7MyCkhH1geZDMKc6ajS6VB9AqLTrfoT4XfD0IZrinY/edit">flow
 *      chart</a>
 * @see <a href=
 *      "https://drive.google.com/file/d/1eqbuXWzLDWZYpJzUqDuFEfw6P-cA6rmb/view?usp=sharing">Sample
 *      output runs by Cierra</a>
 * @see <a href=
 *      "https://drive.google.com/file/d/1E8sguLRAlIejXXm1VRYG4jQKBjKJEY0A/view?usp=sharing">Tests
 *      I wrote for this code</a> (will require JUnit 4 jars to be accessible to
 *      work)
 */
public class QuestionRunner implements AutoCloseable {
	private static final String errorMessage = "This QuestionRunner is closed.";
	private final Scanner input;
	private String name;
	private boolean closed;
	private StringBuilder printedText;

	/**
	 * Creates a QuestionRunner with an InputStream
	 * 
	 * @param stream the InputStream to use
	 * @throws NullPointerException if {@code stream} is null
	 */
	public QuestionRunner(InputStream stream) {
		input = new Scanner(Objects.requireNonNull(stream, "stream should not be null"));
		printedText = new StringBuilder();
		closed = false;
	}

	/**
	 * Gets the delimeter used by the internal scanner
	 * 
	 * @return the delimeter being used
	 */
	Pattern getDelimeter() {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		return input.delimiter();
	}

	/**
	 * Promps the user for their name and introduces them to the quiz
	 * 
	 * @throws IllegalStateException if this object is closed
	 */
	public void askName() {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		System.out.println("What is your name?");
		name = input.nextLine();
		System.out.printf("Hello, %s!%n", name);
		System.out.println("Welcome to the interest quiz!");
		System.out.println("Based on your interests, this quiz will reccomend an activity to join.");
	}

	/**
	 * Runs through all questions possible
	 * 
	 * @param startingQuestion the first question to ask
	 * @throws IllegalStateException if this object is closed
	 * @throws NullPointerException  if {@code startingQuestion} is null
	 */
	public void start(Questions startingQuestion) {
		if (closed) {
			throw new IllegalStateException(errorMessage);
		}
		Questions currentQuestion = Objects.requireNonNull(startingQuestion);
		boolean running = true;
		while (running) {
			System.out.println();
			System.out.println(currentQuestion.getPrompt());
			if (currentQuestion.hasAnswers()) {
				String response = input.nextLine();
				Optional<Questions> next = currentQuestion.getNext(response);
				if (next.isPresent()) {
					currentQuestion = next.get();
					continue;
				} else {
					System.out.println("Invalid answer");
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

	/**
	 * Gets the text printed to the standard output {@link PrintStream}
	 * 
	 * @return Returns a string containing all of the text this QuestionRunner has
	 *         printed
	 */
	public String getPrintedText() {
		return printedText.toString();
	}

	/**
	 * Closes this QuestionRunner.
	 * Calling any method that requires the given {@link InputStream}
	 * will throw an {@link IllegalStateException}
	 * 
	 * @throws IllegalStateException if this QuestionRunner is allready closed
	 * @see #isClosed()
	 */
	@Override
	public void close() {
		if (closed) {
			throw new IllegalStateException("This QuestionRunner is already closed");
		}
		closed = true;
		input.close();
		name = null;
	}

	/**
	 * Checks if this QuestionRunner is closed
	 * 
	 * @return {@code true} if this QuestionRunner is closed. Otherwise,
	 *         {@code false}
	 * @see #close()
	 */
	public boolean isClosed() {
		return closed;
	}
}
