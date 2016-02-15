package com.leela.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leela.pubnub.PubnubPublisher;
import com.leela.pubnub.PubnubSubscriber;

@Path("/pubnub")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PubnubResource {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PubnubResource.class);

    @Path("/newChannel")
    @POST
    public Response createNewChannelAndSubscribe(
            @QueryParam("channelName") final String channelName) {
        try {
            PubnubSubscriber.subscribeToChannel(channelName);
            return Response.status(200).build();
        } catch (final Exception e) {
            LOGGER.error("Error", e);
            return Response.status(400).build();
        }
    }

    @Path("/publisMessage")
    @POST
    public Response publishMessageToAChannel(
            @QueryParam("message") final String message,
            @QueryParam("channelName") final String channelName) {
        try {
            PubnubPublisher.publishMessageToChannel(channelName, message);
            return Response.status(200).build();
        } catch (final Exception e) {
            LOGGER.error("Error", e);
            return Response.status(400).build();
        }
    }
}
