package io.sparkled;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Injector;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestApiServerImpl implements RestApiServer {

    private static final Logger logger = LoggerFactory.getLogger(RestApiServerImpl.class);
    private static final String REST_PATH = "/rest/*";
    private final ExecutorService executor;
    private boolean started;

    @Inject
    public RestApiServerImpl(Injector injector) {
        JerseyResourceConfig.setInjector(injector);

        this.executor = Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder().setNameFormat("rest-api-server-%d").build()
        );
    }

    @Override
    public void start(int port) throws Exception {
        if (started) {
            logger.warn("Attempted to start REST API server, but it is already running.");
            return;
        }

        executor.submit(() -> startServer(port));
        logger.info("Started REST API server at port {}.", port);
        started = true;
    }

    private void startServer(int port) {
        Server jettyServer = new Server(port);

        ServletContextHandler context = createContextHandler();
        addStaticResourceConfig(context);
        addJerseyServlet(context);
        initCorsFilter(context);

        jettyServer.setHandler(context);
        startJetty(jettyServer);
    }

    private ServletContextHandler createContextHandler() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(DefaultServlet.class, "/");
        return context;
    }

    private void addStaticResourceConfig(ServletContextHandler context) {
        URL webappLocation = getClass().getResource("/webapp/index.html");
        if (webappLocation == null) {
            logger.warn("Failed to retrieve webapp location, the web UI was likely omitted from this build.");
        } else {
            try {
                URI webRootUri = URI.create(webappLocation.toURI().toASCIIString().replaceFirst("/index.html$", "/"));
                context.setBaseResource(Resource.newResource(webRootUri));
                context.setWelcomeFiles(new String[]{"index.html"});

                GzipHandler gzipHandler = new GzipHandler();
                gzipHandler.setIncludedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name());
                context.setGzipHandler(gzipHandler);
                context.addFilter(TryFilesFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addJerseyServlet(ServletContextHandler context) {
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, REST_PATH);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "io.sparkled.rest");
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MultiPartFeature.class.getName());
        jerseyServlet.setInitParameter("javax.ws.rs.Application", JerseyResourceConfig.class.getName());
        jerseyServlet.setInitOrder(0);
    }

    private void initCorsFilter(ServletContextHandler context) {
        FilterHolder corsFilter = context.addFilter(CrossOriginFilter.class, REST_PATH, EnumSet.of(DispatcherType.REQUEST));
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "DELETE,GET,POST,PUT");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
    }

    private void startJetty(Server jettyServer) {
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            logger.error("Encountered an unexpected exception.", e);
        } finally {
            jettyServer.destroy();
        }
    }
}