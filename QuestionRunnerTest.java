import static org.junit.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class QuestionRunnerTest {
	@Parameters(name= "{index}: {1} -> {0}") 
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "yes\nwires", Questions.START,
						Arrays.asList(Questions.START, Questions.HW_QUESTION, Questions.HW_ELECTRICAL) },
				{ "dEsIgn", Questions.HW_QUESTION, Arrays.asList(Questions.HW_QUESTION, Questions.HW_DESIGN) },
				{ "other", Questions.HW_QUESTION, Arrays.asList(Questions.HW_QUESTION, Questions.CHAM) },
				{ "pArTs", Questions.HW_QUESTION, Arrays.asList(Questions.HW_QUESTION, Questions.HW_FABRICATION) },
				//
				{ "no\nyes\nyes\nyes", Questions.START,
						Arrays.asList(Questions.START, Questions.BUS_QUESTION, Questions.BUS_AWARDS_QUESTION_1,
								Questions.BUS_OUTREACH_QUESTION, Questions.BUS_OUTREACH) }
		});
	}

	public void setPrintStream() {
		//
	}

	public OutputStream stream;
	@Parameter(0)
	public String input;
	@Parameter(1)
	public Questions startingQuestion;
	@Parameter(2)
	public List<Questions> expectedQuestions;

	public String printedValues() {
		StringBuilder builder = new StringBuilder();
		for (Questions q : expectedQuestions) {
			builder.append("\n");
			builder.append(q.getPrompt());
			builder.append("\n");
		}
		return builder.toString();
	}

	@Test
	public void expectedOutputTest() {
		try (InputStream stream = new ByteArrayInputStream(input.getBytes());
				QuestionRunner runner = new QuestionRunner(stream)) {
			assumeTrue(runner.getDelimeter().matcher("\n").matches());
			runner.start(startingQuestion);
			assertEquals(printedValues(), runner.getPrintedText());
		} catch (IOException e) {
			throw new RuntimeException("Failure on closing stream", e);
		}
	}
}
