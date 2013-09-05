import java.util.ArrayList;
import java.util.Collections;

import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;
import java.awt.Color;

/**
 * The Class MasterNode. This class contains the main method and runs the
 * discrete event simulation. It initializes Job objects and WorkerNode objects
 * (Cluster) and passes these objects to Scheduler Object. It creates the Class
 * Simulation object and executes Simulation’s run() method to start the
 * simulation.
 * 
 * Usage: java MasterNode <sched> <J> <T> <N> <seed><BR>
 * <sched> = 1. FIFO 2. Fair 3.Capacity<BR>
 * <J> = Maximum number of jobs<BR>
 * <T> = Number of Trails<BR>
 * <N> = size of the cluster<BR>
 * <seed> = Random seed<BR>
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class MasterNode {

	/** The qmax. */
	private static int qmax;

	/** The complexity. */
	private static int complexity;

	/** The seed. */
	private static long seed;

	/** The trail. */
	private static int trail;

	/** The prng. */
	private static Random prng;

	/** The sim. */
	private static Simulation sim;

	/** The scheduler. */
	private static Scheduler scheduler;

	/** The job. */
	private static Job job;

	/** The n. */
	private static int N;

	/** The node list. */
	private static ArrayList<WorkerNode> nodeList;

	/** The job list. */
	private static ArrayList<Job> jobList;

	/** The select scheduler. */
	private static int selectScheduler;

	/** The j. */
	private static int J;

	/**
	 * Main program.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// Parse command line arguments.
		if (args.length != 5)
			usage();
		ListXYSeries NHseries = new ListXYSeries();
		qmax = 5;
		selectScheduler = Integer.parseInt(args[0]);
		J = Integer.parseInt(args[1]);
		trail = Integer.parseInt(args[2]);
		N = Integer.parseInt(args[3]);
		seed = Long.parseLong(args[4]);

		// Set up pseudorandom number generator.
		prng = Random.getInstance(seed);
		jobList = new ArrayList<Job>();
		nodeList = new ArrayList<WorkerNode>();
		System.out.println("Response time " + "  Response mean time  "
				+ "  Response time ");
		System.out.println("for the job    " + "  of the worker nodes"
				+ "  stddev");
		// This loop iterates from 1 to maximum number of Jobs
		for (int j = 1; j <= J; j++) {
			ListSeries len = new ListSeries();
			ListSeries meanResponseTime = new ListSeries();
			ListSeries stdDev = new ListSeries();
			// This loop is for trails.
			for (int t = 1; t <= trail; t++) {
				nodeList.clear();
				jobList.clear();
				// Create the Jobs
				for (int i = 0; i < j; i++) {
					jobList.add(new Job((prng.nextInt(10) + 1), (prng
							.nextInt(10) + 1)));
				}
				Collections.sort(jobList, new Job());
				// Set up simulation.
				sim = new Simulation();
				// Set up the cluster
				for (int i = 0; i < N; i++) {
					nodeList.add(new WorkerNode(Integer.toString(prng
							.nextInt(10) + 1), sim, prng, qmax));
				}

				// Select the scheduler.
				if (selectScheduler == 1)
					scheduler = new FifoScheduler(sim, jobList, prng, nodeList);
				else if (selectScheduler == 2)
					scheduler = new FairScheduler(sim, jobList, prng, nodeList);
				else if (selectScheduler == 3)
					scheduler = new CapacityScheduler(sim, jobList, prng,
							nodeList);

				// Run the simulation.
				sim.run();
				// Add the simulation time.
				len.add(sim.time());
				Series.Stats stats = scheduler.responseTimeStats();
				meanResponseTime.add(stats.mean);
				stdDev.add(stats.stddev);
			}
			Series.Stats responseTime = len.stats();
			Series.Stats mean = meanResponseTime.stats();
			Series.Stats stddev = stdDev.stats();
			NHseries.add(responseTime.mean, j);

			// Print the response time mean and standard deviation.
			System.out.printf(" %.3f               %.3f            %.3f\n",
					responseTime.mean, mean.mean, stddev.mean);

		}
		// Plot the graph for Response Time vs Number of Jobs for a constant
		// cluster.
		new Plot().xAxisTitle("Response Time for the job")
				.yAxisTitle("Number of jobs").seriesStroke(null)
				.xySeries(NHseries).seriesStroke(Strokes.solid(1))
				.seriesColor(Color.RED).seriesDots(null).getFrame()
				.setVisible(true);
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err
				.println("Usage: java MasterNode <sched> <J> <T>  <N> <seed>");
		System.err.println("<sched> = 1. FIFO 2. Fair 3.Capacity");
		System.err.println("<J> = Maximum number of jobs");
		System.err.println("<T> = Number of Trails");
		System.err.println("<N> = size of the cluster");
		System.err.println("<seed> = Random seed");
		System.exit(1);
	}
}
