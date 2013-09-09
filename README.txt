MapReduceSim
============

A Comparison of Scheduling Algorithms in  MapReduce Systems

MapReduce is a model used for processing large data sets in parallel across a 
distributed cluster of computers. Generally, the process happens in two phases, a map 
phase and a reduce phase. In the first phase, a very large problem is split into smaller 
tasks and then distributed among many peers. Those peers then individually solve the 
smaller problems. Once the smaller problems have been evaluated, their results are 
gathered in the second phase and reduced into one final result. Ultimately, harnessing a 
large number of peers and applying a divide-and-conquer methodology can, dramatically 
reduce the completion time of jobs, when compared to using a single peer.
The architecture for a MapReduce system can be thought of simply as one front-facing 
“master node”, that accepts jobs from clients, connected to many back-end “worker 
nodes”, referred to as a cluster. Jobs are divided by the master 
node and distributed among the worker nodes.

One critical piece of any MapReduce system is the scheduler used by the master node. 
When jobs come in to the master node’s queue they must be divided among worker 
nodes in some algorithmic fashion. A good scheduler will use the cluster’s resources to 
its fullest potential, resulting in overall faster job completion time. Maximizing a cluster’s 
potential includes utilizing processing power effectively, minimizing network traffic 
overhead (data locality), maximizing throughput, minimizing latency, and maintaining 
fairness.

In this project, we plan to simulate three different scheduling algorithms in MapReduce. 
We first started our investigation by referencing research articles about existing 
scheduling algorithms and their respective characteristics. We chose mean completion 
time as the basis of comparison, and then chose three candidate algorithms to compare 
in a simulated MapReduce system using that criteria. The schedulers are outlined briefly 
below.

The First In First Out (FIFO) scheduler selects the first job in the queue, pops it off, 
and splits the task evenly among all the worker nodes in the cluster. The Fairness 
scheduler, conversely, splits the resources evenly among all the tasks, i.e., every task
gets an equal share of resources. Lastly, the Capacity scheduler is an adaptation of the 
Fairness scheduler built for large clusters. The Capacity scheduler utilizes sub-queues
and priorities within the master node to make decisions as to whom to schedule tasks.


The report on this project can be read on this document. http://people.rit.edu/~jas7553/pub/DistributedMind_TeamProject.pdf

Software Archive : https://sites.google.com/site/distributedsystems4005730/
