import java.util.Comparator;

/**
 * The Class Job. This class maintains the Job id and complexity. Complexity of
 * the job ranges from 1 to 10 where Job with complexity 1 will require the
 * least amount to execute and job with complexity 10 will require 10 times more
 * time to execute, when compared to job with complexity 1.
 */
public class Job implements Comparator<Job> {

	/** The id. */
	int id;

	/** The complexity. */
	private int complexity;

	/**
	 * Instantiates a new job.
	 */
	public Job() {
	}

	/**
	 * Instantiates a new job.
	 * 
	 * @param id
	 *            the id
	 * @param complexity
	 *            the complexity
	 */
	public Job(int id, int complexity) {
		this.id = id;
		this.complexity = complexity;
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
	 * Gets the complexity.
	 * 
	 * @return the complexity
	 */
	public int getComplexity() {
		return complexity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Job arg0, Job arg1) {
		int idValue = arg0.getId() - arg0.getId();
		if (idValue > 0) {
			return 1;
		} else if (idValue < 0) {
			return -1;
		} else {
			return 0;
		}
	}
}
