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

import org.infinispan.Cache;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * Infinispan cache implementation of {@link CacheService}.
 *
 * @author Oliver Lorenz
 */
final class InfinispanCacheService implements CacheService {

    private final Cache<Serializable, Object> cache;

    @Inject
    @SuppressWarnings("unchecked")
    public InfinispanCacheService(@NamedCache Cache<?, ?> cache) {
        this.cache = (Cache<Serializable, Object>) Preconditions.checkNotNull(cache, "Cache");
    }

    @Override
    public void store(Serializable key, Object value) {
        Preconditions.checkNotNull(key, "Key");
        cache.put(key, value);
    }

    @Override
    public void store(Serializable key, Object value, CacheExpiration expiration) {
        Preconditions.checkNotNull(key, "Key");
        Preconditions.checkNotNull(expiration, "Expiration");

        // CacheExpiration considers 0 to be eternal, but infinispan considers negative values to be eternal
        final long lifespan = expiration.getLifeTime() == 0 ? -1 : expiration.getLifeTime();
        final long maxIdleTime = expiration.getIdleTime() == 0 ? -1 : expiration.getIdleTime();

        cache.put(key, value, lifespan, expiration.getLifeTimeUnit(), maxIdleTime, expiration.getIdleTimeUnit());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(Serializable key) {
        Preconditions.checkNotNull(key, "Key");
        return (T) cache.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T remove(Serializable key) {
        Preconditions.checkNotNull(key, "Key");
        return (T) cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
    
}
