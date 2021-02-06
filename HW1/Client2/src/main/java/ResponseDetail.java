public class ResponseDetail {
  private long startTimestamp;
  private long latency;
  private String requestType;
  private String responseContent;

  public ResponseDetail(long startTimestamp, long endTimestamp, String requestType,
      String responseContent) {
    this.startTimestamp = startTimestamp;
    this.latency = endTimestamp - startTimestamp;
    this.requestType = requestType;
    this.responseContent = responseContent;
  }

  @Override
  public String toString() {
    return startTimestamp + " " + latency + " " + requestType + " " + responseContent + "\n";
  }
}
