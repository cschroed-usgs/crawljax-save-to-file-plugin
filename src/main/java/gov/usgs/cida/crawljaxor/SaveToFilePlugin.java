package gov.usgs.cida.crawljaxor;

import java.io.File;
import java.io.FileWriter;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import java.io.IOException;
import gov.usgs.cida.simplehash.SimpleHash;

public class SaveToFilePlugin implements OnNewStatePlugin {

	private HostInterface hostInterface;

	SaveToFilePlugin(HostInterface hostInterface) {
		this.hostInterface = hostInterface;
	}

	@Override
	public void onNewState(CrawlerContext context, StateVertex newState) {

		String dom = context.getBrowser().getStrippedDom();
		String url = context.getCurrentState().getUrl();

		String hashedUrl = SimpleHash.hash(url, "SHA-1");
		String fileName = hashedUrl + ".html";
		File parentDirs = hostInterface.getOutputDirectory();
		File file = new File(parentDirs, fileName);
		file.getParentFile().mkdirs();
		try (FileWriter fw = new FileWriter(file, false)) {
			fw.write(dom);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
