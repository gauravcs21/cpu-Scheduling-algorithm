import java.util.Scanner;

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

public class SimpleCPUScheduling {

    // Simple Shortest Job First (SJF) Scheduling
    public static void sjfScheduling(Process[] processes) {
        System.out.println("\n--- Shortest Job First Scheduling ---");

        // Sort processes by burst time
        for (int i = 0; i < processes.length - 1; i++) {
            for (int j = i + 1; j < processes.length; j++) {
                if (processes[i].burstTime > processes[j].burstTime) {
                    Process temp = processes[i];
                    processes[i] = processes[j];
                    processes[j] = temp;
                }
            }
        }

        int currentTime = 0;

        for (Process process : processes) {
            System.out.println("Process " + process.processID + " is running for " + process.burstTime + " units.");

            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.turnAroundTime = process.waitingTime + process.burstTime;

            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        printResults(processes);
    }

    // Simple Priority Scheduling
    public static void priorityScheduling(Process[] processes) {
        System.out.println("\n--- Priority Scheduling ---");

        // Sort processes by priority (lower number = higher priority)
        for (int i = 0; i < processes.length - 1; i++) {
            for (int j = i + 1; j < processes.length; j++) {
                if (processes[i].priority > processes[j].priority) {
                    Process temp = processes[i];
                    processes[i] = processes[j];
                    processes[j] = temp;
                }
            }
        }

        int currentTime = 0;

        for (Process process : processes) {
            System.out.println("Process " + process.processID + " is running for " + process.burstTime + " units.");

            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.turnAroundTime = process.waitingTime + process.burstTime;

            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        printResults(processes);
    }

    // Simple Round Robin Scheduling
    public static void roundRobin(Process[] processes, int timeQuantum) {
        System.out.println("\n--- Round Robin Scheduling ---");

        int currentTime = 0;
        boolean processesRemaining;

        do {
            processesRemaining = false;

            for (Process process : processes) {
                if (process.remainingTime > 0) {
                    processesRemaining = true;
                    if (process.remainingTime > timeQuantum) {
                        System.out.println("Process " + process.processID + " is running for " + timeQuantum + " units.");
                        process.remainingTime -= timeQuantum;
                        currentTime += timeQuantum;
                    } else {
                        System.out.println("Process " + process.processID + " is running for " + process.remainingTime + " units.");
                        currentTime += process.remainingTime;
                        process.remainingTime = 0;
                    }

                    process.waitingTime = currentTime - process.burstTime;

                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        } while (processesRemaining);

        // Calculate Turnaround Time for each process
        for (Process process : processes) {
            process.turnAroundTime = process.waitingTime + process.burstTime;
        }

        printResults(processes);
    }

    // Function to print the results after scheduling
    public static void printResults(Process[] processes) {
        System.out.println("\nProcessID\tBurstTime\tWaitingTime\tTurnAroundTime\tPriority");
        for (Process p : processes) {
            System.out.println(p.processID + "\t\t" + p.burstTime + "\t\t" + p.waitingTime + "\t\t" + p.turnAroundTime + "\t\t" + p.priority);
        }
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
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Priority Scheduling");
        System.out.println("3. Round Robin");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                sjfScheduling(processes);
                break;
            case 2:
                priorityScheduling(processes);
                break;
            case 3:
                System.out.print("Enter the time quantum for Round Robin: ");
                int timeQuantum = sc.nextInt();
                roundRobin(processes, timeQuantum);
                break;
            default:
                System.out.println("Invalid choice.");
        }

        sc.close();
    }
}
