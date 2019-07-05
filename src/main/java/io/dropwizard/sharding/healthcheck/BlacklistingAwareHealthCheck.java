package io.dropwizard.sharding.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.sharding.sharding.ShardManager;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BlacklistingAwareHealthCheck extends HealthCheck {

    private final int shardId;
    private final HealthCheck baseHealthCheck;
    private final ShardManager shardManager;

    public BlacklistingAwareHealthCheck(int shardId, HealthCheck healthCheck, ShardManager shardManager) {
        this.shardId = shardId;
        this.baseHealthCheck = healthCheck;
        this.shardManager = shardManager;
    }

    @Override
    protected Result check() {
        if (shardManager.isBlacklisted(shardId)) {
            log.info("returning healthy since shard is blacklisted [{}]", shardId);
            return Result.healthy();
        }
        return baseHealthCheck.execute();
    }
}
