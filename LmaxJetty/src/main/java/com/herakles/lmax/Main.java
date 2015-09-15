package com.herakles.lmax;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

    public static void main(String[] args) throws Exception {

        String weppAppHome = args[0];// path/to/src/main/webapp
        Integer port = new Integer(args[1]);// 8080

        Server server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setCompactPath(true);

        webapp.setDescriptor(weppAppHome + "/WEB-INF/web.xml");
        webapp.setResourceBase(weppAppHome);
        webapp.setParentLoaderPriority(true);

        server.setHandler(webapp);
        server.start();
        server.join();

    }

}