import java.util.*;

class Process {
    int processID;
    int burstTime;
    int arrivalTime;
    int priority;
    int waitingTime;
    int turnAroundTime;
    int remainingTime;  // Used for Round Robin scheduling

    public Process(int processID, int burstTime, int arrivalTime, int priority) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}

public class OptimizedCPUScheduling {

    // Priority Scheduling using PriorityQueue
    public static void priorityScheduling(Process[] processes) {
        System.out.println("\n--- Priority Scheduling using PriorityQueue ---");

        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority)); // Min-Heap based on priority

        // Add all processes to the priority queue
        pq.addAll(Arrays.asList(processes));

        int currentTime = 0;

        while (!pq.isEmpty()) {
            Process process = pq.poll();
            System.out.println("Process " + process.processID + " is running for " + process.burstTime + " units.");
            
            // Calculate waiting time and turnaround time
            process.waitingTime = currentTime - process.arrivalTime;
            process.turnAroundTime = process.waitingTime + process.burstTime;
            
            currentTime += process.burstTime;
            
            // Simulate delay for visualization
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        printResults(processes);
    }

    // Round Robin Scheduling using LinkedList (Circular Queue)
    public static void roundRobin(Process[] processes, int timeQuantum) {
        System.out.println("\n--- Round Robin Scheduling using LinkedList (Circular Queue) ---");

        LinkedList<Process> queue = new LinkedList<>(Arrays.asList(processes));
        int currentTime = 0;

        System.out.println("\nTime | Process Execution");

        while (!queue.isEmpty()) {
            Process process = queue.poll();
            
            if (process.remainingTime > timeQuantum) {
                System.out.println(currentTime + " | Process " + process.processID + " is running for " + timeQuantum + " units.");
                currentTime += timeQuantum;
                process.remainingTime -= timeQuantum;
                queue.addLast(process); // Re-add to the back of the queue
            } else {
                System.out.println(currentTime + " | Process " + process.processID + " is running for " + process.remainingTime + " units.");
                currentTime += process.remainingTime;
                process.remainingTime = 0;
                process.waitingTime = currentTime - process.burstTime;
            }

            // Simulate delay for visualization
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        // Calculate Turnaround Time for each process
        for (Process process : processes) {
            process.turnAroundTime = process.waitingTime + process.burstTime;
        }

        printResults(processes);
    }

    // Shortest Job First (SJF) Scheduling using PriorityQueue (Min-Heap)
    public static void sjfScheduling(Process[] processes) {
        System.out.println("\n--- Shortest Job First Scheduling using PriorityQueue ---");

        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime)); // Min-Heap based on burst time
        pq.addAll(Arrays.asList(processes)); // Add all processes to the priority queue

        int currentTime = 0;

        while (!pq.isEmpty()) {
            Process process = pq.poll();
            System.out.println("Process " + process.processID + " is running for " + process.burstTime + " units.");

            // Calculate waiting time and turnaround time
            process.waitingTime = currentTime - process.arrivalTime;
            process.turnAroundTime = process.waitingTime + process.burstTime;

            currentTime += process.burstTime;

            // Simulate delay for visualization
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        printResults(processes);
    }

    // Print scheduling results
    public static void printResults(Process[] processes) {
        System.out.println("\nFinal Process Statistics:");
        System.out.println("ProcessID\tBurstTime\tWaitingTime \tTurnAroundTime\tPriority");
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;
            System.out.println(p.processID + "\t\t" + p.burstTime + "\t\t" + p.waitingTime + "\t\t" + p.turnAroundTime + "\t\t" + p.priority);
        }

        System.out.println("\nAverage Waiting Time: " + (double) totalWaitingTime / processes.length);
        System.out.println("Average Turnaround Time: " + (double) totalTurnAroundTime / processes.length);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter priority for process " + (i + 1) + ": ");
            int priority = sc.nextInt();
            processes[i] = new Process(i + 1, burstTime, arrivalTime, priority);
        }

        System.out.println("\nChoose Scheduling Algorithm: ");
        System.out.println("1. Shortest Job First");
        System.out.println("2. Priority Scheduling");
        System.out.println("3. Round Robin");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                sjfScheduling(processes.clone());
                break;
            case 2:
                priorityScheduling(processes.clone());
                break;
            case 3:
                System.out.print("Enter the time quantum for Round Robin: ");
                int timeQuantum = sc.nextInt();
                roundRobin(processes.clone(), timeQuantum);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
