package net.chrisparton.sparkled;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Redirects nonexistent paths to index.html to support Single Page App refreshing. For example,
 * if the user navigates to the /editor page and refreshes, they will get a 404. Redirecting to index.html
 * means that the SPA will load up and automatically navigate the user to the correct SPA page based on the URL.
 *
 * REST links are explicitly excluded from rerouting.
 */
public class TryFilesFilter implements Filter {

    private static final String PATH = "$path";
    private static final String FALLBACK = "/index.html";
    private static final String REST_PATH = "/rest";
    private static final String JAR_RESOURCE_FILE_SEPARATOR = "!";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().startsWith(REST_PATH)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String resolved = resolve(httpRequest, PATH);
        URL url = request.getServletContext().getResource(resolved);

        if (url != null) {
            String[] resourcePath = url.toString().split(JAR_RESOURCE_FILE_SEPARATOR);
            if (resourcePath.length == 2 && getClass().getResource(resourcePath[1]) != null) {
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }

        fallback(httpRequest, httpResponse);
    }

    protected void fallback(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String resolved = resolve(request, FALLBACK);
        request.getServletContext().getRequestDispatcher(resolved).forward(request, response);
    }

    private String resolve(HttpServletRequest request, String value) {
        String path = request.getServletPath();
        path = StringUtils.prependIfMissing(path, "/");
        return value.replaceAll(Pattern.quote(PATH), path);
    }

    @Override
    public void destroy() {
    }
}