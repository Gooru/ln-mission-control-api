package org.gooru.missioncontrol.bootstrap.component.jdbi;


import java.sql.Array;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class PostgresLongArrayArgumentFactory implements ArgumentFactory<PGArray<Long>> {
  @SuppressWarnings("unchecked")
  public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
    return value instanceof PGArray && ((PGArray) value).getType().isAssignableFrom(Long.class);
  }

  @Override
  public Argument build(Class<?> expectedType, final PGArray<Long> value, StatementContext ctx) {
    return (position, statement, ctx1) -> {
      Array ary = ctx1.getConnection().createArrayOf("bigint", value.getElements());
      statement.setArray(position, ary);
    };
  }
}

