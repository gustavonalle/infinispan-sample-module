package org.infinispan.samplemodule;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.samplemodule.api.SampleModuleDecorator;
import org.infinispan.test.MultipleCacheManagersTest;
import org.testng.annotations.Test;

@Test(groups = "unit", testName = "samples.SampleUsageTest")
public class SampleUsageTest extends MultipleCacheManagersTest {

   @Test
   public void demonstrateUsage() {
      Cache<String, String> cache1 = cacheManagers.get(0).getCache();
      Cache<String, String> cache2 = cacheManagers.get(1).getCache();

      SampleModuleDecorator<String, String> moduleApi1 = new SampleModuleDecorator<>(cache1);
      SampleModuleDecorator<String, String> moduleApi2 = new SampleModuleDecorator<>(cache2);

      cache1.put("1", "test");
      cache1.put("2", "test");
      cache1.put("3", "test");
      cache1.put("FOUR", "test");

      moduleApi1.performRemoteGCOnCluster();
      moduleApi2.printCacheContents();

      moduleApi1.bulkDelete("[0-9]*");
      moduleApi2.printCacheContents();
   }

   @Override
   protected void createCacheManagers() throws Throwable {
      ConfigurationBuilder config = getDefaultClusteredCacheConfig(CacheMode.REPL_SYNC, false);
      createCluster(config, 2);
      waitForClusterToForm();

   }
}
