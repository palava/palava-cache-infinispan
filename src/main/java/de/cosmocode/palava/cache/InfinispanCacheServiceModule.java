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

import com.google.inject.Binder;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> Binds InfinispanCacheService to CacheService.
 * </p>
 * <p> For further documentation look at the constructor ({@link #InfinispanCacheServiceModule()}).
 * </p>
 *
 * @author Oliver Lorenz
 */
public final class InfinispanCacheServiceModule implements Module {

    private static final Logger LOG = LoggerFactory.getLogger(InfinispanCacheServiceModule.class);

    /**
     * <h3> Binds the infinispan cache implementation to CacheService. </h3>
     * <h4> Necessary guice parameters: </h4>
     * <dl>
     *   <dt> infinispan.config (URL) </dt>
     *   <dd> The xml configuration file for infinispan, as a URL. </dd>
     *   <dt> infinispan.name (String) </dt>
     *   <dd> The name for the infinispan cache configuration.
     *        This can be a pre-configured entry in the xml or directly. </dd>
     * </dl>
     * <h4> Optional parameters: </h4>
     * <dl>
     *   <dt> infinispan.cacheMode ({@link de.cosmocode.palava.cache.CacheMode}) </dt>
     *   <dd> The mode to use when the cache overflows and elements have to be removed from cache.
     *        Possible values:
     *        {@linkplain CacheMode#FIFO FIFO},
     *        {@linkplain CacheMode#LRU LRU},
     *        {@linkplain CacheMode#UNLIMITED UNLIMITED}
     *   </dd>
     *   <dt> infinispan.maxEntries </dt>
     *   <dd> The maximum number of elements in cache, at any time </dd>
     *   <dt> infinispan.replicationMode ({@link org.infinispan.config.Configuration.CacheMode}) </dt>
     *   <dd> Cache replication mode. Possible values:
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#LOCAL LOCAL},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#REPL_SYNC REPL_SYNC},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#REPL_ASYNC REPL_ASYNC},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#INVALIDATION_SYNC INVALIDATION_SYNC},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#INVALIDATION_ASYNC INVALIDATION_ASYNC},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#DIST_SYNC DIST_SYNC},
     *        {@linkplain org.infinispan.config.Configuration.CacheMode#DIST_ASYNC DIST_ASYNC}
     *   </dd>
     * </dl>
     */
    public InfinispanCacheServiceModule() {
        // constructor does nothing special, but is used for documentation
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(CacheService.class).to(InfinispanCacheService.class).asEagerSingleton();
    }

    // TODO: named RebindModule, or with annotation (like FileSystemStoreModule)

}
