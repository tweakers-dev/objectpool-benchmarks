package net.tweakers.objectpool;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Variant that uses a normal (fast) queue, but uses a synchronized object for synchronization.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class SynchronizedArrayQueueMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private final Object lock = new Object();
	private Queue<NumberFormat> queue;

	@Setup(Level.Iteration)
	public void setup()
	{
		queue = new ArrayDeque<>();
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		synchronized (lock)
		{
			NumberFormat format = queue.poll();

			if (format == null)
				format = createNumberFormat();

			return format;
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		synchronized (lock)
		{
			queue.offer(format);
		}
	}
}
