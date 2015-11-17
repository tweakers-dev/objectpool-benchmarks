package net.tweakers.objectpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;

/**
 * Benchmark-variant with Apache Commons Pool2.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ApacheObjectPoolMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private ObjectPool<NumberFormat> pool;

	/**
	 * PooledObjectFactory for NumberFormat.
	 */
	private static class NumberFormatFactory extends BasePooledObjectFactory<NumberFormat>
	{
		@Override
		public NumberFormat create() throws Exception
		{
			return createNumberFormat();
		}

		@Override
		public PooledObject<NumberFormat> wrap(NumberFormat format)
		{
			return new DefaultPooledObject<>(format);
		}
	}

	@Setup(Level.Iteration)
	public void setup()
	{
		pool = new GenericObjectPool<NumberFormat>(new NumberFormatFactory());
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		try
		{
			return pool.borrowObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Impossible", e);
		}
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		try
		{
			pool.returnObject(format);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Impossible", e);
		}
	}
}
