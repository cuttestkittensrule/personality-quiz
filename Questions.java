import java.util.Objects;
import java.util.Optional;

/**
 * Represents a question to be asked to the user, or text to be printed onto the
 * screen.
 * 
 * @author Kyle Cooney
 */
public enum Questions {
	HW_FABRICATION("Join the robotics hardware: fabrication subteam!"),
	HW_DESIGN("Join the robotics hardware: design subteam!"),
	HW_ELECTRICAL("Join the robotics hardware: electrical subteam!"),
	CHAM("Become the next super-rookie and join every Robotics subteam!"),
	BUS_OUTREACH("Join the robotics buisness: outreach subteam!"),
	BUS_AWARDS("Join the robotics buisness: awards team!"),
	SW_ROBOT("Join the robotics software: robot subteam!"),
	SW_STRATEGY("Join the robotics software: strategy subteam!"),
	BUS_MEDIA("Join the robotics business: media subteam!"),
	BUS_FINANCE("Join the robotics business: finance subteam"),
	HW_DESIGN_QUESTION(
			"Do you like designing things? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(HW_DESIGN),
			AnswerType.negative.createAnswer(CHAM)),
	HW_FABRICATION_QUESTION(
			"Do you like manufacturing parts? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(HW_FABRICATION),
			AnswerType.negative.createAnswer(HW_DESIGN_QUESTION)),
	HW_ELECTRICAL_QUESTION(
			"Do you like working with electricity? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(HW_ELECTRICAL),
			AnswerType.negative.createAnswer(HW_FABRICATION_QUESTION)),
	SW_SUBTEAM_QUESTION(
			"Do you like designing applications? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(SW_STRATEGY),
			AnswerType.negative.createAnswer(SW_ROBOT)),
	BUS_MEDIA_QUESTION(
			"Do you like recording or editing videos? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_MEDIA),
			AnswerType.negative.createAnswer(CHAM)),
	SW_QUESTION(
			"Do you like writing software? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(SW_SUBTEAM_QUESTION),
			AnswerType.negative.createAnswer(BUS_MEDIA_QUESTION)),
	BUS_AWARDS_QUESTION_2(
			"Do you like writing? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_AWARDS),
			AnswerType.negative.createAnswer(SW_QUESTION)),
	BUS_FINANCE_QUESTION(
			"Do you like handling money? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_FINANCE),
			AnswerType.negative.createAnswer(BUS_AWARDS_QUESTION_2)),
	BUS_OUTREACH_QUESTION(
			"Do you like convincing people to do what you are interested in? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_OUTREACH),
			AnswerType.negative.createAnswer(BUS_AWARDS_QUESTION_2)),
	BUS_AWARDS_QUESTION_1(
			"Do you like creating presentations? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_OUTREACH_QUESTION),
			AnswerType.negative.createAnswer(BUS_FINANCE_QUESTION)),
	BUS_QUESTION(
			"Do you like talking to people? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(BUS_AWARDS_QUESTION_1),
			AnswerType.negative.createAnswer(BUS_FINANCE_QUESTION)),
	START(
			"Do you like building things? (\"yes\" or \"no\")",
			AnswerType.positive.createAnswer(HW_ELECTRICAL_QUESTION),
			AnswerType.negative.createAnswer(BUS_QUESTION));

	private final String prompt;
	private final AnswerType.Answer[] answers;
	private final Optional<Questions> next;

	/**
	 * Creates a Question with a prompt and any number of Answers <strong>including
	 * zero</strong>
	 * 
	 * @param prompt  The prompt to be printed to the screen
	 * @param answers The responses that this prompt is expecting
	 * @throws NullPointerException if either {@code prompt} or {@code answers} are
	 *                              null
	 * @apiNote having no answers inputted will result in the prompt being printed,
	 *          and the program is exited.
	 */
	private Questions(String prompt, AnswerType.Answer... answers) {
		this.prompt = Objects.requireNonNull(prompt, "prompt should not be null");
		this.answers = Objects.requireNonNull(answers, "answers should not be null");
		for (int i = 0; i < answers.length; i++) {
			Objects.requireNonNull(answers[i], String.format("Answer %d should not be null", i));
			Objects.requireNonNull(answers[i].getNext(), String.format("Answer %d's next should not be null", i));
		}
		this.next = Optional.empty();
	}

	/**
	 * Creates a Question with a promt and
	 * 
	 * @param prompt The text to be printed onto the screen
	 * @param next   The question to ask after the text is printed
	 * @apiNote having {@code next} be null is allowed, and is equivilent to calling
	 *          {@link #Question(String, Answer...)} with no answers.
	 */
	private Questions(String prompt, Questions next) {
		this.prompt = Objects.requireNonNull(prompt, "prompt should not be null");
		this.answers = new AnswerType.Answer[0];
		this.next = Optional.ofNullable(next);
	}

	/**
	 * Gets the next question given an answer.
	 * 
	 * @param stringAnswer The answer to the question
	 * @return an {@link Optional} of the next question, or {@link Optional#empty()}
	 *         if there is none.
	 */
	public Optional<Questions> getNext(String stringAnswer) {
		stringAnswer = Objects.requireNonNull(stringAnswer, "stringAnswer should not be null").toLowerCase();
		for (AnswerType.Answer answer : answers) {
			if (answer.matches(stringAnswer)) {
				return Optional.of(answer.getNext());
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the next question this question will always go to
	 * 
	 * @return the next question
	 * @throws IllegalStateException if there is not a next question
	 * @see #hasNext() check if there is a next question
	 */
	public Questions getNextQuestion() {
		if (next.isPresent()) {
			return next.get();
		} else {
			throw new IllegalStateException("There is no next question");
		}

	}

	/**
	 * Checks if this Question has Answers for it.
	 * 
	 * @return {@code true} if there are answers for this question
	 */
	public boolean hasAnswers() {
		return answers.length > 0;
	}

	/**
	 * Checks if there is a Question to always go to after this
	 * 
	 * @return {@code true} if there is a Question to go to
	 */
	public boolean hasNext() {
		return next.isPresent();
	}

	/**
	 * Gets the string representation of the prompt
	 * 
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}
}
