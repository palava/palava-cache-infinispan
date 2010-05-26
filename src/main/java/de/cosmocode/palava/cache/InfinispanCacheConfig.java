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


    private static final String CONFIG_SUFFIX = "config";

    private static final String NAME_SUFFIX = "name";

    private static final String CACHE_MODE_SUFFIX = "cacheMode";

    private static final String REPLICATIN_MODE_SUFFIX = "replicationMode";

    private static final String MAX_ENTRIES_SUFFIX = "maxEntries";


    public static final String PREFIX = "infinispan.";

    public static final String CONFIG = PREFIX + CONFIG_SUFFIX;

    public static final String NAME = PREFIX + NAME_SUFFIX;

    public static final String CACHE_MODE = PREFIX + CACHE_MODE_SUFFIX;

    public static final String REPLICATION_MODE = PREFIX + REPLICATIN_MODE_SUFFIX;

    public static final String MAX_ENTRIES = PREFIX + MAX_ENTRIES_SUFFIX;

}
