package io.sparkled.rest

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.Injector
import io.sparkled.rest.jetty.JerseyResourceConfig
import io.sparkled.rest.jetty.TryFilesFilter
import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.eclipse.jetty.util.resource.Resource
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.servlet.ServletContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import javax.servlet.DispatcherType
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.EnumSet
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RestApiServerImpl @Inject
constructor(injector: Injector) : RestApiServer {
    private val executor: ExecutorService
    private var started: Boolean = false

    init {
        JerseyResourceConfig.setInjector(injector)

        this.executor = Executors.newSingleThreadExecutor(
                ThreadFactoryBuilder().setNameFormat("rest-api-server-%d").build()
        )
    }

    @Override
    @Throws(Exception::class)
    fun start(port: Int) {
        if (started) {
            logger.warn("Attempted to start REST API server, but it is already running.")
            return
        }

        executor.submit({ startServer(port) })
        logger.info("Started REST API server at port {}.", port)
        started = true
    }

    private fun startServer(port: Int) {
        val jettyServer = Server(port)

        val context = createContextHandler()
        addStaticResourceConfig(context)
        addJerseyServlet(context)
        addCorsFilter(context)

        jettyServer.setHandler(context)
        startJetty(jettyServer)
    }

    private fun createContextHandler(): ServletContextHandler {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.setContextPath("/")
        context.addServlet(DefaultServlet::class.java, "/")
        return context
    }

    private fun addStaticResourceConfig(context: ServletContextHandler) {
        val webappLocation = getClass().getResource("/webapp/index.html")
        if (webappLocation == null) {
            logger.warn("Failed to retrieve webapp location, the webapp was likely omitted from this build.")
        } else {
            try {
                val webRootUri = URI.create(webappLocation!!.toURI().toASCIIString().replaceFirst("/index.html$", "/"))
                context.setBaseResource(Resource.newResource(webRootUri))
                context.setWelcomeFiles(arrayOf("index.html"))

                val gzipHandler = GzipHandler()
                gzipHandler.setIncludedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name())
                context.setGzipHandler(gzipHandler)
                context.addFilter(TryFilesFilter::class.java, "*", EnumSet.of(DispatcherType.REQUEST))
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

        }
    }

    private fun addJerseyServlet(context: ServletContextHandler) {
        val jerseyServlet = context.addServlet(ServletContainer::class.java, REST_PATH)
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", getClass().getPackage().getName())
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MultiPartFeature::class.java!!.getName())
        jerseyServlet.setInitParameter("javax.ws.rs.Application", JerseyResourceConfig::class.java!!.getName())
        jerseyServlet.setInitOrder(0)
    }

    private fun addCorsFilter(context: ServletContextHandler) {
        val corsFilter = context.addFilter(CrossOriginFilter::class.java, REST_PATH, EnumSet.of(DispatcherType.REQUEST))
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*")
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "DELETE,GET,POST,PUT")
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin")
    }

    private fun startJetty(jettyServer: Server) {
        try {
            jettyServer.start()
            jettyServer.join()
        } catch (e: Exception) {
            logger.error("Encountered an unexpected exception.", e)
        } finally {
            jettyServer.destroy()
        }
    }

    companion object {

        private val logger = LoggerFactory.getLogger(RestApiServerImpl::class.java)
        private val REST_PATH = "/rest/*"
    }
}