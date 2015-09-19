package de.ad.lint.graph;

final class Issue {
  private final String id;
  private final String severity;
  private final String message;
  private final int priority;
  private final String summary;
  private final String explanation;
  private final String url;
  private final String urls;
  private final String errorLine1;
  private final String errorLine2;
  private final Location location;

  Issue(String id, String severity, String message, int priority, String summary,
      String explanation, String url, String urls, String errorLine1, String errorLine2,
      Location location) {
    this.id = id;
    this.severity = severity;
    this.message = message;
    this.priority = priority;
    this.summary = summary;
    this.explanation = explanation;
    this.url = url;
    this.urls = urls;
    this.errorLine1 = errorLine1;
    this.errorLine2 = errorLine2;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public String getSeverity() {
    return severity;
  }

  public String getMessage() {
    return message;
  }

  public int getPriority() {
    return priority;
  }

  public String getSummary() {
    return summary;
  }

  public String getExplanation() {
    return explanation;
  }

  public String getUrl() {
    return url;
  }

  public String getUrls() {
    return urls;
  }

  public String getErrorLine1() {
    return errorLine1;
  }

  public String getErrorLine2() {
    return errorLine2;
  }

  public Location getLocation() {
    return location;
  }

  static final class Location {
    private final String file;
    private final int line;
    private final int column;

    Location(String file, int line, int column) {
      this.file = file;
      this.line = line;
      this.column = column;
    }

    public String getFile() {
      return file;
    }

    public int getLine() {
      return line;
    }

    public int getColumn() {
      return column;
    }
  }
}
