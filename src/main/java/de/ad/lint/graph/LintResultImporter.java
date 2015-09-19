package de.ad.lint.graph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.xml.sax.SAXException;

class LintResultImporter {
  private final GraphDatabaseService database;
  private final LintResultParser parser;

  public LintResultImporter(GraphDatabaseService database, LintResultParser parser) {
    this.database = database;
    this.parser = parser;
  }

  public void importLintResults(InputStream lintResultXml)
      throws IOException, SAXException, ParserConfigurationException {
    List<Issue> issues = parser.parse(lintResultXml);

    try (Transaction tx = database.beginTx()) {
      for (Issue issue : issues) {
        database.createNode().setProperty("id", issue.getId());
      }

      tx.success();
    }
  }
}
