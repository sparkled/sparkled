package io.sparkled.rest

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.Injector
import io.sparkled.rest.jetty.JerseyResourceConfig
import io.sparkled.rest.jetty.TryFilesFilter
import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.eclipse.jetty.util.resource.Resource
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.servlet.ServletProperties
import org.slf4j.LoggerFactory
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.util.EnumSet
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.servlet.DispatcherType

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

    @Throws(Exception::class)
    override fun start(port: Int) {
        if (started) {
            logger.warn("Attempted to start REST API server, but it is already running.")
            return
        }

        executor.submit { startServer(port) }
        logger.info("Started REST API server at port {}.", port)
        started = true
    }

    private fun startServer(port: Int) {
        val jettyServer = Server(port)

        val context = createContextHandler()
        addStaticResourceConfig(context)
        addJerseyServlet(context)
        addCorsFilter(context)

        jettyServer.handler = context
        startJetty(jettyServer)
    }

    private fun createContextHandler(): ServletContextHandler {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        context.addServlet(DefaultServlet::class.java, "/")
        return context
    }

    private fun addStaticResourceConfig(context: ServletContextHandler) {
        val webappLocation = javaClass.getResource("/webapp/$INDEX_HTML")
        if (webappLocation == null) {
            logger.warn("Failed to retrieve webapp location, the webapp was likely omitted from this build.")
        } else {
            try {
                logger.warn("Webapp location: $webappLocation")

                val webRootUri = URI.create(webappLocation.toURI().toASCIIString().replace("/$INDEX_HTML", "/"))
                context.baseResource = Resource.newResource(webRootUri)
                context.welcomeFiles = arrayOf(INDEX_HTML)

                val gzipHandler = GzipHandler()
                gzipHandler.setIncludedMethods(HttpMethod.GET.name, HttpMethod.POST.name, HttpMethod.PUT.name)
                context.gzipHandler = gzipHandler
                context.addFilter(TryFilesFilter::class.java, "*", EnumSet.of(DispatcherType.REQUEST))
            } catch (e: URISyntaxException) {
                logger.error("Failed to retrieve root URI.", e)
            } catch (e: MalformedURLException) {
                logger.error("Failed to retrieve root URI.", e)
            }
        }
    }

    private fun addJerseyServlet(context: ServletContextHandler) {
        val jerseyServlet = context.addServlet(ServletContainer::class.qualifiedName, REST_PATH)
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", javaClass.getPackage().name)
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MultiPartFeature::class.qualifiedName)
        jerseyServlet.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyResourceConfig::class.qualifiedName)
        jerseyServlet.initOrder = 0
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
        private const val REST_PATH = "/rest/*"
        private const val INDEX_HTML = "index.html"
    }
}