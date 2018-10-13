package io.sparkled.rest;

/**
 * A REST API Server that listens on the given port.
 */
public interface RestApiServer {

    /**
     * Start the REST API server on the provided port. This method is idempotent.
     *
     * @param port The port to listen on.
     */
    void start(int port) throws Exception;
}
