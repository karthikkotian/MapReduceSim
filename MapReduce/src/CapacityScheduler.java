import java.awt.Event;
import java.util.ArrayList;

import edu.rit.util.Random;

/**
 * This class implements the Capacity scheduler algorithm. The scheduler breaks
 * down Job object to Task objects. The number of Task object depends on the
 * size of the inner cluster. WorkerNode objects with same id are part of the
 * same inner cluster and consumes Task object with same Task Id as theirs. If
 * the queue of WorkerNode’s is full in the same inner cluster then it allocates
 * the Task to WorkerNode with least or no Task in it.The priority of the task
 * is given by its id.
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 * 
 */
public class CapacityScheduler extends Scheduler {

	/** The Worker node list. */
	private ArrayList<WorkerNode> nodeList;

	/** The queue of this Scheduler. */
	private ArrayList<Task> queue;

	/** The job list. */
	private ArrayList<Job> jobList;

	/** The queue size of this Schedler. */
	private int queueSize;

	/** The m. */
	private static int m = 0;

	/** The Worker node list size. */
	private int nodeListSize;

	/** The prng. */
	private Random prng;

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
	public CapacityScheduler(Simulation sim, ArrayList<Job> jobList,
			Random prng, ArrayList<WorkerNode> nodeList) {
		super(sim);
		this.nodeList = nodeList;
		nodeListSize = nodeList.size();
		this.jobList = jobList;
		this.prng = prng;
		this.queue = new ArrayList<Task>();
		int complexity = 0;
		int id = 0;
		int index = 0;
		// Calculate the individual cluster size
		for (int k = 1; k <= 10; k++) {
			queueSize = 0;
			for (int i = 0; i < nodeListSize; i++) {
				if (Integer.parseInt(nodeList.get(i).getName()) == k) {
					queueSize++;
				}
			}
			index = 0;
			// Divide the Job into Tasks.Tasks are placed in the queue with
			// respect to their proirity.
			for (int i = 0; i < jobList.size(); i++) {
				if (jobList.get(i).getId() == k) {
					complexity = jobList.get(i).getComplexity();
					id = jobList.get(i).getId();
					// Split the Job into 2 Task if the Job don't belong to any
					// inner cluster.
					if (queueSize == 0) {
						queue.add(new Task(id, sim, respTimeSeries, complexity,
								2));
						queue.add(new Task(id, sim, respTimeSeries, complexity,
								2));
					} else {
						for (int j = 0; j < queueSize; j++) {
							queue.add(new Task(id, sim, respTimeSeries,
									complexity, queueSize));
						}
					}
				}
			}
		}
		queueSize = queue.size();
		generateRequest();
	}

	/**
	 * Generate the next request.
	 */
	protected void generateRequest() {
		int i = 0;
		while (i < nodeList.size()) {
			// Assign to Task to its inner cluster.
			if (Integer.parseInt(nodeList.get(i).getName()) == queue.get(0)
					.getId()) {
				if (nodeList.get(i).queueSize() != 5) {
					nodeList.get(i).add(queue.get(0));
					queue.remove(0);
					break;
				}
			}
			// If the inner cluster of any task cannot process that task then
			// assign it to some other cluster.
			if (i == nodeList.size() - 1) {
				for (int j = 0; j < nodeList.size(); j++) {
					if (nodeList.get(j).queueSize() != 5) {
						nodeList.get(j).add(queue.get(0));
						queue.remove(0);
						break;
					}
				}
				break;
			}
			i++;
		}
		++n;
		// Add the event to Simulation.
		if (n < queueSize) {
			sim.doAfter(0.05, new Event() {
				public void perform() {
					generateRequest();
				}
			});
		}

	}
}
