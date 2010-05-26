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

/**
 * The configuration for the infinispan cache ({@link InfinispanCacheService}).
 *
 * @author Oliver Lorenz
 */
public final class InfinispanCacheConfig {

    public static final String PREFIX = CacheConfig.PREFIX + "infinispan.";

    public static final String CONFIG = PREFIX + "config";

    public static final String NAME = PREFIX + "name";

    public static final String CACHE_MODE = PREFIX + "cacheMode";

    public static final String REPLICATION_MODE = PREFIX + "replicationMode";

    public static final String MAX_ENTRIES = PREFIX + "maxEntries";
    
    private InfinispanCacheConfig() {
        
    }

}
