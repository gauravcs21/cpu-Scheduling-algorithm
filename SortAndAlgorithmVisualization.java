import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortAndAlgorithmVisualization extends JPanel {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private final int BAR_WIDTH = 4;
    private final int NUM_BARS = WINDOW_WIDTH / BAR_WIDTH;
    private final int[] numbers;
    private String elapsedTime = ""; // To display elapsed time for all algorithms

    public SortAndAlgorithmVisualization() {
        numbers = new int[NUM_BARS];
        initializeArray();
    }

    private void initializeArray() {
        Random random = new Random();
        for (int i = 0; i < NUM_BARS; i++) {
            numbers[i] = random.nextInt(WINDOW_HEIGHT - 50) + 10;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw bars
        for (int i = 0; i < NUM_BARS; i++) {
            g.setColor(Color.WHITE);
            g.fillRect(i * BAR_WIDTH, WINDOW_HEIGHT - numbers[i], BAR_WIDTH, numbers[i]);
            g.setColor(Color.BLACK);
            g.drawRect(i * BAR_WIDTH, WINDOW_HEIGHT - numbers[i], BAR_WIDTH, numbers[i]);
        }

        // Display elapsed time
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time: " + elapsedTime, 20, 30);
    }

    public void bubbleSort() {
        new Thread(() -> {
            long startTime = System.nanoTime(); // Start time
            for (int i = 0; i < numbers.length - 1; i++) {
                for (int j = 0; j < numbers.length - i - 1; j++) {
                    if (numbers[j] > numbers[j + 1]) {
                        swap(j, j + 1);
                    }
                }
            }
            long endTime = System.nanoTime(); // End time
            elapsedTime = formatTime(endTime - startTime);
            repaint();
        }).start();
    }

    public void insertionSort() {
        new Thread(() -> {
            long startTime = System.nanoTime();
            for (int i = 1; i < numbers.length; i++) {
                int key = numbers[i];
                int j = i - 1;
                while (j >= 0 && numbers[j] > key) {
                    numbers[j + 1] = numbers[j];
                    j--;
                    repaintAndSleep();
                }
                numbers[j + 1] = key;
                repaintAndSleep();
            }
            long endTime = System.nanoTime();
            elapsedTime = formatTime(endTime - startTime);
            repaint();
        }).start();
    }

    public void selectionSort() {
        new Thread(() -> {
            long startTime = System.nanoTime();
            for (int i = 0; i < numbers.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < numbers.length; j++) {
                    if (numbers[j] < numbers[minIndex]) {
                        minIndex = j;
                    }
                }
                swap(i, minIndex);
            }
            long endTime = System.nanoTime();
            elapsedTime = formatTime(endTime - startTime);
            repaint();
        }).start();
    }

    public void mergeSort() {
        new Thread(() -> {
            long startTime = System.nanoTime();
            mergeSortHelper(0, numbers.length - 1);
            long endTime = System.nanoTime();
            elapsedTime = formatTime(endTime - startTime);
            repaint();
        }).start();
    }

    private void mergeSortHelper(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (numbers[i] <= numbers[j]) {
                temp[k++] = numbers[i++];
            } else {
                temp[k++] = numbers[j++];
            }
            repaintAndSleep();
        }

        while (i <= mid) {
            temp[k++] = numbers[i++];
            repaintAndSleep();
        }

        while (j <= right) {
            temp[k++] = numbers[j++];
            repaintAndSleep();
        }

        for (i = left; i <= right; i++) {
            numbers[i] = temp[i - left];
            repaintAndSleep();
        }
    }

    public void quickSort() {
        new Thread(() -> {
            long startTime = System.nanoTime();
            quickSortHelper(0, numbers.length - 1);
            long endTime = System.nanoTime();
            elapsedTime = formatTime(endTime - startTime);
            repaint();
        }).start();
    }

    private void quickSortHelper(int low, int high) {
        if (low < high) {
            int pivot = partition(low, high);
            quickSortHelper(low, pivot - 1);
            quickSortHelper(pivot + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = numbers[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (numbers[j] < pivot) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
        repaintAndSleep();
    }

    private void repaintAndSleep() {
        repaint();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String formatTime(long nanoseconds) {
        return (nanoseconds / 1_000_000.0) + " ms";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Algorithm Visualization");
        SortAndAlgorithmVisualization panel = new SortAndAlgorithmVisualization();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panel.WINDOW_WIDTH, panel.WINDOW_HEIGHT);
        frame.add(panel);
        frame.setVisible(true);

        String[] options = { "Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort" };
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Select an algorithm:",
                "Algorithm Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        SwingUtilities.invokeLater(() -> {
            switch (choice) {
                case "Bubble Sort" -> panel.bubbleSort();
                case "Insertion Sort" -> panel.insertionSort();
                case "Selection Sort" -> panel.selectionSort();
                case "Merge Sort" -> panel.mergeSort();
                case "Quick Sort" -> panel.quickSort();
                default -> JOptionPane.showMessageDialog(null, "Invalid Selection!");
            }
        });
    }
}
