package de.ad.lint.graph;

import java.io.InputStream;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

;

public class LintResultParserTest {
  @Test
  public void testParse() throws Exception {
    LintResultParser parser = new LintResultParser();
    InputStream input = this.getClass().getResourceAsStream("/input.xml");

    List<Issue> issues = parser.parse(input);

    assertThat(issues).extracting("id").
        containsExactly("DefaultLocale", "UnusedResources");
  }
}
