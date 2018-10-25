package io.sparkled.rest.util

import io.sparkled.model.util.GsonProvider

import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class JerseyGsonProvider : MessageBodyWriter<Object>, MessageBodyReader<Object> {

    @Override
    fun isReadable(type: Class<*>, genericType: Type,
                   annotations: Array<java.lang.annotation.Annotation>, mediaType: MediaType): Boolean {
        return true
    }

    @Override
    @Throws(IOException::class)
    fun readFrom(type: Class<Object>, genericType: Type,
                 annotations: Array<Annotation>, mediaType: MediaType,
                 httpHeaders: MultivaluedMap<String, String>, entityStream: InputStream): Object {
        InputStreamReader(entityStream, StandardCharsets.UTF_8).use({ streamReader -> return GsonProvider.get().fromJson(streamReader, genericType) })
    }

    @Override
    fun isWriteable(type: Class<*>, genericType: Type, annotations: Array<Annotation>, mediaType: MediaType): Boolean {
        return true
    }

    @Override
    fun getSize(`object`: Object, type: Class<*>, genericType: Type, annotations: Array<Annotation>, mediaType: MediaType): Long {
        return -1
    }

    @Override
    @Throws(IOException::class, WebApplicationException::class)
    fun writeTo(`object`: Object, type: Class<*>, genericType: Type, annotations: Array<Annotation>, mediaType: MediaType,
                httpHeaders: MultivaluedMap<String, Object>, entityStream: OutputStream) {
        OutputStreamWriter(entityStream, StandardCharsets.UTF_8).use({ writer -> GsonProvider.get().toJson(`object`, genericType, writer) })
    }
}