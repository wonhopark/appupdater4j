package org.gdteam.appupdater4j.version.feed;

import java.net.URL;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

import org.gdteam.appupdater4j.model.Application;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.version.feed.RSSReader;
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
    public void testBasic() {
        try {
            Application app = RSSReader.getApplication(this.getClass().getClassLoader().getResource("basictestrss.xml"));
            
            Assert.assertEquals("myappid", app.getId());
            Assert.assertEquals("myappname", app.getName());
            
            Locale fr = new Locale("fr");
            Locale en = new Locale("en");
            
            //Firt
            ApplicationVersion firstVersion = app.getVersions().get(0);
            Assert.assertEquals("Version 0.0.4", firstVersion.getName());
            Assert.assertEquals(new Date("19 Jul 2005 04:32:51 -0700").toString(), firstVersion.getPublicationDate().toString());
            Assert.assertEquals("http://code.google.com/p/appupdater4j", firstVersion.getUpdateURL());
            Assert.assertEquals("0", firstVersion.getMajor());
            Assert.assertEquals("0", firstVersion.getMinor());
            Assert.assertEquals("4", firstVersion.getBuild());
            Assert.assertNull(firstVersion.getRevision());
            Assert.assertEquals("Version initiale", firstVersion.getDescription(fr));
            Assert.assertEquals("Initial version", firstVersion.getDescription(en));
            
            //Second
            ApplicationVersion secondVersion = app.getVersions().get(1);
            Assert.assertEquals("Version 0.0.5", secondVersion.getName());
            Assert.assertEquals(new Date("19 Aug 2005 04:32:51 -0700").toString(), secondVersion.getPublicationDate().toString());
            Assert.assertEquals("http://code.google.com/p/appupdater4j", secondVersion.getUpdateURL());
            Assert.assertEquals("0", secondVersion.getMajor());
            Assert.assertEquals("0", secondVersion.getMinor());
            Assert.assertEquals("5", secondVersion.getBuild());
            Assert.assertNull(secondVersion.getRevision());
            Assert.assertEquals("Seconde version", secondVersion.getDescription(fr));
            Assert.assertEquals("Second version", secondVersion.getDescription(en));
            
            //Third
            ApplicationVersion thirdVersion = app.getVersions().get(2);
            Assert.assertEquals("Version 0.0.5.1", thirdVersion.getName());
            Assert.assertEquals(new Date("22 Aug 2005 04:32:51 -0700").toString(), thirdVersion.getPublicationDate().toString());
            Assert.assertEquals("http://code.google.com/p/appupdater4j", thirdVersion.getUpdateURL());
            Assert.assertEquals("0", thirdVersion.getMajor());
            Assert.assertEquals("0", thirdVersion.getMinor());
            Assert.assertEquals("5", thirdVersion.getBuild());
            Assert.assertEquals("1", thirdVersion.getRevision());
            Assert.assertEquals("Troisieme version", thirdVersion.getDescription(fr));
            Assert.assertEquals("Third version", thirdVersion.getDescription(en));
            
            //Official
            ApplicationVersion officialVersion = app.getVersions().get(3);
            Assert.assertEquals("Version 1.0.0", officialVersion.getName());
            Assert.assertEquals(new Date("14 Dec 2005 04:32:51 -0700").toString(), officialVersion.getPublicationDate().toString());
            Assert.assertEquals("http://code.google.com/p/appupdater4j", officialVersion.getUpdateURL());
            Assert.assertEquals("1", officialVersion.getMajor());
            Assert.assertEquals("0", officialVersion.getMinor());
            Assert.assertEquals("0", officialVersion.getBuild());
            Assert.assertNull(officialVersion.getRevision());
            Assert.assertEquals("Version officielle", officialVersion.getDescription(fr));
            Assert.assertEquals("Official version", officialVersion.getDescription(en));
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
