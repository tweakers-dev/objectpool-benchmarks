# Set of JMH-benchmarks to tests alternatives for ThreadLocal

This is a set of JMH-benchmarks to test various types of alternatives for ThreadLocal within a web-application.
These alternatives are mainly intended for very quick use of slow-to-create objects like NumberFormat.

If you want to test it yourself, clone this repository and use maven to prepare your environment.
  `mvn install` should do the trick. Note: some of the code requires Java 1.8.

Afterwards you can run `java -jar target/benchmarks.jar -l` to see all the benchmarks.
For a 'quick' run, use `java -jar target/benchmarks.jar -f1` which will run all benchmarks once and create a nice summary at the end.

The benchmarks will take several minutes (they take 10 seconds per benchmark, with 5 benchmarks per class).

If you want to test your own implementation, just create a subclass of `AbstractMultiThreadedFormatBench` and run `mvn clean install`.
To actually run just your benchmark use `java -jar target/benchmarks.jar -f1 UniqueuPartOfYourBenchmark` (use -l to see which ones are matched).

See the [JMH website](http://openjdk.java.net/projects/code-tools/jmh/) for more information. And [Maven](http://maven.apache.org/) if you need help installing that.