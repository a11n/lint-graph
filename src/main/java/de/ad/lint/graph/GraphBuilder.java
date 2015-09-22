package de.ad.lint.graph;

import java.util.List;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

class GraphBuilder {
  private static final Label ISSUE = DynamicLabel.label("Issue");
  private static final Label FILE = DynamicLabel.label("File");

  private GraphBuilder() {
  }

  public static void build(GraphDatabaseService database, List<Issue> issues) {
    try (Transaction tx = database.beginTx()) {
      buildGraph(database, issues);

      tx.success();
    }
  }

  private static void buildGraph(GraphDatabaseService database, List<Issue> issues) {
    for (Issue issue : issues) {
      Label severity = DynamicLabel.label(issue.getSeverity());
      
      Node issueNode = database.createNode(ISSUE, severity);
      issueNode.setProperty("id", issue.getId());
      issueNode.setProperty("severity", issue.getSeverity());
      issueNode.setProperty("message", issue.getMessage());
      issueNode.setProperty("priority", issue.getPriority());
      issueNode.setProperty("summary", issue.getSummary());
      issueNode.setProperty("explanation", issue.getExplanation());
      issueNode.setProperty("url", issue.getUrl());
      issueNode.setProperty("urls", issue.getUrls());
      issueNode.setProperty("errorLine1", issue.getErrorLine1());
      issueNode.setProperty("errorLine2", issue.getErrorLine2());

      Issue.Location location = issue.getLocation();
      Node fileNode = database.createNode(FILE);
      fileNode.setProperty("file", location.getFile());
      fileNode.setProperty("line", location.getLine());
      fileNode.setProperty("column", location.getColumn());

      issueNode.createRelationshipTo(fileNode, Relationship.AFFECTS);
      fileNode.createRelationshipTo(issueNode, Relationship.IS_AFFECTED_BY);
    }
  }

  private enum Relationship implements RelationshipType {
    AFFECTS,
    IS_AFFECTED_BY
  }
}
