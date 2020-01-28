package org.gooru.missioncontrol.processors.auth.signinuser;

import org.gooru.missioncontrol.processors.auth.signinuser.SignInUserCommand.SignInUserCommandBean;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;


interface SignInUserDao {

  @Mapper(SignInUserModelMapper.class)
  @SqlQuery("select id, email, first_name, last_name, password, display_name, designation, about, thumbnail, tenant_root_id as tenant_root, tenant_id from "
      + "admin_users where email = :email")
  SignInUserModel fetchUserByEmail(@BindBean SignInUserCommandBean model);
}
