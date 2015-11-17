package net.tweakers.objectpool;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

/**
 * Variant that caches elements based on the Thread's id in a Google Guava-cache.
 * See: https://github.com/google/guava/wiki/CachesExplained
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ThreadIdCacheTimedMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private LoadingCache<Long, NumberFormat> cache;

	@Setup(Level.Iteration)
	public void setup()
	{
		cache = CacheBuilder.newBuilder()
				.concurrencyLevel(CONCURRENCY_LEVEL)
				.expireAfterWrite(60, TimeUnit.SECONDS)
				.build(new CacheLoader<Long, NumberFormat>() {
					@Override
					public NumberFormat load(Long key) throws Exception
					{
						return createNumberFormat();
					}
				});
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		return cache.getUnchecked(Thread.currentThread().getId());
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
