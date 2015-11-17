package net.tweakers.objectpool;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Variant that uses a normal (fast) queue, but uses a lock for synchronization.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class LockedArrayQueueMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private final Lock lock = new ReentrantLock();
	private Queue<NumberFormat> queue;

	@Setup(Level.Iteration)
	public void setup()
	{
		queue = new ArrayDeque<>();
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		lock.lock();
		try
		{
			NumberFormat format = queue.poll();

			if (format == null)
				format = createNumberFormat();

			return format;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		lock.lock();
		try
		{
			queue.offer(format);
		}
		finally
		{
			lock.unlock();
		}
	}
}
