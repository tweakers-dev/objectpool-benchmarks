package net.tweakers.objectpool;

import gnu.trove.impl.sync.TSynchronizedLongObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;

/**
 * Variant that stores elements based on the Thread's id in a synchronized TLongObjectMap.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ThreadIdTLongMapFixedMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private TLongObjectMap<NumberFormat> cache;

	@Setup(Level.Iteration)
	public void setup()
	{
		cache = new TSynchronizedLongObjectMap<NumberFormat>(new TLongObjectHashMap<>(16));
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		long key = Thread.currentThread().getId();
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
