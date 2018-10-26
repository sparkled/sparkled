package io.sparkled.rest.jetty

import com.google.inject.persist.UnitOfWork
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.ext.Provider

/**
 * Integrates the Guice [UnitOfWork] into the REST API, which automatically creates and destroys an
 * [EntityManager] for each REST API call.
 */
@Provider
class UnitOfWorkFilter @Inject
constructor(private val unitOfWork: UnitOfWork) : ContainerRequestFilter, ContainerResponseFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        unitOfWork.begin()
    }

    override fun filter(request: ContainerRequestContext, response: ContainerResponseContext) {
        unitOfWork.end()
    }
}
