package org.gooru.missioncontrol.processors.researchprojects;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface FetchResearchProjectsDao {

  @Mapper(FetchResearchProjectsModelMapper.class)
  @SqlQuery("SELECT id, title, summary, category, description, teams, publications, data FROM research_projects")
  List<ResearchProjectsModel> fetchResearchProjects();
}
