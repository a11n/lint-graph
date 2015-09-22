package de.ad.lint.graph;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class LintResultImporterTest {
  private GraphDatabaseService graphDb;

  @Before
  public void setUp() {
    graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
  }

  @Test
  public void testImport() throws Exception {
    LintResultParser parser = mock(LintResultParser.class);
    LintResultImporter importer = new LintResultImporter(graphDb, parser);

    importer.importLintResults(null);

    GlobalGraphOperations operations = GlobalGraphOperations.at(graphDb);

    assertThat(operations.getAllLabels()).extractingResultOf("name")
        .containsExactly("Issue", "File");

    NodeAssert.assertThat(operations.getAllNodes()).extractingProperty("id").containsExactly("A");
  }

  @After
  public void tearDown() {
    graphDb.shutdown();
  }

  private static class NodeAssert
      extends AbstractIterableAssert<NodeAssert, Iterable<? extends Node>, Node> {
    protected NodeAssert(Iterable<Node> actual) {
      super(actual, NodeAssert.class);
    }

    public static NodeAssert assertThat(Iterable<Node> actual) {
      return new NodeAssert(actual);
    }

    public IterableAssert<String> extractingProperty(String key) {
      Iterable<String> properties =
          StreamSupport.stream(actual.spliterator(), false).
              map(n -> (String) n.getProperty("key")).collect(Collectors.toList());

      return (IterableAssert) Assertions.<String>assertThat(properties);
    }
  }
}
