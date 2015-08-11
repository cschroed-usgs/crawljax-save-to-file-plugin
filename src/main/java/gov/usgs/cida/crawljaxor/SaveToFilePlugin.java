package gov.usgs.cida.crawljaxor;

import java.io.File;
import java.io.FileWriter;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import java.io.IOException;

public class SaveToFilePlugin implements OnNewStatePlugin {

	private HostInterface hostInterface;

	SaveToFilePlugin(HostInterface hostInterface) {
		this.hostInterface = hostInterface;
	}

	@Override
	public void onNewState(CrawlerContext context, StateVertex newState) {

		String dom = context.getBrowser().getStrippedDom();
		String url = context.getCurrentState().getUrl();

			//Native Java String Hash Code is stable between versions
		//as of v1.2 according to: http://stackoverflow.com/a/785150
		String fileName = url.hashCode() + ".html";
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
