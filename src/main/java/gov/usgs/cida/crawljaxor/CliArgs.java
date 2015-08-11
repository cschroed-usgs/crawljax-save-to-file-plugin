package gov.usgs.cida.crawljaxor;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CliArgs {

	@Parameter
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = "-url", description = "Root URL from which to start crawling", required = true)
	private String url;
	
	@Parameter(names = "-output-dir", description = "path to put the html files after they have been processed by a headless browser")
	private File outputDir;
	
	@Parameter(names = "-max-depth", description = "Max depth of the crawler's traversal tree")
	private int maxDepth = 3;

	@Parameter(names = "-max-number-of-states", description = "Maximum number of states that the crawler will visit")
	private int maxNumberOfStates = 100000;
	
	@Parameter(names = "-number-of-browsers", description = "Number of concurrent headless browsers used to crawl")
	private int numberOfBrowsers = 3;
	/**
	 * @return the parameters
	 */
	public List<String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @param maxDepth the maxDepth to set
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * @return the maxNumberOfStates
	 */
	public int getMaxNumberOfStates() {
		return maxNumberOfStates;
	}

	/**
	 * @param maxNumberOfStates the maxNumberOfStates to set
	 */
	public void setMaxNumberOfStates(int maxNumberOfStates) {
		this.maxNumberOfStates = maxNumberOfStates;
	}

	/**
	 * @return the numberOfBrowsers
	 */
	public int getNumberOfBrowsers() {
		return numberOfBrowsers;
	}

	/**
	 * @param numberOfBrowsers the numberOfBrowsers to set
	 */
	public void setNumberOfBrowsers(int numberOfBrowsers) {
		this.numberOfBrowsers = numberOfBrowsers;
	}

	/**
	 * @return the outputDir
	 */
	public File getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir the outputDir to set
	 */
	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

}