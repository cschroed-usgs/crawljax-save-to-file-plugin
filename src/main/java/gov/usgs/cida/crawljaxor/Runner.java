package gov.usgs.cida.crawljaxor;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CandidateElement;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.HostInterfaceImpl;
import com.crawljax.core.plugin.descriptor.Parameter;
import com.crawljax.core.plugin.descriptor.PluginDescriptor;
import com.crawljax.core.state.StateVertex;
import com.crawljax.core.state.StateVertexFactory;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.w3c.dom.Document;

/**
 * Use the sample plugin in combination with Crawljax.
 */
public class Runner {

	private static final String URL = "http://cida-eros-wsdev.er.usgs.gov:8080/nwc/";
	private static final int MAX_DEPTH = 3;
	private static final int MAX_NUMBER_STATES = 100;
	private static final int NUM_BROWSERS = 3;
	/**
	 * Entry point
	 */

	public static void main(String[] args) {
		CrawljaxConfiguration.CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(URL);
		builder.crawlRules().insertRandomDataInInputForms(false);

		builder.crawlRules().click("a");

		builder.setMaximumStates(MAX_NUMBER_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		builder.setBrowserConfig(new BrowserConfiguration(EmbeddedBrowser.BrowserType.PHANTOMJS, NUM_BROWSERS));
		builder.setStateVertexFactory(new StateVertexFactory() {
			@Override
			public StateVertex newStateVertex(int id, String url, String name, String dom, String strippedDom) {
				return new StateVertexUrlEqualityImpl(id, url, name, dom, strippedDom);
			}
		});
		Map<String, String> parameters = new HashMap<>();
		File dir = new File("out");
		dir.mkdir();
		HostInterface hostInterface = new HostInterfaceImpl(dir, parameters);
		builder.addPlugin(new SaveToFilePlugin(hostInterface));

		CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
		crawljax.call();
	}
}
