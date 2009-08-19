package org.gdteam.appupdater4j.notification.feed;

import java.net.URL;

import junit.framework.Assert;

import org.gdteam.appupdater4j.model.Application;
import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSTest {

    @Test
    public void testRome() {
        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new XmlReader(new URL("http://www.rssboard.org/files/sample-rss-2.xml")));
            
            Assert.assertEquals("Liftoff News", feed.getTitle());
            Assert.assertEquals("http://liftoff.msfc.nasa.gov/", feed.getLink());
            
            SyndEntry secondItem = (SyndEntry) feed.getEntries().toArray()[1];
            
            Assert.assertEquals("30 May 2003 11:06:42 GMT", secondItem.getPublishedDate().toGMTString());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
    
    @Test
    public void basicTest() {
        try {
            Application app = RSSReader.getApplication(this.getClass().getClassLoader().getResource("basictestrss.xml"));
            
            Assert.assertEquals("myappid", app.getId());
            Assert.assertEquals("myappname", app.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
    }
}
