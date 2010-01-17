package org.gdteam.appupdater4j.download;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FileDownloadTest {

    @Test
    public void testDownload() {
        
        File dest = new File(System.getProperty("java.io.tmpdir"), "downloadtest.xml");
        dest.deleteOnExit();
        
        try {
            
            FileDownload download = new FileDownload(new URL("http://www.rssboard.org/files/sample-rss-2.xml"), dest);
            download.performDownload();
            
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(dest));
            
            Assert.assertEquals("Liftoff News", feed.getTitle());
            Assert.assertEquals("http://liftoff.msfc.nasa.gov/", feed.getLink());
            
            SyndEntry secondItem = (SyndEntry) feed.getEntries().toArray()[1];
            
            Assert.assertEquals("30 May 2003 11:06:42 GMT", secondItem.getPublishedDate().toGMTString());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
        
    }
    
    
}
