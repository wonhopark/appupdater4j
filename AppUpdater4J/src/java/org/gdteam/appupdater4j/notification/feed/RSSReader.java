package org.gdteam.appupdater4j.notification.feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.gdteam.appupdater4j.model.Application;
import org.jdom.Element;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSReader {
    
    public static final String CHANNEL_ID = "au:id";
    public static final String CHANNEL_NAME = "au:name";

    /**
     * Get org.gdteam.appupdater4j.model.Application representation
     * @param rssfeedURL
     * @return Application
     * @throws MalformedURLException
     * @throws RssParserException
     * @throws IOException
     * @throws FeedException 
     * @throws IllegalArgumentException 
     */
    public static Application getApplication(URL rssfeedURL) throws IOException, IllegalArgumentException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(rssfeedURL));
        
        Application ret = new Application();
        
        List<Element> channelForeignMarkup = (List<Element>) feed.getForeignMarkup();
        for (Element element : channelForeignMarkup) {
            if (element.getQualifiedName().equals(CHANNEL_ID)) {
                ret.setId(element.getValue());
            } else if (element.getQualifiedName().equals(CHANNEL_NAME)) {
                ret.setName(element.getValue());
            }
        }
        
        return ret;
    }
}
