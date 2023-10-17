/**
 * Initializes the code
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
public class MainRuntime {
	public static void main(String[] args) {
		try (QuestionRunner runner = new QuestionRunner(System.in)) {
			runner.askName();
			runner.start(Questions.START);
		}
	}
}