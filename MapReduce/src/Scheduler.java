import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;

/**
 * The Class Scheduler.This is a abstract class. It maintains the Simulation
 * class object. It also collects the response time of each task. The classes
 * extending Scheduler implements generateRequest() method.<BR>
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public abstract class Scheduler {

	/** The sim. */
	protected Simulation sim;

	/** The resp time series. */
	protected ListSeries respTimeSeries;

	/** The n. */
	protected int n;

	/**
	 * Create a new request generator.
	 * 
	 * @param sim
	 *            Simulation.
	 */
	public Scheduler(Simulation sim) {
		this.sim = sim;
		respTimeSeries = new ListSeries();
		n = 0;
	}

	/**
	 * Generate the next request.
	 */
	protected abstract void generateRequest();

	/**
	 * Returns a data series containing the response time statistics of the
	 * generated Tasks.
	 * 
	 * @return Response time series.
	 */
	public Series responseTimeSeries() {
		return respTimeSeries;
	}

	/**
	 * Returns the response time statistics of the generated Tasks.
	 * 
	 * @return Response time statistics (mean, standard deviation, variance).
	 */
	public Series.Stats responseTimeStats() {
		return respTimeSeries.stats();
	}
}