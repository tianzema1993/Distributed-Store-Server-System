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

  public long getStartTimestamp() {
    return startTimestamp;
  }

  public long getLatency() {
    return latency;
  }

  public String getRequestType() {
    return requestType;
  }

  public String getResponseContent() {
    return responseContent;
  }

  @Override
  public String toString() {
    return startTimestamp + " " + latency + " " + requestType + " " + responseContent + "\n";
  }
}
