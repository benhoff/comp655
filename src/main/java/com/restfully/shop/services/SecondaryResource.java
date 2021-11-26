/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restfully.shop.services;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import redis.clients.jedis.Jedis;

/**
 *
 * @author hoff
 */
@Path("/secondary")
public class SecondaryResource {
    private Jedis jedis;
    public SecondaryResource(Jedis inJedis)
    {
        jedis = inJedis;
    }
   
    @POST
    @PUT
    @Path("/{id}")
    public Response createSecondary(@PathParam("id") int inId)
    {
        // Check primary, otherwise throw BAD_REQUEST
        if (!jedis.hexists("gradebook", Integer.toString(inId)))
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        jedis.sadd("secondary", Integer.toString(inId));
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces("application/xml")
    public Response deleteSecondary(@PathParam("id") int inId)
    {
        if (!jedis.hexists("gradebook", Integer.toString(inId)))
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (!jedis.sismember("secondary", Integer.toString(inId)))
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        jedis.srem("secondary", Integer.toString(inId));
        return Response.ok().build();
    }
}
