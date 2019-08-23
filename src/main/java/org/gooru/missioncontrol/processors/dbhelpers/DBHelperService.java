
package org.gooru.missioncontrol.processors.dbhelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArrayUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public class DBHelperService {

  private final DBHelperDao dao;

  public DBHelperService(DBI dbi) {
    this.dao = dbi.onDemand(DBHelperDao.class);
  }

  public Map<Long, CountryModel> fetchCountries() {
    List<CountryModel> countries = this.dao.fetchCountries();
    return this.countriesMapper(countries);
  }

  public Map<Long, CountryModel> fetchCountries(List<Long> countryIds) {
    List<CountryModel> countries =
        this.dao.fetchCountries(PGArrayUtils.convertFromListLongToSqlArrayOfLong(countryIds));
    return this.countriesMapper(countries);
  }

  public Map<Long, StateModel> fetchStates(List<Long> stateIds) {
    List<StateModel> states =
        this.dao.fetchStates(PGArrayUtils.convertFromListLongToSqlArrayOfLong(stateIds));
    return this.statesMapper(states);
  }

  public Map<String, SubjectModel> fetchSubjects(List<String> subjectIds) {
    List<SubjectModel> subjects =
        this.dao.fetchSubjects(PGArrayUtils.convertFromListStringToSqlArrayOfString(subjectIds));
    return this.subjectsMapper(subjects);
  }

  public Map<String, SubjectCategoryModel> fetchSubjectCategories(List<String> categoryIds) {
    List<SubjectCategoryModel> categories = this.dao
        .fetchSubjectCategories(PGArrayUtils.convertFromListStringToSqlArrayOfString(categoryIds));
    return this.subjectCategoriesMapper(categories);
  }

  private Map<Long, CountryModel> countriesMapper(List<CountryModel> countries) {
    Map<Long, CountryModel> countryMap = new HashMap<>();
    countries.forEach(country -> {
      countryMap.put(country.getId(), country);
    });

    return countryMap;
  }

  private Map<Long, StateModel> statesMapper(List<StateModel> states) {
    Map<Long, StateModel> stateMap = new HashMap<>();
    states.forEach(state -> {
      stateMap.put(state.getId(), state);
    });

    return stateMap;
  }

  private Map<String, SubjectModel> subjectsMapper(List<SubjectModel> subjects) {
    Map<String, SubjectModel> subjectMap = new HashMap<>();
    subjects.forEach(subject -> {
      subjectMap.put(subject.getId(), subject);
    });

    return subjectMap;
  }

  private Map<String, SubjectCategoryModel> subjectCategoriesMapper(
      List<SubjectCategoryModel> categories) {
    Map<String, SubjectCategoryModel> categoryMap = new HashMap<>();
    categories.forEach(category -> {
      categoryMap.put(category.getId(), category);
    });

    return categoryMap;
  }
}
