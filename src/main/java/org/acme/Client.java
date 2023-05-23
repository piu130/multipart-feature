package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileDownload;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataOutput;

import java.io.File;

@Path("/")
@RegisterRestClient(baseUri = "http://localhost:8080")
public interface Client {
    @POST
    String multipart(@RestForm File file);

    @POST
    String multipart(@RestForm FileUpload file);

    @POST
    String multipart(@RestForm FileDownload file);

    @POST
    String multipart(@RestForm MultipartFormDataOutput output);
}
