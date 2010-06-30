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

import java.lang.annotation.Annotation;

import org.infinispan.Cache;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.name.Names;

import de.cosmocode.palava.core.inject.AbstractRebindModule;

/**
 * @author Oliver Lorenz
 */
public final class InfinispanCacheServiceModule extends AbstractRebindModule {

    private final String cacheName;
    private final Class<? extends Annotation> annotation;

    public InfinispanCacheServiceModule(String cacheName) {
        Preconditions.checkNotNull(cacheName, "CacheName");
        this.cacheName = cacheName;
        this.annotation = null;
    }

    public InfinispanCacheServiceModule(String cacheName, Class<? extends Annotation> annotation) {
        Preconditions.checkNotNull(cacheName, "CacheName");
        Preconditions.checkNotNull(annotation, "Annotation");
        this.cacheName = cacheName;
        this.annotation = annotation;
    }

    @Override
    protected void configuration() {
        bind(Cache.class).annotatedWith(NamedCache.class).to(Key.get(Cache.class, Names.named(cacheName)));
    }

    @Override
    protected void optionals() {
    }

    @Override
    protected void bindings() {
        if (annotation == null) {
            bind(CacheService.class).to(InfinispanCacheService.class).asEagerSingleton();
        } else {
            bind(CacheService.class).annotatedWith(annotation).to(InfinispanCacheService.class).asEagerSingleton();
        }
    }

    @Override
    protected void expose() {
        if (annotation == null) {
            expose(CacheService.class);
        } else {
            expose(CacheService.class).annotatedWith(annotation);
        }
    }
}

