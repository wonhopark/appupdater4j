package org.gdteam.appupdater4j.demo.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class FeedResource extends ServerResource {

    @Get
    public Representation getUpdateFeed() throws ResourceException {
        
        String buildNumber = this.getRequest().getResourceRef().getQueryAsForm().getValues("buildnumber");
        
        System.out.println("Number : " + buildNumber);
        
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        try {
            reader = new BufferedReader(new InputStreamReader(FeedResource.class.getClassLoader().getResourceAsStream("feed.xml")));
            
            String line  = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }
            
            //Replace token
            String replaced = stringBuilder.toString().replace("@build@", buildNumber);

            File tmp = File.createTempFile("feed", "xml");
            
            writer = new BufferedWriter(new FileWriter(tmp));
            writer.write(replaced);
            
            FileRepresentation fr = new FileRepresentation(tmp, MediaType.APPLICATION_XML);
            return fr;
            
        } catch (Exception e) {
            throw new ResourceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
