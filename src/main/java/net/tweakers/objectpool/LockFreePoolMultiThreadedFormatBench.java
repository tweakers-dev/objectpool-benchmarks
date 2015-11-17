package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Variant on object pool that has supposedly no locks and thus better scaling.
 * From:
 * http://daviddengcn.blogspot.nl/2015/02/a-lock-free-java-object-pool.html
 * https://github.com/daviddengcn/lockfreepool/blob/master/ObjectPool.java
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class LockFreePoolMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	/**
	 * The cached object stack.
	 */
	private AtomicReferenceArray<NumberFormat> objects;
	/**
	 * The index in {@link #objects} of the first empty element.
	 */
	private final AtomicInteger top = new AtomicInteger(0);

	@Setup(Level.Iteration)
	public void setup()
	{
		objects = new AtomicReferenceArray<NumberFormat>(16);
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		while (true)
		{
			// Try reserve a cached object in objects
			int n;
			do
			{
				n = top.get();
				if (n == 0)
				{
					// No cached oobjects, allocate a new one
					return createNumberFormat();
				}
			}
			while ( ! top.compareAndSet(n, n - 1));

			// Try fetch the cached object
			NumberFormat e = objects.getAndSet(n, null);
			if (e != null)
			{
				return e;
			}
			// It is possible that the reserved object was extracted before
			// the current thread tried to get it. Let's start over again.
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		while (true)
		{
			// Try reserve a place in this.objects for e.
			int n;
			do
			{
				n = top.get();
				if (n >= objects.length() - 1)
				{
					// List is full
					return;
				}
			}
			while ( ! top.compareAndSet(n, n + 1));

			// Try put e at the reserved place.
			if (objects.compareAndSet(n + 1, null, format))
			{
				return;
			}
			// It is possible that the reserved place was occupied before
			// the current thread tried to put e in it. Let's start over again.
		}
	}
}
