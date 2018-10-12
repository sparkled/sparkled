package io.sparkled.rest;

import com.google.inject.persist.UnitOfWork;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Integrates the Guice {@link UnitOfWork} into the REST API, which automatically creates and destroys an
 * {@link EntityManager} for each REST API call.
 */
@Provider
public class UnitOfWorkFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private UnitOfWork unitOfWork;

    @Inject
    public UnitOfWorkFilter(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        unitOfWork.begin();
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        unitOfWork.end();
    }
}
