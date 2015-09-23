package de.ad.lint.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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

      if (!nodes.containsKey(issue.getId())) {
        issueNode.createRelationshipTo(fileNode, Relationships.AFFECTS);
      }

      if (!nodes.containsKey(location.getFile())) {
        fileNode.createRelationshipTo(issueNode, Relationships.IS_AFFECTED_BY);
      }

      Relationship violatesRelation =
          fileNode.createRelationshipTo(issueNode, Relationships.VIOLATES);
      violatesRelation.setProperty("line", location.getLine());
      violatesRelation.setProperty("column", location.getColumn());
      violatesRelation.setProperty("message", issue.getMessage());
      violatesRelation.setProperty("errorLine1", issue.getErrorLine1());
      violatesRelation.setProperty("errorLine2", issue.getErrorLine2());

      nodes.put(issue.getId(), issueNode);
      nodes.put(location.getFile(), fileNode);
    }
  }

  private enum Relationships implements RelationshipType {
    AFFECTS,
    IS_AFFECTED_BY,
    VIOLATES
  }
}
