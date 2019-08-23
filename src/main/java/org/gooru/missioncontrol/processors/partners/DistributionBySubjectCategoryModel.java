
package org.gooru.missioncontrol.processors.partners;


public class DistributionBySubjectCategoryModel {
  private String categoryCode;
  private Long totalCount;


  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public String getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }


}
