package com.leela.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ElasticSearchResource {
    private static ObjectMapper mapper = new ObjectMapper();

    public ElasticSearchResource(final Client client) {
    }
}
