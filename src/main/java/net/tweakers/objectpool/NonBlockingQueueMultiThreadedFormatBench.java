package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Variant that uses a non-blocking queue (and unlimited size) for items.
 * A returned item is just put back in the queue and if the queue has no items, a new item is created.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class NonBlockingQueueMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private Queue<NumberFormat> queue;

	@Setup(Level.Iteration)
	public void setup()
	{
		queue = new ConcurrentLinkedQueue<>();
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		NumberFormat format = queue.poll();

		if(format == null)
			format = createNumberFormat();

		return format;
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		queue.offer(format);
	}
}
