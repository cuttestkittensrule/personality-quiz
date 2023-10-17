import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
	public void expectedOutputTestLF() {
		try (InputStream stream = new ByteArrayInputStream(input.getBytes());
				QuestionRunner runner = new QuestionRunner(stream)) {
			assumeTrue(runner.getDelimeter().matcher("\n").matches());
			runner.start(startingQuestion);
			assertEquals(printedValues(), runner.getPrintedText());
		} catch (IOException e) {
			throw new RuntimeException("Failure on closing stream", e);
		}
	}

	@Test
	public void expectedOutputTestCRLF() {
		String newInput = input.replace("\n", "\r\n");
		try (InputStream stream = new ByteArrayInputStream(newInput.getBytes());
				QuestionRunner runner = new QuestionRunner(stream)) {
			assumeTrue(runner.getDelimeter().matcher("\r\n").matches());
			assumeTrue(newInput.contains("\r\n"));
			runner.start(startingQuestion);
			assertEquals(printedValues(), runner.getPrintedText());
		} catch (IOException e) {
			throw new RuntimeException("Failure on closing stream", e);
		}
	}

	@Test
	public void expectedOutputTestCR() {
		String newInput = input.replace("\n", "\r");
		try (InputStream stream = new ByteArrayInputStream(newInput.getBytes());
				QuestionRunner runner = new QuestionRunner(stream)) {
			assumeTrue(runner.getDelimeter().matcher("\r").matches());
			assumeTrue(newInput.contains("\r"));
			runner.start(startingQuestion);
			assertEquals(printedValues(), runner.getPrintedText());
		} catch (IOException e) {
			throw new RuntimeException("Failure on closing stream", e);
		}
	}
}
