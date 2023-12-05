package io.appform.dropwizard.sharding.dao.operations;

import io.appform.dropwizard.sharding.dao.operations.OpContext;
import io.appform.dropwizard.sharding.dao.operations.OpType;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.Session;

@Data
@SuperBuilder
public class Save<T, R> extends OpContext<R> {

  @NonNull
  private T entity;
  @NonNull
  private UnaryOperator<T> saver;
  @Builder.Default
  private Function<T, R> afterSave = t -> (R) t;

  @Override
  public R apply(Session session) {
    T result = saver.apply(entity);
    return afterSave.apply(result);
  }

  @Override
  public @NonNull OpType getOpType() {
    return OpType.SAVE;
  }
}