import java.awt.Event;
import java.util.ArrayList;
import java.util.Collections;

import edu.rit.util.Random;

/**
 * The Class FairScheduler.This class implements the Fair scheduler algorithm.
 * The scheduler breaks down Job object to N number of Task objects where N is
 * the cluster size.
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class FairScheduler extends Scheduler {

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
	public FairScheduler(Simulation sim, ArrayList<Job> jobList, Random prng,
			ArrayList<WorkerNode> nodeList) {
		super(sim);
		this.nodeList = nodeList;
		this.jobList = jobList;
		this.prng = prng;
		this.queueSize = jobList.size() * nodeList.size();
		this.queue = new ArrayList<Task>();
		int complexity = 0;
		int id = 0;
		// Dividing the Jobs to Task object and place them in the queue.
		for (int i = 0; i < queueSize; i++) {
			if (i % 3 == 0) {
				complexity = jobList.get(i / nodeList.size()).getComplexity();
				id = jobList.get(i / nodeList.size()).getId();
			}
			queue.add(new Task(id, sim, respTimeSeries, complexity, nodeList
					.size()));
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
