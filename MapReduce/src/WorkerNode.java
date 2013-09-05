import java.util.Comparator;
import java.util.LinkedList;

import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * The Class WorkerNode.The Worker Node calculates the response time for every
 * Task it gets executed. Every WorkerNode cumulates the response time of all
 * the Task it executes in a Simulation. The WorkerNode with the highest
 * cumulative response time gives the response time for that simulation. The
 * WorkerNode also maintains queue for Task’s. For this simulation we maintain a
 * constant queue size of 5.<BR>
 * <BR>
 * 
 * Reference :Distributed Systems, Lecture Notes -- Module 11. Research Methods,
 * Prof. Alan Kaminsky -- Winter Quarter 2012,Department of Computer
 * Science,Rochester Institute of Technology
 */
public class WorkerNode implements Comparator<WorkerNode> {

	/** The name. */
	private String name;

	/** The sim. */
	private Simulation sim;

	/** The qmax. */
	private int qmax;

	/** The queue. */
	private LinkedList<Task> queue;

	/**
	 * True to print transcript, false to omit transcript.
	 */
	public boolean transcript = false;

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean. The server's maximum queue
	 * size is qmax.
	 * 
	 */

	public WorkerNode() {

	}

	/**
	 * Instantiates a new worker node.
	 * 
	 * @param name
	 *            the name
	 * @param sim
	 *            the sim
	 * @param prng
	 *            the prng
	 * @param qmax
	 *            the qmax
	 */
	public WorkerNode(String name, Simulation sim, Random prng, int qmax) {
		this.name = name;
		this.sim = sim;
		this.qmax = qmax;
		this.queue = new LinkedList<Task>();
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the number of requests in this server's queue.
	 * 
	 * @return Queue size.
	 */
	public int queueSize() {
		return queue.size();
	}

	/**
	 * Add the given Task to this WorkerNode's queue. If the queue is full,the
	 * Task is not added.
	 * 
	 * @param task
	 *            Task.
	 */
	public void add(final Task task) {

		if (queue.size() == qmax) {
			if (transcript)
				System.out.printf("%.3f %s dropped%n", sim.time(), task);
		} else {
			if (transcript)
				System.out.printf("%.3f %s added to %s%n", sim.time(), task,
						this);
			queue.addLast(task);
			if (queue.size() == 1)
				startProcessing();
		}
	}

	/**
	 * Start processing the first task in this WorkerNode queue.
	 */
	private void startProcessing() {
		Task task = queue.getFirst();
		if (transcript)
			System.out.printf(" %s starts processing %s%n", this, task);
		sim.doAfter(task.getTime(), new Event() {
			public void perform() {
				finishProcessing();
			}
		});
	}

	/**
	 * Finish processing the first task in this WorkerNode queue.
	 */
	private void finishProcessing() {
		Task task = queue.removeFirst();
		if (transcript)
			System.out.printf(" %s finishes processing %s%n", this, task);
		task.finish();
		if (!queue.isEmpty())
			startProcessing();
	}

	/**
	 * Returns a string version of this WorkerNode.
	 * 
	 * @return String version.
	 */
	public String toString() {
		return "Node " + name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(WorkerNode arg0, WorkerNode arg1) {
		int queueValue = arg0.queueSize() - arg0.queueSize();
		if (queueValue > 0) {
			return 1;
		} else if (queueValue < 0) {
			return -1;
		} else {
			return 0;
		}
	}
}