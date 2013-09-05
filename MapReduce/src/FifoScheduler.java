import java.util.ArrayList;

import java.util.Collections;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * The Class FifoScheduler.This class implements the FIFO scheduler algorithm.
 * The scheduler breaks down Job object to Task objects. These Task objects are
 * then distributed to the all the WorkerNode’s queue that is not full. A Task
 * is feed to the WorkerNode’s queue with least or no Task in it.
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class FifoScheduler extends Scheduler {

	/** The node list. */
	private ArrayList<WorkerNode> nodeList;

	/** The queue. */
	private ArrayList<Task> queue;

	/** The job list. */
	private ArrayList<Job> jobList;

	/** The queue size. */
	private int queueSize;

	/** The m. */
	static int m = 0;

	/** The node list size. */
	int nodeListSize;

	/** The prng. */
	Random prng;

	/**
	 * Constructor breaks the Jobs into Task and places it in the queue.
	 * 
	 * @param sim
	 *            Simulation.
	 * @param jobList
	 *            the job list
	 * @param prng
	 *            Pseudorandom number generator.
	 * @param nodeList
	 *            the node list
	 */
	public FifoScheduler(Simulation sim, ArrayList<Job> jobList, Random prng,
			ArrayList<WorkerNode> nodeList) {
		super(sim);
		this.nodeList = nodeList;
		this.jobList = jobList;
		this.prng = prng;
		this.queueSize = jobList.size() * 3;
		this.queue = new ArrayList<Task>();
		int complexity = 0;
		int id = 0;
		int counter = 3;
		// Dividing the Jobs to Task object and place them in the queue.
		for (int i = 0; i < jobList.size(); i++) {
			complexity = jobList.get(i).getComplexity();
			id = jobList.get(i).getId();
			for (int j = 0; j < 3; j++) {
				queue.add(new Task(id, sim, respTimeSeries, complexity, 3));
			}
		}
		nodeListSize = nodeList.size();
		generateRequest();
	}

	/**
	 * Generate the next request.
	 */
	protected void generateRequest() {
		++m;
		// Place the tasks evenly across the cluster
		if ((nodeList.get(m - 1)).queueSize() != 5) {
			nodeList.get(m - 1).add(queue.get(0));
			queue.remove(0);
			++n;
		}
		// Sort the Worker Node with repect to their queue size
		if (m == nodeListSize) {
			Collections.sort(nodeList, new WorkerNode());
			m = 0;
		}
		// If the inner cluster of any task cannot process that task then
		// assign it to some other cluster.
		if (n < queueSize) {
			sim.doAfter(0.01, new Event() {
				public void perform() {
					generateRequest();
				}
			});
		}

	}
}
