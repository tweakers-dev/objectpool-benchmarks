package net.tweakers.objectpool;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.text.NumberFormat;

/**
 * Variant that stores elements in a ThreadLocal.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class ThreadLocalMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private ThreadLocal<NumberFormat> formatThreadLocal = new ThreadLocal<NumberFormat>(){
		@Override
		protected NumberFormat initialValue()
		{
			return createNumberFormat();
		}
	};

	@Override
	protected NumberFormat getNumberFormat()
	{
		return formatThreadLocal.get();
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
