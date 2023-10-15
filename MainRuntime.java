/**
 * Initializes the code
 * @author Kyle Cooney
 * @see <a href="https://docs.google.com/drawings/d/1X7MyCkhH1geZDMKc6ajS6VB9AqLTrfoT4XfD0IZrinY/edit">flow chart</a> 
 * @see <a href="https://google.com">Sample output run by Count Spatula NOT DONE YET</a>
 */
public class MainRuntime {
	public static void main(String[] args) {
		try (QuestionRunner runner = new QuestionRunner(System.in)) {
			runner.askName();
			runner.start(Questions.START);
		}
	}
}