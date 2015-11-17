package net.tweakers.objectpool;

import java.text.NumberFormat;
import org.openjdk.jmh.annotations.*;

import java.util.Locale;

/**
 * Base line variant to test the performance of reusing a property-object in a single thread.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class SingleThreadedFormatBench
{
	static {
		Locale.setDefault(new Locale("nl", "NL"));
	}

	private NumberFormat numberFormat;

	@Setup(Level.Iteration)
	public void setup()
	{
		numberFormat = NumberFormat.getNumberInstance();
	}

	protected String doFormat()
	{
		return numberFormat.format(123_456.789);
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(1)
	public String formatThread01()
	{
		return doFormat();
	}
}
