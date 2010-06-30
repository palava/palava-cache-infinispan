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
import java.net.URL;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Tests {@link de.cosmocode.palava.cache.InfinispanCacheService} as a CacheService.
 *
 * @author Oliver Lorenz
 */
public class InfinispanCacheServiceTest extends CacheServiceTest {

    private static EmbeddedCacheManager manager;

    /**
     * Runs before class.
     */
    @BeforeClass
    public static void beforeClass() {
        final URL config = InfinispanCacheServiceTest.class.getClassLoader().getResource("infinispan.xml");

        try {
            manager = new DefaultCacheManager(config.openStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        manager.start();
    }

    @Override
    public CacheService unit() {
        return new InfinispanCacheService(manager.getCache("testcache"));
    }

    /**
     * Runs after class.
     */
    @AfterClass
    public static void afterClass() {
        manager.stop();
    }
    
}
