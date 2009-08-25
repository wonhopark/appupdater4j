package org.gdteam.appupdater4j.demo.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class DemoApplication extends Application {

    @Override
    public Restlet createRoot() {
        Router router = new Router(getContext());

        // Defines only one route
        router.attach("/feed", FeedResource.class);

        return router;
    }
}
