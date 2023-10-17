import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The types of answers that can be responded with.
 * Multiple words may correspond to the same AnswerType
 * 
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
public enum AnswerType {
	/**
	 * A positive response to a question
	 */
	positive("yes", "yep", "ye", "yes!", "yep!", "ye!"),
	/**
	 * A negative response to a question
	 */
	negative("no", "nuh", "nope", "no!", "nuh!", "nope!");

	private AnswerType(String... acceptableAnswers) {
		for (String string : acceptableAnswers) {
			if (string.toLowerCase() != string) {
				throw new IllegalArgumentException(String.format("answer %s not in lower case", string));
			}
		}
		this.acceptableAnswers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(acceptableAnswers)));
	}

	private boolean matches(String s) {
		return acceptableAnswers.contains(s.toLowerCase());
	}

	private final Set<String> acceptableAnswers;

	/**
	 * Creates an Answer with the given Question to go to next
	 * @param next the question to go to next
	 * @return the Answer generated
	 * @see Answer
	 */
	public Answer createAnswer(Questions next) {
		return new Answer(this, next);
	}

	/**
	 * Represents a way to answer a {@link Questions}
	 * @see Questions
	 * @see AnswerType#createAnswer(Questions)
	 */
	public static class Answer {
		private AnswerType type;
		private Questions next;

		private Answer(AnswerType type, Questions next) {
			this.type = type;
			this.next = next;
		}

		/**
		 * Gets the next question
		 * @return the next question
		 * @see #matches(String)
		 */
		public Questions getNext() {
			return next;
		}

		/**
		 * Checks if a string has a match with the given {@link AnswerType}
		 * @param val the string to check against
		 * @return {@code true} if there is a match
		 */
		public boolean matches(String val) {
			return type.matches(val);
		}

		/**
		 * Checks if an object is equivilent to this one.
		 * An Answer is equivilent to this one if:
		 * they have the same {@link AnswerType}, and they
		 * have the same {@link Questions Question} next.
		 */
		@Override
		public boolean equals(Object o) {
			if (o instanceof Answer) {
				Answer other = (Answer) o;
				return this.type.equals(other.type) && this.next.equals(other.next);
			}
			return false;
		}
	}
}
