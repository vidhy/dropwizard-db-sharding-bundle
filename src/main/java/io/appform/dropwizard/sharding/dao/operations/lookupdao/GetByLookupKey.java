package io.appform.dropwizard.sharding.dao.operations.lookupdao;

import io.appform.dropwizard.sharding.dao.operations.OpContext;
import io.appform.dropwizard.sharding.dao.operations.OpType;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;

@Data
@SuperBuilder
public class GetByLookupKey<T, R> extends OpContext<R> {

  @NonNull
  private String id;
  @NonNull
  private BiFunction<String, UnaryOperator<Criteria>, T> getter;
  @Builder.Default
  private UnaryOperator<Criteria> criteriaUpdater = t -> t;
  @Builder.Default
  private Function<T, R> afterGet = t -> (R) t;


  @Override
  public R apply(Session session) {
    T result = getter.apply(id, criteriaUpdater);
    return afterGet.apply(result);
  }

  @Override
  public @NonNull OpType getOpType() {
    return OpType.GET_BY_LOOKUP_KEY;
  }
}