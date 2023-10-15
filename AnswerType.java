import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum AnswerType {
	positive("yes", "yep", "ye"),
	negative("no", "nuh");
	private AnswerType(String... acceptableAnswers) {
		for (String string : acceptableAnswers) {
			if (string.toLowerCase() != string) {
				throw new IllegalArgumentException("answer not in lower case");
			}
		}
		this.acceptableAnswers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(acceptableAnswers)));
	}
	public boolean matches(String s) {
		return acceptableAnswers.contains(s.toLowerCase());
	}
	private Set<String> acceptableAnswers;

	public Answer createAnswer(Questions next) {
		return new Answer(this, next);
	}

	public class Answer {
		private AnswerType type;
		Questions next;
		private Answer(AnswerType type, Questions next) {
			this.type = type;
			this.next = next;
		}

		public Questions getNext() {
			return next;
		}

		public boolean matches(String val) {
			return type.matches(val);
		}

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
