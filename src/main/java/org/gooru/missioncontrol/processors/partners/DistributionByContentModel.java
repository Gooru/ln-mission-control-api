
package org.gooru.missioncontrol.processors.partners;


public class DistributionByContentModel {
  private String contentType;
  private Long totalCount;

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

}
