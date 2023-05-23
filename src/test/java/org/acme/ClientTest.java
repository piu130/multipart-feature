package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.multipart.FileDownload;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataOutput;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class ClientTest {
    @Inject
    @RestClient
    Client client;

    @Test
    public void file() throws Exception {
        var res = client.multipart(File.createTempFile("bla", ".pdf"));
        assertEquals("multi", res);
    }

    @Test
    public void fileUpload() throws Exception {
        var res = client.multipart(new FileUpload() {
            @Override
            public String name() {
                return "file";
            }

            @Override
            public Path filePath() {
                try {
                    return Files.createTempFile("bla", ".pdf");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String fileName() {
                return "bla.pdf";
            }

            @Override
            public long size() {
                return 0;
            }

            @Override
            public String contentType() {
                return "application/pdf";
            }

            @Override
            public String charSet() {
                return null;
            }
        });
        assertEquals("multi", res);
    }

    @Test
    public void fileDownload() throws Exception {
        var res = client.multipart(new FileDownload() {
            @Override
            public String name() {
                return "file";
            }

            @Override
            public Path filePath() {
                try {
                    return Files.createTempFile("bla", ".pdf");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String fileName() {
                return "bla.pdf";
            }

            @Override
            public long size() {
                return 0;
            }

            @Override
            public String contentType() {
                return "application/pdf";
            }

            @Override
            public String charSet() {
                return null;
            }
        });
        assertEquals("multi", res);
    }

    @Test
    public void multipartFormDataOutput() throws Exception {
        var multipartFormDataOutput = new MultipartFormDataOutput();
        multipartFormDataOutput.addFormData("file", Files.createTempFile("bla", ".pdf"), MediaType.valueOf("application/pdf"), "bla.pdf");
        var res = client.multipart(multipartFormDataOutput);
        assertEquals("multi", res);
    }
}
