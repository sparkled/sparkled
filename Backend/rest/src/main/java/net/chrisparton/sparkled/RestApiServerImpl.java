package net.chrisparton.sparkled;

import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestApiServerImpl implements RestApiServer {

    private static final Logger logger = Logger.getLogger(RestApiServerImpl.class.getName());
    private static final String REST_PATH = "/rest/*";

    public static Injector injector;
    private final ExecutorService threadPool;

    @Inject
    public RestApiServerImpl(Injector injector) {
        RestApiServerImpl.injector = injector;
        this.threadPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start(int port) throws Exception {
        threadPool.submit(() -> startServer(port));
    }

    private void startServer(int port) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(port);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, REST_PATH);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "net.chrisparton.sparkled.rest");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MultiPartFeature.class.getName());
        jerseyServlet.setInitParameter("javax.ws.rs.Application", JerseyResourceConfig.class.getName());
        jerseyServlet.setInitOrder(0);

        initCorsFilter(context);

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Encountered an unexpected exception.", e);
        } finally {
            jettyServer.destroy();
        }
    }

    private void initCorsFilter(ServletContextHandler context) {
        FilterHolder corsFilter = context.addFilter(CrossOriginFilter.class, REST_PATH, EnumSet.of(DispatcherType.REQUEST));
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "DELETE,GET,POST,PUT");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
    }
}