package de.ad.lint.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.rest.graphdb.RestGraphDatabase;

class GraphDatabaseFactory {
  public GraphDatabaseService create(String serverUrl, String user, String password) {
    GraphDatabaseService graphDatabase = new RestGraphDatabase(serverUrl, user, password);

    registerShutdownHook(graphDatabase);

    return graphDatabase;
  }

  private void registerShutdownHook(final GraphDatabaseService graphDb) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graphDb.shutdown();
      }
    });
  }
}
