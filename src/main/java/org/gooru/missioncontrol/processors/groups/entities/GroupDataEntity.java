
package org.gooru.missioncontrol.processors.groups.entities;

import org.apache.commons.csv.CSVRecord;

/**
 * @author szgooru Created On 31-Aug-2019
 */
public class GroupDataEntity {

  private String country;
  private String state;
  private String district;
  private String districtCode;
  private String block;
  private String blockCode;
  private String cluster;
  private String clusterCode;
  private String school;
  private String schoolCode;
  
  public GroupDataEntity(CSVRecord record) {
    this.country = record.get(0);
    this.state = record.get(1);
    this.district = record.get(2);
    this.districtCode = record.get(3);
    this.block = record.get(4);
    this.blockCode = record.get(5);
    this.cluster = record.get(6);
    this.clusterCode = record.get(7);
    this.school = record.get(8);
    this.schoolCode = record.get(9);
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getDistrictCode() {
    return districtCode.toLowerCase();
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  public String getBlock() {
    return block;
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public String getBlockCode() {
    return blockCode.toLowerCase();
  }

  public void setBlockCode(String blockCode) {
    this.blockCode = blockCode;
  }

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public String getClusterCode() {
    return clusterCode.toLowerCase();
  }

  public void setClusterCode(String clusterCode) {
    this.clusterCode = clusterCode;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
  }

  public String getSchoolCode() {
    return schoolCode.toLowerCase();
  }

  public void setSchoolCode(String schoolCode) {
    this.schoolCode = schoolCode;
  }

}
