/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.cache;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.CacheManager;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.palava.core.lifecycle.Initializable;
import de.cosmocode.palava.core.lifecycle.LifecycleException;

/**
 * Infinispan cache implementation of {@link CacheService}.
 *
 * @author Oliver Lorenz
 */
final class InfinispanCacheService implements CacheService, Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(InfinispanCacheService.class);
    private static final String MAX_AGE_NEGATIVE = "Max age must not be negative, but was %s";

    private final CacheManager manager;
    private final Configuration config;
    private final String name;

    private long maxAge = DEFAULT_MAX_AGE;
    private TimeUnit maxAgeUnit = DEFAULT_MAX_AGE_TIMEUNIT;

    private Cache<Serializable, Object> cache;

    @Inject
    public InfinispanCacheService(
        @Named(InfinispanCacheConfig.CONFIG) URL config,
        @Named(InfinispanCacheConfig.NAME) String name) throws IOException {
        
        this.manager = new DefaultCacheManager(config.openStream());
        this.config = new Configuration();
        this.name = name;
    }

    /**
     * Sets the cache mode for this infinispan cache.
     * 
     * @param cacheMode the cache mode
     */
    @Inject(optional = true)
    void setCacheMode(@Named(InfinispanCacheConfig.CACHE_MODE) final CacheMode cacheMode) {
        this.config.setEvictionStrategy(of(cacheMode));
    }

    /**
     * Sets the replication mode ({@link Configuration.CacheMode}).
     * 
     * @param replicationMode replication mode
     */
    @Inject(optional = true)
    void setReplicationMode(
        @Named(InfinispanCacheConfig.REPLICATION_MODE) Configuration.CacheMode replicationMode) {
        this.config.setCacheMode(replicationMode);
    }

    /**
     * Sets the maximum number of entries in the cache.
     * 
     * @param maxEntries maximum number of entries in the cache
     */
    @Inject(optional = true)
    void setMaxEntries(@Named(InfinispanCacheConfig.MAX_ENTRIES) final int maxEntries) {
        Preconditions.checkState(maxEntries >= 0, "Max entries must not be negative, but was %s", maxEntries);
        this.config.setEvictionMaxEntries(maxEntries);
    }

    private EvictionStrategy of(CacheMode mode) {
        switch (mode) {
            case LRU: {
                return EvictionStrategy.LRU;
            }
            case FIFO: {
                return EvictionStrategy.FIFO;
            }
            case UNLIMITED: {
                return EvictionStrategy.NONE;
            }
            default: {
                throw new UnsupportedOperationException(mode.name());
            }
        }
    }


    @Override
    public void initialize() throws LifecycleException {
        manager.defineConfiguration(name, config);
        cache = manager.getCache(name);
        LOG.debug("Initializing cache {} with name {}", cache, name);
    }

    @Override
    public long getMaxAge() {
        return getMaxAge(TimeUnit.SECONDS);
    }

    @Override
    public long getMaxAge(TimeUnit unit) {
        return unit.convert(maxAge, maxAgeUnit);
    }

    @Override
    public void setMaxAge(long maxAgeSeconds) {
        setMaxAge(maxAgeSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setMaxAge(long newMaxAge, TimeUnit newMaxAgeUnit) {
        Preconditions.checkArgument(newMaxAge >= 0, MAX_AGE_NEGATIVE, newMaxAge);
        Preconditions.checkNotNull(newMaxAgeUnit, "MaxAge TimeUnit");

        this.maxAge = newMaxAge;
        this.maxAgeUnit = newMaxAgeUnit;
    }

    @Override
    public void store(Serializable key, Object value) {
        Preconditions.checkState(cache != null, "Cache is not initialized");
        Preconditions.checkNotNull(key, "Key");

        if (maxAge == DEFAULT_MAX_AGE && maxAgeUnit == DEFAULT_MAX_AGE_TIMEUNIT) {
            cache.put(key, value);
        } else {
            cache.put(key, value, maxAge, maxAgeUnit);
        }
    }

    @Override
    public void store(Serializable key, Object value, long customMaxAge, TimeUnit customMaxAgeUnit) {
        Preconditions.checkState(cache != null, "Cache is not initialized");
        Preconditions.checkNotNull(key, "Key");
        Preconditions.checkArgument(customMaxAge >= 0, MAX_AGE_NEGATIVE, customMaxAge);
        Preconditions.checkNotNull(customMaxAgeUnit, "MaxAge TimeUnit");

        cache.put(key, value, customMaxAge, customMaxAgeUnit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(Serializable key) {
        Preconditions.checkState(cache != null, "Cache is not initialized");
        Preconditions.checkNotNull(key, "Key");

        return (T) cache.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T remove(Serializable key) {
        Preconditions.checkState(cache != null, "Cache is not initialized");
        Preconditions.checkNotNull(key, "Key");

        return (T) cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
