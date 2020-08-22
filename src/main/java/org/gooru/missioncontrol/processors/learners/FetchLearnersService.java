package org.gooru.missioncontrol.processors.learners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArrayUtils;
import org.skife.jdbi.v2.DBI;


class FetchLearnersService {


  private final FetchLearnersDao fetchLearnersDao;
  private final String LEARNERS = "learners";
  private LearnerListCommand command;


  FetchLearnersService(DBI dbi) {
    this.fetchLearnersDao = dbi.onDemand(FetchLearnersDao.class);
  }


  Map<String, List<LearnersModel>> fetchLearners(LearnerListCommand command) {
    this.command = command;
    List<LearnersModel> learners = null;
    String groupUsers = fetchLearnersDao.fetchGroupUserAcl(command.getUserId());
    String replaceString =
        groupUsers.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").toString();
    String[] classIds = new String[] {replaceString};
    Map<String, List<LearnersModel>> learnersMap = new HashMap<>();
    if (classIds[0].equals("*")) {
      learners = fetchLearnersDao.fetchLearners(command.asBean());
      learnersMap.put(LEARNERS, learners);
    } else {
      learners = fetchLearnersDao.fetchLearners(command.setBean(command.getOffset(),
          command.getLimit(), command.getTenantId(), command.getQuery(),
          PGArrayUtils.convertFromListStringToSqlArrayOfUUID(Arrays.asList(classIds)),
          command.getSchoolIds()));
      learnersMap.put(LEARNERS, learners);
    }
    return learnersMap;
  }
}
