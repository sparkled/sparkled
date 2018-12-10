package io.sparkled.rest.jetty

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Redirects nonexistent paths to index.html to support Single Page App refreshing. For example,
 * if the user navigates to the /editor page and refreshes, they will get a 404. Redirecting to index.html
 * means that the SPA will load up and automatically navigate the user to the correct SPA page based on the URL.
 * REST links are explicitly excluded from rerouting.
 */
class TryFilesFilter : Filter {

    override fun init(config: FilterConfig) {
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        if (httpRequest.requestURI.startsWith(REST_PATH)) {
            chain.doFilter(httpRequest, httpResponse)
            return
        }

        val resolved = resolve(httpRequest, PATH)
        val url = request.getServletContext().getResource(resolved)

        if (url != null) {
            val resourcePath = url.toString().split(JAR_RESOURCE_FILE_SEPARATOR)
            if (resourcePath.size == 2 && javaClass.getResource(resourcePath[1]) != null) {
                chain.doFilter(httpRequest, httpResponse)
                return
            }
        }

        fallback(httpRequest, httpResponse)
    }

    private fun fallback(request: HttpServletRequest, response: HttpServletResponse) {
        val resolved = resolve(request, FALLBACK)
        request.servletContext.getRequestDispatcher(resolved).forward(request, response)
    }

    private fun resolve(request: HttpServletRequest, value: String): String {
        var path = request.servletPath
        path = if (path.startsWith(LEADING_SLASH)) path else LEADING_SLASH + path
        return value.replace(PATH, path)
    }

    override fun destroy() {
    }

    companion object {
        private const val PATH = "\$path"
        private const val FALLBACK = "/index.html"
        private const val REST_PATH = "/rest"
        private const val JAR_RESOURCE_FILE_SEPARATOR = "!"
        private const val LEADING_SLASH = "/"
    }
}