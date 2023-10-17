import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class QuestionRunnerTest {
	@Parameters(name = "{index}: {1} -> {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "no\nyes\nyes\nyes", Questions.START,
						Arrays.asList(
								Questions.START,
								Questions.BUS_QUESTION,
								Questions.BUS_AWARDS_QUESTION_1,
								Questions.BUS_OUTREACH_QUESTION,
								Questions.BUS_OUTREACH) },
				{ "no\nyes", Questions.BUS_OUTREACH_QUESTION,
						Arrays.asList(
								Questions.BUS_OUTREACH_QUESTION,
								Questions.BUS_AWARDS_QUESTION_2,
								Questions.BUS_AWARDS) },
				{ "no\nno\nyes", Questions.BUS_AWARDS_QUESTION_1,
						Arrays.asList(
								Questions.BUS_AWARDS_QUESTION_1,
								Questions.BUS_FINANCE_QUESTION,
								Questions.BUS_AWARDS_QUESTION_2,
								Questions.BUS_AWARDS) },
				{ "yes\nno\nnope\nno", Questions.START,
						Arrays.asList(
								Questions.START,
								Questions.HW_ELECTRICAL_QUESTION,
								Questions.HW_FABRICATION_QUESTION,
								Questions.HW_DESIGN_QUESTION,
								Questions.CHAM) },
				{ "yes", Questions.HW_ELECTRICAL_QUESTION,
						Arrays.asList(
								Questions.HW_ELECTRICAL_QUESTION,
								Questions.HW_ELECTRICAL) },
				{ "yes", Questions.HW_FABRICATION_QUESTION,
						Arrays.asList(
								Questions.HW_FABRICATION_QUESTION,
								Questions.HW_FABRICATION) },
				{ "yes", Questions.HW_DESIGN_QUESTION,
						Arrays.asList(
								Questions.HW_DESIGN_QUESTION,
								Questions.HW_DESIGN) }
		});
	}
	
	private ByteArrayOutputStream outputStream;
	private PrintStream previousPrint;
	@Before
	public void initializePrintStream() {
		previousPrint = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}
	@After
	public void closeStream() throws IOException {
		outputStream.close();
		System.setOut(previousPrint);
	}
	@Parameter(0)
	public String input;
	@Parameter(1)
	public Questions startingQuestion;
	@Parameter(2)
	public List<Questions> expectedQuestions;

	public String printedValues() {
		StringBuilder builder = new StringBuilder();
		for (Questions q : expectedQuestions) {
			builder.append(System.lineSeparator());
			builder.append(q.getPrompt());
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	@Test
	public void expectedOutputTest() {
		assumeFalse(input.contains("\r"));
		try (InputStream inputStream = new ByteArrayInputStream(input.getBytes());
				QuestionRunner runner = new QuestionRunner(inputStream)) {
			assumeTrue(runner.getDelimeter().matcher("\n").matches());
			runner.start(startingQuestion);
			assertEquals(printedValues(), outputStream.toString());
		} catch (IOException e) {
			throw new RuntimeException("Failure on closing stream", e);
		}
	}
}
