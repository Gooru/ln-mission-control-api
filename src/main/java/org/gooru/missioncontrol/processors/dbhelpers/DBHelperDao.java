
package org.gooru.missioncontrol.processors.dbhelpers;

import java.util.List;
import org.gooru.missioncontrol.bootstrap.component.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public interface DBHelperDao {

  @Mapper(CountryModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM country_ds")
  public List<CountryModel> fetchCountries();

  @Mapper(CountryModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM country_ds WHERE id = ANY(:countryIds)")
  public List<CountryModel> fetchCountries(@Bind("countryIds") PGArray<Long> countryIds);

  @Mapper(StateModelMapper.class)
  @SqlQuery("SELECT id, name, code, country_id FROM state_ds WHERE id = ANY(:stateIds)")
  public List<StateModel> fetchStates(@Bind("stateIds") PGArray<Long> stateIds);

  @Mapper(SubjectModelMapper.class)
  @SqlQuery("SELECT id, title as name, code, taxonomy_subject_classification_id as category_id FROM taxonomy_subject WHERE id = ANY(:subjectIds)")
  public List<SubjectModel> fetchSubjects(@Bind("subjectIds") PGArray<String> subjectIds);

  @Mapper(SubjectCategoryModelMapper.class)
  @SqlQuery("SELECT id, title as name, code FROM taxonomy_subject_classification WHERE id = ANY (:categoryIds)")
  public List<SubjectCategoryModel> fetchSubjectCategories(@Bind("categoryIds") PGArray<String> categoryIds);

}
