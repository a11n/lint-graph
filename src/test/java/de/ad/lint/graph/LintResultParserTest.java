package de.ad.lint.graph;

import java.util.List;
import org.junit.Test;

public class LintResultParserTest {
  @Test
  public void testParse() throws Exception {
    LintResultParser parser = new LintResultParser();

    List<Issue> issues = parser.parse(this.getClass().getResourceAsStream("/input.xml"));
  }
}
