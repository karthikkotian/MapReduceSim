import edu.rit.numeric.ListSeries;
import edu.rit.sim.Simulation;

/**
 * The Class Task.This class calculates the response time for each Task to get
 * executed. The response time is calculated using the Job complexity. If the
 * complexity of the job is 1 then it takes one unit of time, if the complexity
 * is n the it takes n * complexity unit of time. It also maintains the response
 * time in ListSeries.
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class Task {

	/** The id counter. */
	private static int idCounter = 0;

	/** The id. */
	private int id;

	/** The sim. */
	private Simulation sim;

	/** The start time. */
	private double startTime;

	/** The finish time. */
	private double finishTime;

	/** The resp time series. */
	private ListSeries respTimeSeries;

	/** The time. */
	private double time;

	/**
	 * Instantiates a new task.
	 * 
	 * @param sim
	 *            the sim
	 */
	public Task(Simulation sim) {
		this.id = ++idCounter;
		this.sim = sim;
		this.startTime = sim.time();
	}

	/**
	 * Construct a new Task. The request's start time is set to the current
	 * simulation time. The request's response time will be recorded in the
	 * given series.
	 * 
	 * @param id
	 *            the id
	 * @param sim
	 *            Simulation.
	 * @param series
	 *            Response time series.
	 * @param complexity
	 *            the complexity
	 * @param task
	 *            the task
	 */
	public Task(int id, Simulation sim, ListSeries series, int complexity,
			int task) {
		this(sim);
		this.id = id;
		this.respTimeSeries = series;
		this.time = complexity / task;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Mark this request as finished. The request's finish time is set to the
	 * current simulation time. The request's response time is recorded in the
	 * response time series.
	 */
	public void finish() {
		finishTime = sim.time();
		if (respTimeSeries != null)
			respTimeSeries.add(responseTime());
	}

	/**
	 * Returns this request's response time.
	 * 
	 * @return Response time.
	 */
	public double responseTime() {
		// System.out.println("Task time : " + (finishTime - startTime));
		return finishTime - startTime;
	}

	/**
	 * Returns a string version of this request.
	 * 
	 * @return String version.
	 */
	public String toString() {
		return "Task " + id;
	}
}