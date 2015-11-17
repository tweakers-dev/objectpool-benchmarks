package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.Semaphore;

/**
 * Variant that uses a semaphore and a array for the elements.
 * Based on the sample code in the javadoc:
 * http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Semaphore.html
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class SemaphoreArrayMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private final Semaphore available = new Semaphore(MAX_CAPACITY, true);

	// For just 16 items, an array is probably faster than most other data structures
	protected NumberFormat[] items = new NumberFormat[MAX_CAPACITY];
	protected boolean[] used = new boolean[MAX_CAPACITY];

	@Setup(Level.Iteration)
	public void setup()
	{
		for (int i = 0; i < MAX_CAPACITY; ++i)
		{
			items[i] = createNumberFormat();
		}
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		try
		{
			available.acquire();
			return getNextAvailableItem();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Impossible", e);
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		if (markAsUnused(format))
			available.release();
	}

	protected synchronized NumberFormat getNextAvailableItem()
	{
		for (int i = 0; i < MAX_CAPACITY; ++i)
		{
			if ( ! used[i])
			{
				used[i] = true;
				return items[i];
			}
		}

		return null; // not reached
	}

	protected synchronized boolean markAsUnused(NumberFormat item)
	{
		for (int i = 0; i < MAX_CAPACITY; ++i)
		{
			if (item == items[i])
			{
				if (used[i])
				{
					used[i] = false;
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
}
