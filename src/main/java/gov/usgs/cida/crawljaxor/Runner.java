package gov.usgs.cida.crawljaxor;

import com.beust.jcommander.JCommander;
import com.crawljax.browser.EmbeddedBrowser;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.HostInterfaceImpl;
import com.crawljax.core.state.StateVertex;
import com.crawljax.core.state.StateVertexFactory;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * Use the plugin in combination with Crawljax.
 */
public class Runner {

	/**
	 * Entry point
	 */

	public static void main(String[] args) throws IOException {
		CliArgs cliArgs = new CliArgs();
		new JCommander(cliArgs, args);
		CrawljaxConfiguration config = getConfig(cliArgs);
		CrawljaxRunner crawljax = new CrawljaxRunner(config);
		crawljax.call();
	}
	
	private static File createCleanDir(String dirName) throws IOException{
		File dir = new File(dirName);
		//clear existing dir
		FileUtils.deleteDirectory(dir);
		//re-create
		dir.mkdirs();
		return dir;
	}
	
	private static CrawljaxConfiguration getConfig(CliArgs cliArgs) throws IOException{
		String url = cliArgs.getUrl();
		int maxDepth = cliArgs.getMaxDepth();
		int maxNumberOfStates = cliArgs.getMaxNumberOfStates();
		int numberOfBrowsers = cliArgs.getNumberOfBrowsers();
		
		CrawljaxConfiguration.CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		builder.crawlRules().insertRandomDataInInputForms(false);

		builder.crawlRules().click("a");

		builder.setMaximumStates(maxNumberOfStates);
		builder.setMaximumDepth(maxDepth);
		builder.setBrowserConfig(new BrowserConfiguration(EmbeddedBrowser.BrowserType.PHANTOMJS, numberOfBrowsers));

		//override default factory so that states are only compared based on url
		builder.setStateVertexFactory(new StateVertexFactory() {
			@Override
			public StateVertex newStateVertex(int id, String url, String name, String dom, String strippedDom) {
				return new StateVertexUrlEqualityImpl(id, url, name, dom, strippedDom);
			}
		});
		
		String outputDir = cliArgs.getOutputDir();
		File dir = createCleanDir(outputDir);
		Map<String, String> parameters = new HashMap<>();
		HostInterface hostInterface = new HostInterfaceImpl(dir, parameters);
		builder.addPlugin(new SaveToFilePlugin(hostInterface));
		
		return builder.build();
	}
}
