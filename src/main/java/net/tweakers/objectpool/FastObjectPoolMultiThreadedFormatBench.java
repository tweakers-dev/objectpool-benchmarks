package net.tweakers.objectpool;

import com.haiwanwan.common.objectpool.ObjectFactory;
import com.haiwanwan.common.objectpool.ObjectPool;
import com.haiwanwan.common.objectpool.PoolConfig;
import com.haiwanwan.common.objectpool.Poolable;
import java.text.NumberFormat;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Variant that is based on 'fast object pool'.
 * https://github.com/DanielYWoo/fast-object-pool
 *
 * @author Arjen van der Meijden (acm@tweakers.net)
 */
@State(Scope.Benchmark)
public class FastObjectPoolMultiThreadedFormatBench extends AbstractMultiThreadedFormatBench
{
	private ObjectPool<NumberFormat> pool;

	@Setup(Level.Iteration)
	public void setup()
	{
		PoolConfig config = new PoolConfig();
		config.setPartitionSize(CONCURRENCY_LEVEL);
		config.setMaxSize(MAX_CAPACITY);
		config.setMinSize(16);
		config.setMaxIdleMilliseconds(60_000);

		ObjectFactory<NumberFormat> factory = new ObjectFactory<NumberFormat>() {
			@Override
			public NumberFormat create() {
				return createNumberFormat();
			}
			@Override
			public void destroy(NumberFormat o) {
			}
			@Override
			public boolean validate(NumberFormat o) {
				return true;
			}
		};

		pool = new ObjectPool<NumberFormat>(config, factory);
	}

	/**
	 * Overwrite to use try-with-resources rather than returnNumberFormat.
	 * @return
	 */
	protected String doFormat()
	{
		try(Poolable<NumberFormat> obj = pool.borrowObject())
		{
			return obj.getObject().format(123_456.789);
		}
	}

	@Override
	protected NumberFormat getNumberFormat()
	{
		return null;
	}

	@Override
	protected void returnNumberFormat(NumberFormat format)
	{
		// Nothing to do
	}
}
