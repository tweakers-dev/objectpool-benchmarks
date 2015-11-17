package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Variant that uses a ArrayBlockingQueue to allow a certain fixed maximum amount of objects to be pooled around.
 * The max is currently higher than the highest amount of threads used in the benchmarks.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class BlockingQueueMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private BlockingQueue<NumberFormat> queue;

	@Setup(Level.Iteration)
	public void setup()
	{
		queue = new ArrayBlockingQueue<NumberFormat>(MAX_CAPACITY, false);
		for(int i = 0; i < MAX_CAPACITY; i++)
		{
			queue.offer(createNumberFormat());
		}
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		try
		{
			return queue.take();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Problem with taking item", e);
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		try
		{
			queue.put(format);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
