package net.tweakers.objectpool;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;
import java.util.concurrent.ConcurrentMap;

/**
 * Variant that uses a ConcurrentLinkedHashMap (improved version of Google Guave Cache).
 * See: https://github.com/ben-manes/concurrentlinkedhashmap
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ThreadIdConcurrentLinkedMapMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private ConcurrentMap<Long, NumberFormat> cache;

	@Setup(Level.Iteration)
	public void setup()
	{
		cache = new ConcurrentLinkedHashMap.Builder<Long, NumberFormat>()
				.concurrencyLevel(CONCURRENCY_LEVEL)
				.initialCapacity(16)
				.maximumWeightedCapacity(MAX_CAPACITY)
				.build();
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		Long key = Thread.currentThread().getId();
		NumberFormat numberFormat = cache.get(key);

		if(numberFormat == null)
		{
			numberFormat = createNumberFormat();
			cache.put(key, numberFormat);
		}

		return numberFormat;
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
