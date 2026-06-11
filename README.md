# Algorithmic Programming Final Assignment

A JavaFX desktop application that loads a dataset, converts it into three **self-built**
generic data structures, and runs hand-written sorting and searching algorithms over them.
Execution speed for every operation is measured and displayed.

Built for the NHL Stenden **Algorithmic Programming** module.

## Authors

- Kaiser Aftab
- Abu Hasib Shanewaz

---

## Features

- **Three custom, generic data structures**, each built from scratch and implementing its own interface:
  - Singly linked list (`CustomLinkedList`)
  - Hash map with separate chaining and automatic resizing (`CustomHashMap`)
  - Binary search tree with in-/pre-/post-order traversal (`CustomBinarySearchTree`)
  - (plus an array-backed list, `CustomArrayList`, used as the sort/search working buffer)
- **Four hand-written algorithms**, all generic (`<T>`):
  - Sorting: Insertion Sort, Quick Sort
  - Searching: Sequential Search, Binary Search
- **Two ways to order data**: by a **custom `Comparator`** (any field) *or* by the object's
  **natural ordering** (`Comparable`).
- **CSV import** into a typed model class (`DataRecord`), with support for loading the whole
  dataset or a subset.
- **JavaFX GUI** to import data, convert it into any of the structures, pick and run an
  algorithm, and view results.
- **Timed execution** — every operation reports its speed (rounded to the nearest tenth of a second).

---

## Requirements

- **JDK 21 or newer** (the project targets Java 21; building with an older JDK fails with
  `invalid target release: 21`).
- **Maven** (JavaFX 21.0.2 is pulled in automatically as a Maven dependency).

## Running

From the project root (the folder containing `pom.xml`):

```bash
mvn clean javafx:run
```

The main class is `nl.nhlstenden.ap.gui.MainApp`.

> Running `MainApp.main` directly from an IDE can fail with *"JavaFX runtime components are
> missing"* because JavaFX is on the classpath rather than the module path. Use the
> `javafx:run` Maven goal, which configures this correctly.

## Using the application

1. Click **Import CSV Dataset** and select a CSV file (e.g. `src/main/resources/movies_large.csv`).
   Optionally type a number into **Max records** to load only a subset.
2. Choose a **Data Structure** and click **Convert to Data Structure**.
3. Pick a **sorting** algorithm and a field (including the `natural order` option) and click **Sort**.
4. Pick a **searching** algorithm and field, enter a value, and click **Search**.
   (Binary Search sorts the data first, as it requires sorted input.)
5. The status bar and **Output Log** show the algorithm, data structure, record count, and speed.

---

## Project structure

```
src/main/java/nl/nhlstenden/ap/
├── algorithms/
│   ├── Sorter.java            interface: comparator + natural-order sort
│   ├── Searcher.java          interface: comparator + natural-order search
│   ├── InsertionSort.java
│   ├── QuickSort.java         median-of-three pivot, bounded recursion
│   ├── SequentialSearch.java
│   └── BinarySearch.java
├── datastructures/
│   ├── CustomList.java        interface (extends Iterable<T>)
│   ├── CustomLinkedList.java  singly linked list with tail pointer
│   ├── CustomArrayList.java   array-backed list
│   ├── CustomMap.java         interface
│   ├── CustomHashMap.java     separate chaining + resize at load factor 0.75
│   ├── CustomTree.java        interface
│   └── CustomBinarySearchTree.java
├── model/
│   └── DataRecord.java        Comparable + per-field comparators
├── util/
│   ├── AlgorithmTimer.java    nanosecond timer, tenth-of-a-second formatting
│   └── DatasetLoader.java     CSV reader (handles quoted fields)
└── gui/
    └── MainApp.java           JavaFX user interface

src/main/resources/
└── movies_large.csv           large dataset (50,000 records)
```

---

## Data structures

| Structure | Key operations | Average | Worst |
|---|---|---|---|
| `CustomLinkedList` | `add` (append) | O(1) | O(1) |
| | `get` / `set` / `remove(i)` / `indexOf` | O(n) | O(n) |
| `CustomArrayList` | `get` / `set` | O(1) | O(1) |
| | `add` (append) | O(1) amortised | O(n) on resize |
| `CustomHashMap` | `put` / `get` / `remove` | O(1) | O(n) |
| `CustomBinarySearchTree` | `insert` / `search` / `remove` | O(log n) | O(n) |
| | traversals | O(n) | O(n) |

`CustomList` extends `Iterable<T>`, so all lists support enhanced-for iteration; this keeps
import, conversion, and table refresh linear instead of quadratic.

## Algorithms

| Algorithm | Best | Average | Worst | Notes |
|---|---|---|---|---|
| Insertion Sort | O(n) | O(n²) | O(n²) | In place |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | Median-of-three pivot; recursion bounded to O(log n) |
| Sequential Search | O(1) | O(n) | O(n) | Works on unsorted data |
| Binary Search | O(1) | O(log n) | O(log n) | Requires sorted input |

All algorithms work with **generic types** and accept either a custom `Comparator` or the
element's natural ordering (`Comparable`).

### Quick Sort hardening

A naive Quick Sort with a last-element pivot degrades to O(n²) **and** O(n) recursion depth on
already-sorted input — which the GUI produces (e.g. a binary search after converting to a BST or
HashMap). This implementation uses a **median-of-three pivot** to avoid the sorted-input worst
case and **recurses into the smaller partition while iterating on the larger one**, bounding the
recursion depth to O(log n) so it never overflows the stack. Sorting a pre-sorted 50,000-record
dataset completes in milliseconds.

---

## Dataset

CSV format (header required):

```
id,title,year,rating,genre
1,The Shawshank Redemption,1994,9.3,Drama
```

| Field | Type |
|---|---|
| `id` | int (unique) |
| `title` | string |
| `year` | int |
| `rating` | double |
| `genre` | string |

`movies_large.csv` (50,000 records) is included so the timing differences between algorithms are
visible — for example, sorting by a field with Insertion Sort versus Quick Sort.

---

## Assessment rubric coverage

| Component | How it is met |
|---|---|
| **Data structures** | Three generic, self-built structures with full manipulation methods; `DataRecord` provides per-field comparators and natural ordering. |
| **Algorithms** | Four functional, generic algorithms; sorting supports both the object's natural ordering and custom comparators. |
| **Graphical user interface** | Import, convert to any structure, select and run algorithms, and display speed, dataset, structure, and algorithm. |
| **Use of dataset** | CSV converted into objects of a predefined class (`DataRecord`) and applied to all structures; full dataset or a subset. |
