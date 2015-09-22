package de.ad.lint.graph;

import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.xml.sax.SAXException;

public class Application {
  static GraphDatabaseFactory graphDatabaseFactory = new GraphDatabaseFactory();
  static LintResultParser lintResultParser = new LintResultParser();
  static LintResultImporterFactory lintResultImporterFactory = new LintResultImporterFactory();
  static Application application = new Application();

  public static void main(String... args)
      throws ParserConfigurationException, SAXException, IOException {
    verifyArguments(args);

    String pathToLintResults = args[0];
    String server = args[1];
    String user = args[2];
    String password = args[3];

    GraphDatabaseService database = graphDatabaseFactory.create(server, user, password);
    LintResultImporter importer = lintResultImporterFactory.create(database, lintResultParser);
    
    application.execute(importer, pathToLintResults);
  }

  private void execute(LintResultImporter importer, String pathToLintResults)
      throws IOException, ParserConfigurationException, SAXException {
    importer.importLintResults(new FileInputStream(pathToLintResults));
  }

  private static void verifyArguments(String[] args) {
    if (args.length != 4) {
      throw illegalArgument(
          "Wrong number of arguments. Call with: <path to lint results xml> <server> <user> <password>");
    }
  }

  private static IllegalArgumentException illegalArgument(String message, Object... arguments) {
    message = String.format(message, arguments);
    return new IllegalArgumentException(message);
  }

  static class LintResultImporterFactory {
    public LintResultImporter create(GraphDatabaseService database,
        LintResultParser lintResultParser) {
      return new LintResultImporter(database, lintResultParser);
    }
  }
}
