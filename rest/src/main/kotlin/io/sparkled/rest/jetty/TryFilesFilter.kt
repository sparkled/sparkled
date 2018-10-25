package io.sparkled.rest.jetty

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern

/**
 * Redirects nonexistent paths to index.html to support Single Page App refreshing. For example,
 * if the user navigates to the /editor page and refreshes, they will get a 404. Redirecting to index.html
 * means that the SPA will load up and automatically navigate the user to the correct SPA page based on the URL.
 * REST links are explicitly excluded from rerouting.
 */
class TryFilesFilter : Filter {

    @Override
    @Throws(ServletException::class)
    fun init(config: FilterConfig) {
    }

    @Override
    @Throws(IOException::class, ServletException::class)
    fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        if (httpRequest.getRequestURI().startsWith(REST_PATH)) {
            chain.doFilter(httpRequest, httpResponse)
            return
        }

        val resolved = resolve(httpRequest, PATH)
        val url = request.getServletContext().getResource(resolved)

        if (url != null) {
            val resourcePath = url!!.toString().split(JAR_RESOURCE_FILE_SEPARATOR)
            if (resourcePath.size == 2 && getClass().getResource(resourcePath[1]) != null) {
                chain.doFilter(httpRequest, httpResponse)
                return
            }
        }

        fallback(httpRequest, httpResponse)
    }

    @Throws(IOException::class, ServletException::class)
    private fun fallback(request: HttpServletRequest, response: HttpServletResponse) {
        val resolved = resolve(request, FALLBACK)
        request.getServletContext().getRequestDispatcher(resolved).forward(request, response)
    }

    private fun resolve(request: HttpServletRequest, value: String): String {
        var path = request.getServletPath()
        path = if (path.startsWith(LEADING_SLASH)) path else LEADING_SLASH + path
        return value.replaceAll(Pattern.quote(PATH), path)
    }

    @Override
    fun destroy() {
    }

    companion object {

        private val PATH = "\$path"
        private val FALLBACK = "/index.html"
        private val REST_PATH = "/rest"
        private val JAR_RESOURCE_FILE_SEPARATOR = "!"
        private val LEADING_SLASH = "/"
    }
}