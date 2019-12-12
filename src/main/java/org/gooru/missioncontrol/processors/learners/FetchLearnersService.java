package org.gooru.missioncontrol.processors.learners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;


class FetchLearnersService {


  private final FetchLearnersDao fetchLearnersDao;

  private final String LEARNERS = "learners";


  FetchLearnersService(DBI dbi) {
    this.fetchLearnersDao = dbi.onDemand(FetchLearnersDao.class);
  }


  Map<String, List<LearnersModel>> fetchLearners() {
    final List<LearnersModel> learners = fetchLearnersDao.fetchLearners();
    Map<String, List<LearnersModel>> learnersMap = new HashMap<>();
    learnersMap.put(LEARNERS, learners);
    return learnersMap;
  }


}
