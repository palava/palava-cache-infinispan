package de.cosmocode.palava.cache;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Tests {@link de.cosmocode.palava.cache.InfinispanCacheService} as a CacheService.
 *
 * @author Oliver Lorenz
 */
public class InfinispanCacheServiceTest extends CacheServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(InfinispanCacheServiceTest.class);

    private static URL config;

    @BeforeClass
    public static void loadConfig() {
        config = InfinispanCacheServiceTest.class.getClassLoader().getResource("infinispan.xml");
        LOG.debug("Config: {}", config);
    }

    @Override
    public CacheService unit() {
        final InfinispanCacheService service;
        try {
            service = new InfinispanCacheService(config, "test");
        } catch (IOException e) {
            LOG.error("An IO exception occured in constructor", e);
            throw new IllegalStateException(e);
        }
        service.initialize();
        return service;
    }
}
