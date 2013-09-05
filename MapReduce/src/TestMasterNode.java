import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import pj.lib.edu.rit.numeric.AggregateXYSeries;
import pj.lib.edu.rit.numeric.ListSeries;
import pj.lib.edu.rit.numeric.Series;
import pj.lib.edu.rit.numeric.plot.Plot;
import pj.lib.edu.rit.sim.Simulation;
import pj.lib.edu.rit.util.Random;

/**
 * The Class TestMasterNode. This class is similar to MasterNode class. The only
 * difference is run all the three scheduler simultaneously and plots the graph
 * of all the three schedulers response time w.r.t. Number of jobs. Also it
 * generates no data to save heap memory.<BR>
 * 
 * Usage: java TestMasterNode <J> <T> <N> <seed><BR>
 * <J> = Maximum number of jobs<BR>
 * <T> = Number of Trails<BR>
 * <N> = size of the cluster<BR>
 * <seed> = Random seed<BR>
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class TestMasterNode {

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
		if (args.length != 4)
			usage();
		ListSeries NHseries1 = new ListSeries();
		ListSeries NHseries2 = new ListSeries();
		ListSeries NHseries3 = new ListSeries();
		ListSeries jobs = new ListSeries();

		qmax = 5;
		J = Integer.parseInt(args[0]);
		trail = Integer.parseInt(args[1]);
		N = Integer.parseInt(args[2]);
		seed = Long.parseLong(args[3]);

		// Set up pseudorandom number generator.
		prng = Random.getInstance(seed);
		jobList = new ArrayList<Job>();
		nodeList = new ArrayList<WorkerNode>();
		for (int j = 1; j <= J; j++) {
			ListSeries len1 = new ListSeries();
			ListSeries len2 = new ListSeries();
			ListSeries len3 = new ListSeries();
			// This loop iterates from 1 to maximum number of Jobs
			for (int t = 1; t <= trail; t++) {
				// This loop iterates for each scheduler.
				for (int z = 0; z < 3; z++) {
					selectScheduler = z + 1;
					nodeList.clear();
					jobList.clear();
					// Create Jobs
					for (int i = 0; i < j; i++) {
						jobList.add(new Job((prng.nextInt(10) + 1), (prng
								.nextInt(10) + 1)));
					}
					Collections.sort(jobList, new Job());
					// Set up simulation.
					sim = new Simulation();
					// Set up the cluster.
					for (int i = 0; i < N; i++) {
						nodeList.add(new WorkerNode(Integer.toString(prng
								.nextInt(10) + 1), sim, prng, qmax));
					}
					// Select the scheduler.
					if (selectScheduler == 1)
						scheduler = new FifoScheduler(sim, jobList, prng,
								nodeList);
					else if (selectScheduler == 2)
						scheduler = new FairScheduler(sim, jobList, prng,
								nodeList);
					else if (selectScheduler == 3)
						scheduler = new CapacityScheduler(sim, jobList, prng,
								nodeList);

					// Run the simulation.
					sim.run();
					if (selectScheduler == 1)
						len1.add(sim.time());
					else if (selectScheduler == 2)
						len2.add(sim.time());
					else if (selectScheduler == 3)
						len3.add(sim.time());

				}
			}
			jobs.add(j);
			Series.Stats responseTime1 = len1.stats();
			NHseries1.add(responseTime1.mean);
			Series.Stats responseTime2 = len2.stats();
			NHseries2.add(responseTime2.mean);
			Series.Stats responseTime3 = len3.stats();
			NHseries3.add(responseTime3.mean);
		}
		// Plot the graph for Response Time vs Number of Jobs for a constant
		// cluster. It plots the response time of all the three scheduler for
		// the same set of Jobs.
		new Plot().plotTitle(" Trail = " + trail + " Cluster Size = " + N)
				.xAxisTitle("Response Time for the job")
				.yAxisTitle("Number of jobs").seriesDots(null)
				.seriesColor(Color.BLUE)
				.xySeries(new AggregateXYSeries(NHseries1, jobs))
				.seriesColor(Color.RED)
				.xySeries(new AggregateXYSeries(NHseries2, jobs))
				.seriesColor(Color.GREEN)
				.xySeries(new AggregateXYSeries(NHseries3, jobs))
				.labelPosition(Plot.BELOW_LEFT).labelOffset(6)
				.labelColor(Color.BLUE).label("FIFO", 70.0, 50.0)
				.labelColor(Color.RED).label("FAIR", 70.0, 40.0)
				.labelColor(Color.GREEN).label("CAPACITY", 80.0, 30.0)
				.getFrame().setVisible(true);

	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println("Usage: java TestMasterNode <J> <T> <N> <seed>");
		System.err.println("<J> = Maximum number of jobs");
		System.err.println("<T> = Number of Trails");
		System.err.println("<N> = size of the cluster");
		System.err.println("<seed> = Random seed");
		System.exit(1);
	}
}
