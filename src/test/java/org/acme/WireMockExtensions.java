package org.acme;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import jakarta.ws.rs.core.HttpHeaders;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public class WireMockExtensions implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(post("/")
                .withMultipartRequestBody(
                        new MultipartValuePatternBuilder()
                                .withHeader(HttpHeaders.CONTENT_DISPOSITION, containing("filename=\"bla"))
                                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo("application/pdf"))
                )
                .willReturn(aResponse().withBody("multi")));

        wireMockServer.stubFor(get(urlMatching(".*")).atPriority(10).willReturn(aResponse().proxiedFrom("https://stage.code.quarkus.io/api")));

        return Map.of("quarkus.rest-client.\"org.acme.rest.client.ExtensionsService\".url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}