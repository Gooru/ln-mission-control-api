package org.gooru.missioncontrol.processors.researchprojects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;


class FetchResearchProjectsService {


  private final FetchResearchProjectsDao fetchResearchProjectsDao;

  private static final String RESEARCH_PROJECTS = "researchProjects";


  FetchResearchProjectsService(DBI dbi) {
    this.fetchResearchProjectsDao = dbi.onDemand(FetchResearchProjectsDao.class);
  }


  Map<String, List<ResearchProjectsModel>> fetchResearchProjects() {
    final List<ResearchProjectsModel> projects = fetchResearchProjectsDao.fetchResearchProjects();
    Map<String, List<ResearchProjectsModel>> researchProjects = new HashMap<>();
    researchProjects.put(RESEARCH_PROJECTS, projects);
    return researchProjects;
  }


}
