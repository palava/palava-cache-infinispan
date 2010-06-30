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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * Infinispan cache implementation of {@link CacheService}.
 *
 * @author Oliver Lorenz
 */
final class InfinispanCacheService implements CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(InfinispanCacheService.class);
    private static final String MAX_AGE_NEGATIVE = "Max age must not be negative, but was %s";

    private long maxAge = DEFAULT_MAX_AGE;
    private TimeUnit maxAgeUnit = DEFAULT_MAX_AGE_TIMEUNIT;

    private Cache<Serializable, Object> cache;

    @Inject
    public InfinispanCacheService(@NamedCache Cache cache) {
        this.cache = cache;
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
