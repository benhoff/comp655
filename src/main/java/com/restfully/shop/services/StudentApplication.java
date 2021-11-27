package com.restfully.shop.services;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceModelIssue;
import redis.clients.jedis.Jedis;

// FIXME
// https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/deployment.html#environmenmt.appmodel
//@javax.ws.rs.ApplicationPath("webresources")
public class StudentApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> empty = new HashSet<Class<?>>();
   private Jedis jedis;
   
   public StudentApplication() {
       jedis = new Jedis();
       singletons.add(new SecondaryResource(jedis));
       singletons.add(new GradebookResource(jedis));
   }

   @Override
   public Set<Class<?>> getClasses() {
      return empty;
   }

   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }
}
