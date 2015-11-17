package net.tweakers.objectpool;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;

/**
 * Variant that caches elements based on the Thread's id in a Caffeine Cache (successor to Guava).
 * See: https://github.com/ben-manes/caffeine
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */@State(Scope.Benchmark)
public class ThreadIdCaffeineMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private LoadingCache<Long, NumberFormat> cache;

	@Setup(Level.Iteration)
	public void setup()
	{
		cache = Caffeine.newBuilder()
				.initialCapacity(16)
				.maximumSize(MAX_CAPACITY)
				.build(
						new CacheLoader<Long, NumberFormat>() {
							@Override
							public NumberFormat load(Long key) throws Exception
							{
								return createNumberFormat();
							}
						}
				);
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		return cache.get(Thread.currentThread().getId());
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
