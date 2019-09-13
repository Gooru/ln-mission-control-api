
package org.gooru.missioncontrol.processors.groups.entities;

/**
 * @author szgooru Created On 01-Sep-2019
 */
public class Group {

  private Long id;
  private String name;
  private String code;
  private String type;
  private String subType;
  private Long stateId;
  private Long countryId;
  private String tenant;
  private String tenantRoot;
  private String creatorId;
  private String modifierId;
  private String referenceId;
  private Long parentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public void setTenantRoot(String tenantRoot) {
    this.tenantRoot = tenantRoot;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public String getModifierId() {
    return modifierId;
  }

  public void setModifierId(String modifierId) {
    this.modifierId = modifierId;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return "Group [id=" + id + ", name=" + name + ", code=" + code + ", type=" + type + ", subType="
        + subType + ", stateId=" + stateId + ", countryId=" + countryId + ", tenant=" + tenant
        + ", tenantRoot=" + tenantRoot + ", creatorId=" + creatorId + ", modifierId=" + modifierId
        + ", referenceId=" + referenceId + ", parentId=" + parentId + "]";
  }
  
}
