package org.gdteam.appupdater4j.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationVersion {

    //See: http://en.wikipedia.org/wiki/Software_versioning
    private String major, minor, build, revision;
    private String updateURL;
    private boolean needReboot = false;
    private Map<Locale, String> localizedDescription = new HashMap<Locale, String>();
    private Date publicationDate;
    
}
