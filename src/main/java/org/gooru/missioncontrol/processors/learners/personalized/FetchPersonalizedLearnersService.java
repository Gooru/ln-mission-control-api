package org.gooru.missioncontrol.processors.learners.personalized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;


class FetchPersonalizedLearnersService {


  private final FetchPersonalizedLearnersDao fetchPersonalizedLearnersDao;

  private final String LEARNERS = "learners";


  FetchPersonalizedLearnersService(DBI dbi) {
    this.fetchPersonalizedLearnersDao = dbi.onDemand(FetchPersonalizedLearnersDao.class);
  }


  Map<String, List<PersonalizedLearnersModel>> fetchPersonalizedLearners() {
    final List<PersonalizedLearnersModel> personalizedLearners = fetchPersonalizedLearnersDao.fetchPersonalizedLearners();
    Map<String, List<PersonalizedLearnersModel>> personalizedLearnersMap = new HashMap<>();
    personalizedLearnersMap.put(LEARNERS, personalizedLearners);
    return personalizedLearnersMap;
  }


}
