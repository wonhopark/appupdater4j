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
        v1.getVersion().setMajor("1");
        v1.getVersion().setMinor("0");
        v1.getVersion().setBuild("0");
        
        ApplicationVersion v2 = new ApplicationVersion();
        v1.setName("v2 : 0.2.0");
        v2.getVersion().setMajor("0");
        v2.getVersion().setMinor("2");
        v2.getVersion().setBuild("0");
        
        ApplicationVersion v3 = new ApplicationVersion();
        v3.setName("v3 : 0.0.5.1");
        v3.getVersion().setMajor("0");
        v3.getVersion().setMinor("0");
        v3.getVersion().setBuild("5");
        v3.getVersion().setRevision("1");
        
        ApplicationVersion v4 = new ApplicationVersion();
        v4.setName("v4 : 0.0.5");
        v4.getVersion().setMajor("0");
        v4.getVersion().setMinor("0");
        v4.getVersion().setBuild("5");
        
        ApplicationVersion v5 = new ApplicationVersion();
        v5.setName("v5 : 0.0.4");
        v5.getVersion().setMajor("0");
        v5.getVersion().setMinor("0");
        v5.getVersion().setBuild("4");
        
        ArrayList<ApplicationVersion> versions = new ArrayList<ApplicationVersion>();
        versions.add(v2);
        versions.add(v5);
        versions.add(v3);
        versions.add(v1);
        versions.add(v4);
        
        Collections.sort(versions);
        
        
        Assert.assertEquals(v5, versions.get(0));
        Assert.assertEquals(v4, versions.get(1));
        Assert.assertEquals(v3, versions.get(2));
        Assert.assertEquals(v2, versions.get(3));
        Assert.assertEquals(v1, versions.get(4));
        
        //Check that v4 older than 0.0.4 (from String)
        Assert.assertTrue(v4.getVersion().compareToStringVersion("0.0.4") > 0);
    }
}
