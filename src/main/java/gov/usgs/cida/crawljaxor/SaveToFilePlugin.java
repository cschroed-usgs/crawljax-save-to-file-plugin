package gov.usgs.cida.crawljaxor;

import java.io.File;
import java.io.FileWriter;

import com.crawljax.core.CrawlerContext;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class SaveToFilePlugin implements OnNewStatePlugin {

	private HostInterface hostInterface;
	private Set<String> visitedUrls = new HashSet<>();
	
	private synchronized void markVisited(String str){
		visitedUrls.add(str);
	}
	private synchronized boolean hasBeenVisited(String str){
		return visitedUrls.contains(str);
	}
	public SaveToFilePlugin(HostInterface hostInterface) {
		this.hostInterface = hostInterface;
	}
	private String appendIfNotNull(String original, String possiblyNull){
		String toReturn = original;
		if(null != possiblyNull){
			toReturn += possiblyNull;
		}
		return toReturn;
	}
	@Override
	public void onNewState(CrawlerContext context, StateVertex newState) {
		try {
			String dom = context.getBrowser().getStrippedDom();
			String url = context.getCurrentState().getUrl();
			if(hasBeenVisited(url)){
				System.out.println("Already visited '" + url +"'. Skipping.");
				return;
			} else {
				markVisited(url);
				URI uri = new URI(url);
				File parentDirs = hostInterface.getOutputDirectory();
				String fileName = "";

				fileName = appendIfNotNull(fileName, uri.getPath());
				fileName = appendIfNotNull(fileName, uri.getQuery());
				fileName += "#";
				fileName = appendIfNotNull(fileName, uri.getFragment());

				fileName += ".html";
				
				File file = new File(parentDirs, fileName);
				file.getParentFile().mkdirs();
				FileWriter fw = new FileWriter(file, false);
				fw.write(dom);
				fw.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
