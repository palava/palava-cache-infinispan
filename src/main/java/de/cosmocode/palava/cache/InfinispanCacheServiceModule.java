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

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import de.cosmocode.palava.core.inject.AbstractRebindingModule;
import de.cosmocode.palava.core.inject.Config;
import de.cosmocode.palava.core.inject.RebindModule;
import org.infinispan.config.Configuration;

import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * <p> Binds InfinispanCacheService to CacheService.
 * </p>
 * <p> For further documentation look at the constructor ({@link #InfinispanCacheServiceModule()}).
 * </p>
 *
 * @author Oliver Lorenz
 */
public final class InfinispanCacheServiceModule implements Module {

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
     *   <dt> infinispan.maxEntries (int) </dt>
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

    /**
     * <p> Rebinds all configuration entries using the specified prefix for configuration
     * keys and the supplied annoation for annotation rebindings.
     * </p>
     * <p> The config parameters must be given as <code> (prefix).infinispan.(...) </code>
     * </p>
     * <p> Have a look at the {@linkplain #InfinispanCacheServiceModule() constructor}
     * for a documentation on all configuration parameters.
     * </p>
     *
     * @param annotation the new binding annotation
     * @param prefix the prefix
     * @return a module which rebinds all required settings
     */
    public static RebindModule annotatedWith(Class<? extends Annotation> annotation, String prefix) {
        Preconditions.checkNotNull(annotation, "Annotation");
        Preconditions.checkNotNull(prefix, "Prefix");
        return new AnnotatedInstanceModule(annotation, prefix);
    }

    /**
     * Internal {@link RebindModule} implementation.
     *
     * @since 2.0
     * @author Oliver Lorenz
     * @author Willi Schoenborn
     */
    private static final class AnnotatedInstanceModule extends AbstractRebindingModule {

        private final Class<? extends Annotation> annotation;
        private final Config config;

        private AnnotatedInstanceModule(Class<? extends Annotation> annotation, String prefix) {
            this.annotation = annotation;
            this.config = new Config(prefix);
        }

        @Override
        protected void configuration() {
            bind(URL.class).annotatedWith(Names.named(InfinispanCacheConfig.CONFIG)).to(
                Key.get(URL.class, Names.named(config.prefixed(InfinispanCacheConfig.CONFIG))));
            bind(String.class).annotatedWith(Names.named(InfinispanCacheConfig.NAME)).to(
                Key.get(String.class, Names.named(config.prefixed(InfinispanCacheConfig.NAME))));
        }

        @Override
        protected void optionals() {
            bind(CacheMode.class).annotatedWith(Names.named(InfinispanCacheConfig.CACHE_MODE)).to(
                Key.get(CacheMode.class, Names.named(config.prefixed(InfinispanCacheConfig.CACHE_MODE))));
            bind(int.class).annotatedWith(Names.named(InfinispanCacheConfig.MAX_ENTRIES)).to(
                Key.get(int.class, Names.named(config.prefixed(InfinispanCacheConfig.MAX_ENTRIES))));
            bind(Configuration.CacheMode.class).
                annotatedWith(Names.named(InfinispanCacheConfig.REPLICATION_MODE)).to(
                Key.get(Configuration.CacheMode.class,
                Names.named(config.prefixed(InfinispanCacheConfig.REPLICATION_MODE))));
        }

        @Override
        protected void bindings() {
            bind(CacheService.class).annotatedWith(annotation).to(InfinispanCacheService.class).in(Singleton.class);
        }

        @Override
        protected void expose() {
            expose(CacheService.class).annotatedWith(annotation);
        }
    }

}
