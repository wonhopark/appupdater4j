package org.gdteam.appupdater4j.model;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

public class ApplicationVersionTest {

    @Test
    public void testVersionCompare() {
        
        ApplicationVersion v1 = new ApplicationVersion();
        v1.setName("v1 : 1.0.0");
        v1.setMajor("1");
        v1.setMinor("0");
        v1.setBuild("0");
        
        ApplicationVersion v2 = new ApplicationVersion();
        v1.setName("v2 : 0.2.0");
        v2.setMajor("0");
        v2.setMinor("2");
        v2.setBuild("0");
        
        ApplicationVersion v3 = new ApplicationVersion();
        v3.setName("v3 : 0.0.5.1");
        v3.setMajor("0");
        v3.setMinor("0");
        v3.setBuild("5");
        v3.setRevision("1");
        
        ApplicationVersion v4 = new ApplicationVersion();
        v4.setName("v4 : 0.0.5");
        v4.setMajor("0");
        v4.setMinor("0");
        v4.setBuild("5");
        
        ApplicationVersion v5 = new ApplicationVersion();
        v5.setName("v5 : 0.0.4");
        v5.setMajor("0");
        v5.setMinor("0");
        v5.setBuild("4");
        
        ArrayList<ApplicationVersion> versions = new ArrayList<ApplicationVersion>();
        versions.add(v2);
        versions.add(v5);
        versions.add(v3);
        versions.add(v1);
        versions.add(v4);
        
        Collections.sort(versions);
        
        Assert.assertEquals(v1, versions.get(0));
        Assert.assertEquals(v2, versions.get(1));
        Assert.assertEquals(v3, versions.get(2));
        Assert.assertEquals(v4, versions.get(3));
        Assert.assertEquals(v5, versions.get(4));
        
        //Check that v4 younger than 0.0.4 (from String)
        Assert.assertTrue(v4.compareToStringVersion("0.0.4") < 0);
    }
}
