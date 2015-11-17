package net.tweakers.objectpool;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Variant that just stores each element based on the Thread's id in a ConcurrentHashMap and doesn't bother too much with cleaning up.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ThreadIdConcurrentMapFixedMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private ConcurrentMap<Long, NumberFormat> cache;

	@Setup(Level.Iteration)
	public void setup()
	{
		cache = new ConcurrentHashMap<>(16, 0.5f, CONCURRENCY_LEVEL);
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		Long key = Thread.currentThread().getId();
		NumberFormat numberFormat = cache.get(key);

		if(numberFormat == null)
		{
			// Just clear the whole cache, rebuilding these objects is not _that_ expensive
			if(cache.size() > MAX_CAPACITY)
			{
				cache.clear();
			}

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
