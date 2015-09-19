package de.ad.lint.graph;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LintResultParser {

  public List<Issue> parse(InputStream input)
      throws ParserConfigurationException, IOException, SAXException {
    List<Issue> result = new ArrayList<Issue>();

    Document document = parseStream(input);

    NodeList issues = document.getElementsByTagName("issue");
    for (int i = 0; i < issues.getLength(); i++) {
      Issue issue = resolveIssue(issues.item(i));
      result.add(issue);
    }

    return result;
  }

  private Document parseStream(InputStream input)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(input);
  }

  private Issue resolveIssue(Node item) {
    Element issue = (Element) item;

    Issue.Location location = resolveLocation(issue);

    return new Issue(issue.getAttribute("id"), issue.getAttribute("severity"),
        issue.getAttribute("message"), Integer.valueOf(issue.getAttribute("priority")),
        issue.getAttribute("summary"), issue.getAttribute("explanation"),
        issue.getAttribute("url"), issue.getAttribute("urls"), issue.getAttribute("errorLine1"),
        issue.getAttribute("errorLine2"),
        location);
  }

  private Issue.Location resolveLocation(Element issue) {
    Element location = (Element) issue.getElementsByTagName("location").item(0);

    return new Issue.Location(location.getAttribute("file"), location.hasAttribute("line") ?
        Integer.valueOf(location.getAttribute("line")) : 0, location.hasAttribute("column") ?
        Integer.valueOf(location.getAttribute("column")) : 0);
  }
}
