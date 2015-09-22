package de.ad.lint.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

class GraphBuilder {
  private static final Label ISSUE = DynamicLabel.label("Issue");
  private static final Label FILE = DynamicLabel.label("File");
  private static final Label VIOLATION = DynamicLabel.label("Violation");

  private GraphBuilder() {
  }

  public static void build(GraphDatabaseService database, List<Issue> issues) {
    try (Transaction tx = database.beginTx()) {
      buildGraph(database, issues);

      tx.success();
    }
  }

  private static void buildGraph(GraphDatabaseService database, List<Issue> issues) {
    Map<String, Node> nodes = new HashMap<>();
    for (Issue issue : issues) {
      Issue.Location location = issue.getLocation();

      Node issueNode = nodes.get(issue.getId());
      Node fileNode = nodes.get(location.getFile());

      if (issueNode == null) {
        issueNode = database.createNode(ISSUE);
        issueNode.setProperty("id", issue.getId());
        issueNode.setProperty("severity", issue.getSeverity());
        issueNode.setProperty("priority", issue.getPriority());
        issueNode.setProperty("summary", issue.getSummary());
        issueNode.setProperty("explanation", issue.getExplanation());
        issueNode.setProperty("url", issue.getUrl());
        issueNode.setProperty("urls", issue.getUrls());
      }

      if (fileNode == null) {
        fileNode = database.createNode(FILE);
        fileNode.setProperty("file", location.getFile());
      }
      
      //Node violationNode = database.createNode(VIOLATION);
      //violationNode.setProperty("line", location.getLine());
      //violationNode.setProperty("column", location.getColumn());
      //violationNode.setProperty("message", issue.getMessage());
      //violationNode.setProperty("errorLine1", issue.getErrorLine1());
      //violationNode.setProperty("errorLine2", issue.getErrorLine2());

      issueNode.createRelationshipTo(fileNode, Relationship.AFFECTS);
      fileNode.createRelationshipTo(issueNode, Relationship.IS_AFFECTED_BY);
      
      nodes.put(issue.getId(), issueNode);
      nodes.put(location.getFile(), fileNode);
    }
  }

  private enum Relationship implements RelationshipType {
    AFFECTS,
    IS_AFFECTED_BY
  }
}
