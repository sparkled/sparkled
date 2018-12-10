package io.sparkled.rest

/**
 * A REST API Server that listens on the given port.
 */
interface RestApiServer {

    /**
     * Start the REST API server on the provided port. This method is idempotent.

     * @param port The port to listen on.
     */
    fun start(port: Int)
}
