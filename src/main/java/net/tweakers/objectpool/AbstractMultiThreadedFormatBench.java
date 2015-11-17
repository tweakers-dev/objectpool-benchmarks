package net.tweakers.objectpool;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Base set up of multi-threads benchmarks for thread-safe object reuse of NumberFormat.
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
public abstract class AbstractMultiThreadedFormatBench
{
	static {
		Locale.setDefault(new Locale("nl", "NL"));
	}

	/**
	 * If a pool needs a maximum-capacity indicater, use this many.
	 */
	protected static final int MAX_CAPACITY = 40;

	/**
	 * When selecting a concurrency level, use this one.
	 */
	protected static final int CONCURRENCY_LEVEL = 8;

	/**
	 * Get a NumberFormat that is savely usable.
	 *
	 * @return The NumberFormat.
	 */
	protected abstract NumberFormat getNumberFormat();

	/**
	 * Mark this NumberFormat as unused.
	 *
	 * @param format The returned NumberFormat.
	 */
	protected abstract void returnNumberFormat(NumberFormat format);

	/**
	 * Create a new NumberFormat-instance.
	 * @return The new instance.
	 */
	protected static NumberFormat createNumberFormat()
	{
		return NumberFormat.getNumberInstance();
	}

	/**
	 * Actually do the formatting.
	 *
	 * @return The formatted number.
	 */
	protected String doFormat()
	{
		NumberFormat numberFormat = getNumberFormat();
		try
		{
			return numberFormat.format(123_456.789);
		}
		finally
		{
			returnNumberFormat(numberFormat);
		}
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(1)
	public String formatThread01()
	{
		return doFormat();
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(2)
	public String formatThread02()
	{
		return doFormat();
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(4)
	public String formatThread04()
	{
		return doFormat();
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(8)
	public String formatThread08()
	{
		return doFormat();
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@Threads(16)
	public String formatThread16()
	{
		return doFormat();
	}
}
