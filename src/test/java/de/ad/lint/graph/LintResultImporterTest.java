package de.ad.lint.graph;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;

public class LintResultImporterTest {
  @Test
  public void testImport() throws Exception {
    GraphDatabaseService database =
        new GraphDatabaseFactory().create("http://localhost:7474/db/data", "neo4j", "neo4j");
    LintResultParser parser = new LintResultParser();
    LintResultImporter importer = new LintResultImporter(database, parser);

    importer.importLintResults(getClass().getResourceAsStream("/input.xml"));
  }
}
