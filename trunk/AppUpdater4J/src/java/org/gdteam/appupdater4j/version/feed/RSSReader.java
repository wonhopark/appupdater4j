package org.gdteam.appupdater4j.version.feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.gdteam.appupdater4j.model.Application;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.jdom.Element;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSReader {
    
    public static final String APP_ID = "au:id";
    public static final String APP_NAME = "au:name";
    
    public static final String VERSION_MAJOR = "au:major";
    public static final String VERSION_MINOR = "au:minor";
    public static final String VERSION_BUILD = "au:build";
    public static final String VERSION_REVISION = "au:revision";
    public static final String VERSION_REBOOT = "au:reboot";
    public static final String VERSION_DESCRIPTION = "au:description";

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
            if (element.getQualifiedName().equals(APP_ID)) {
                ret.setId(element.getValue());
            } else if (element.getQualifiedName().equals(APP_NAME)) {
                ret.setName(element.getValue());
            }
        }
        
        for (SyndEntry entry : ((List<SyndEntry>) feed.getEntries())) {
            
            ApplicationVersion version = new ApplicationVersion();
            version.setName(entry.getTitle());
            version.setPublicationDate(entry.getPublishedDate());
            version.setUpdateURL(entry.getLink());
            
            List<Element> itemForeignMarkup = (List<Element>) entry.getForeignMarkup();
            for (Element element : itemForeignMarkup) {
                if (element.getQualifiedName().equals(VERSION_MAJOR)) {
                    version.setMajor(element.getValue());
                } else if (element.getQualifiedName().equals(VERSION_MINOR)) {
                    version.setMinor(element.getValue());
                } else if (element.getQualifiedName().equals(VERSION_BUILD)) {
                    version.setBuild(element.getValue());
                } else if (element.getQualifiedName().equals(VERSION_REVISION)) {
                    version.setRevision(element.getValue());
                } else if (element.getQualifiedName().equals(VERSION_REBOOT)) {
                    version.setNeedReboot(Boolean.valueOf(element.getValue()));
                } else if (element.getQualifiedName().equals(VERSION_DESCRIPTION)) {
                    version.putDescription(new Locale(element.getAttributeValue("lang")), element.getValue());
                }
            }
            
            ret.addVersion(version);
            
        }
        
        
        return ret;
    }
}
