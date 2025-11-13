import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class SortingVisualizer extends JFrame {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private final VisualPanel visualPanel;
    private final JComboBox<String> algorithmBox;
    private final JButton generateBtn, startPauseBtn, resetBtn;
    private final JSlider sizeSlider, speedSlider;

    private int[] array;
    private int[] original;
    private SortingWorker worker;

    public SortingVisualizer() {
        super("Java Sorting Visualizer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        visualPanel = new VisualPanel();
        add(visualPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();

        String[] algorithms = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Radix Sort", "Quick Sort", "Merge Sort", "Bucket Sort"};
        algorithmBox = new JComboBox<>(algorithms);
        controls.add(new JLabel("Algorithm:"));
        controls.add(algorithmBox);

        generateBtn = new JButton("Generate");
        startPauseBtn = new JButton("Start");
        resetBtn = new JButton("Reset");

        controls.add(generateBtn);
        controls.add(startPauseBtn);
        controls.add(resetBtn);

        sizeSlider = new JSlider(10, 300, 80);
        sizeSlider.setPreferredSize(new Dimension(200, 40));
        controls.add(new JLabel("Size:"));
        controls.add(sizeSlider);

        speedSlider = new JSlider(0, 200, 20); // delay in ms
        speedSlider.setPreferredSize(new Dimension(200, 40));
        controls.add(new JLabel("Speed (ms):"));
        controls.add(speedSlider);

        add(controls, BorderLayout.SOUTH);

        // Listeners
        generateBtn.addActionListener(e -> generateArray());
        startPauseBtn.addActionListener(e -> startOrPause());
        resetBtn.addActionListener(e -> resetArray());

        sizeSlider.addChangeListener(e -> {
            if (!sizeSlider.getValueIsAdjusting()) generateArray();
        });

        // Initial generation
        generateArray();
    }

    private void generateArray() {
        int n = sizeSlider.getValue();
        array = new int[n];
        Random r = new Random();
        // fill with values 0..max
        for (int i = 0; i < n; i++) {
            array[i] = r.nextInt(500); // range for visualization
        }
        original = Arrays.copyOf(array, array.length);
        if (worker != null) worker.cancel(true);
        startPauseBtn.setText("Start");
        visualPanel.setArray(array);
    }

    private void resetArray() {
        if (worker != null) worker.cancel(true);
        array = Arrays.copyOf(original, original.length);
        visualPanel.resetHighlights();
        visualPanel.setArray(array);
        startPauseBtn.setText("Start");
    }

    private void startOrPause() {
        if (worker != null && !worker.isDone()) {
            // pause/cancel
            worker.cancel(true);
            startPauseBtn.setText("Start");
        } else {
            // start
            worker = new SortingWorker(algorithmBox.getSelectedItem().toString(), speedSlider.getValue());
            worker.execute();
            startPauseBtn.setText("Pause");
        }
    }

    private class VisualPanel extends JPanel {
        private int[] arr;
        private int highlightedA = -1, highlightedB = -1;
        private List<Integer> pivotIndices = new ArrayList<>();

        VisualPanel() {
            setBackground(Color.BLACK);
        }

        void setArray(int[] a) {
            this.arr = a;
            repaint();
        }

        void setHighlight(int a, int b) {
            highlightedA = a;
            highlightedB = b;
            repaint();
        }

        void setPivotIndices(List<Integer> pivots) {
            pivotIndices = pivots == null ? new ArrayList<>() : pivots;
            repaint();
        }

        void resetHighlights() {
            highlightedA = highlightedB = -1;
            pivotIndices.clear();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arr == null) return;

            int w = getWidth();
            int h = getHeight();
            int n = arr.length;
            double barW = (double) w / n;
            int max = Arrays.stream(arr).max().orElse(1);

            for (int i = 0; i < n; i++) {
                int val = arr[i];
                int barH = (int) ((double) val / max * (h * 0.9));
                int x = (int) (i * barW);
                int y = h - barH;

                if (i == highlightedA || i == highlightedB) {
                    g.setColor(Color.RED);
                } else if (pivotIndices.contains(i)) {
                    g.setColor(Color.MAGENTA);
                } else {
                    // color gradient
                    float hue = (float) i / Math.max(1, n);
                    g.setColor(Color.getHSBColor(hue, 0.9f, 0.9f));
                }

                g.fillRect(x, y, (int) Math.max(1, Math.ceil(barW)), barH);
            }
        }
    }

    private class SortingWorker extends SwingWorker<Void, int[]> {
        private final String algorithm;
        private final int delay;

        SortingWorker(String algorithm, int delay) {
            this.algorithm = algorithm;
            this.delay = Math.max(0, delay);
        }

        @Override
        protected Void doInBackground() throws Exception {
            switch (algorithm) {
                case "Bubble Sort":
                    bubbleSort();
                    break;
                case "Insertion Sort":
                    insertionSort();
                    break;
                case "Selection Sort":
                    selectionSort();
                    break;
                case "Radix Sort":
                    radixSort();
                    break;
                case "Quick Sort":
                    quickSort(0, array.length - 1);
                    break;
                case "Merge Sort":
                    mergeSort(0, array.length - 1);
                    break;
                case "Bucket Sort":
                    bucketSort();
                    break;
            }
            visualPanel.resetHighlights();
            publish(Arrays.copyOf(array, array.length));
            return null;
        }

        @Override
        protected void process(List<int[]> chunks) {
            int[] latest = chunks.get(chunks.size() - 1);
            visualPanel.setArray(Arrays.copyOf(latest, latest.length));
        }

        @Override
        protected void done() {
            startPauseBtn.setText("Start");
        }

        private void sleepAndPublish(int a, int b) throws InterruptedException {
            visualPanel.setHighlight(a, b);
            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
            if (isCancelled()) throw new InterruptedException();
        }

        // --- Sorting algorithms with step/publish calls ---

        private void bubbleSort() throws InterruptedException {
            int n = array.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    if (isCancelled()) return;
                    sleepAndPublish(j, j + 1);
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                        publish(Arrays.copyOf(array, array.length));
                        Thread.sleep(delay);
                    }
                }
            }
        }

        private void insertionSort() throws InterruptedException {
            int n = array.length;
            for (int i = 1; i < n; ++i) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    if (isCancelled()) return;
                    array[j + 1] = array[j];
                    sleepAndPublish(j, j + 1);
                    j = j - 1;
                }
                array[j + 1] = key;
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
            }
        }

        private void selectionSort() throws InterruptedException {
            int n = array.length;
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++) {
                    if (isCancelled()) return;
                    sleepAndPublish(min_idx, j);
                    if (array[j] < array[min_idx]) {
                        min_idx = j;
                        publish(Arrays.copyOf(array, array.length));
                        Thread.sleep(delay);
                    }
                }
                if (min_idx != i) swap(min_idx, i);
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
            }
        }

        private void radixSort() throws InterruptedException {
            // Support negatives by offsetting
            int min = Arrays.stream(array).min().orElse(0);
            int offset = 0;
            if (min < 0) offset = -min;
            int[] arrCopy = Arrays.copyOf(array, array.length);
            if (offset != 0) for (int i = 0; i < arrCopy.length; i++) arrCopy[i] += offset;

            int max = Arrays.stream(arrCopy).max().orElse(0);
            for (int exp = 1; max / exp > 0; exp *= 10) {
                countingSortByExp(arrCopy, exp);
                // map back to array for visualization (reversing offset)
                for (int i = 0; i < arrCopy.length; i++) array[i] = arrCopy[i] - offset;
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
                if (isCancelled()) return;
            }
        }

        private void countingSortByExp(int[] arr, int exp) throws InterruptedException {
            int n = arr.length;
            int[] output = new int[n];
            int[] count = new int[10];
            for (int i = 0; i < n; i++) count[(arr[i] / exp) % 10]++;
            for (int i = 1; i < 10; i++) count[i] += count[i - 1];
            for (int i = n - 1; i >= 0; i--) {
                output[count[(arr[i] / exp) % 10] - 1] = arr[i];
                count[(arr[i] / exp) % 10]--;
            }
            for (int i = 0; i < n; i++) {
                arr[i] = output[i];
                sleepAndPublish(i, i);
            }
        }

        private void quickSort(int low, int high) throws InterruptedException {
            if (low < high) {
                int pi = partition(low, high);
                quickSort(low, pi - 1);
                quickSort(pi + 1, high);
            }
        }

        private int partition(int low, int high) throws InterruptedException {
            int pivot = array[high];
            List<Integer> pivots = Arrays.asList(high);
            visualPanel.setPivotIndices(pivots);
            int i = low - 1;
            for (int j = low; j <= high - 1; j++) {
                if (isCancelled()) return i;
                sleepAndPublish(j, high);
                if (array[j] < pivot) {
                    i++;
                    swap(i, j);
                    publish(Arrays.copyOf(array, array.length));
                    Thread.sleep(delay);
                }
            }
            swap(i + 1, high);
            visualPanel.setPivotIndices(Collections.emptyList());
            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
            return i + 1;
        }

        private void mergeSort(int l, int r) throws InterruptedException {
            if (l < r) {
                int m = (l + r) / 2;
                mergeSort(l, m);
                mergeSort(m + 1, r);
                merge(l, m, r);
            }
        }

        private void merge(int l, int m, int r) throws InterruptedException {
            int n1 = m - l + 1;
            int n2 = r - m;
            int[] L = new int[n1];
            int[] R = new int[n2];
            System.arraycopy(array, l, L, 0, n1);
            System.arraycopy(array, m + 1, R, 0, n2);
            int i = 0, j = 0, k = l;
            while (i < n1 && j < n2) {
                if (isCancelled()) return;
                sleepAndPublish(k, k);
                if (L[i] <= R[j]) {
                    array[k] = L[i++];
                } else {
                    array[k] = R[j++];
                }
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
                k++;
            }
            while (i < n1) {
                if (isCancelled()) return;
                array[k++] = L[i++];
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
            }
            while (j < n2) {
                if (isCancelled()) return;
                array[k++] = R[j++];
                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
            }
        }

        private void bucketSort() throws InterruptedException {
            int n = array.length;
            if (n <= 0) return;
            int min = Arrays.stream(array).min().orElse(0);
            int max = Arrays.stream(array).max().orElse(0);
            int range = max - min + 1;
            // number of buckets heuristic
            int bucketCount = Math.max(1, (int) Math.sqrt(n));
            List<List<Integer>> buckets = new ArrayList<>(bucketCount);
            for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
            for (int v : array) {
                int idx = (int) ((long) (v - min) * (bucketCount - 1) / Math.max(1, range - 1));
                buckets.get(idx).add(v);
            }
            int idx = 0;
            for (int b = 0; b < bucketCount; b++) {
                List<Integer> bucket = buckets.get(b);
                if (bucket.isEmpty()) continue;
                Collections.sort(bucket);
                for (int val : bucket) {
                    if (isCancelled()) return;
                    array[idx++] = val;
                    sleepAndPublish(idx - 1, idx - 1);
                    publish(Arrays.copyOf(array, array.length));
                    Thread.sleep(delay);
                }
            }
        }

        // Utility
        private void swap(int i, int j) {
            int t = array[i];
            array[i] = array[j];
            array[j] = t;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer app = new SortingVisualizer();
            app.setVisible(true);
        });
    }
}