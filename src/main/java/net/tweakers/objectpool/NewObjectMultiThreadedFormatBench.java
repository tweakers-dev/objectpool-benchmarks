package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * Variant that just creates a new NumberFormat-instance for each call.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class NewObjectMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	@Override
	protected NumberFormat getNumberFormat()
	{
		return createNumberFormat();
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
