package org.gooru.missioncontrol.processors.researchprojects;

import io.vertx.core.json.JsonObject;

class ResearchProjectsModel {

  private Long id;
  private String title;
  private String summary;
  private String category;
  private String description;
  private String[] teams;
  private JsonObject publications;
  private String[] data;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public JsonObject getPublications() {
    return publications;
  }

  public void setPublications(JsonObject publications) {
    this.publications = publications;
  }

  public String[] getTeams() {
    return teams;
  }

  public void setTeams(String[] teams) {
    this.teams = teams;
  }

  public String[] getData() {
    return data;
  }

  public void setData(String[] data) {
    this.data = data;
  }





}
