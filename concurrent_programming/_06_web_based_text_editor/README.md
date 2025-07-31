## Scenario
Users write documents in a web editor that auto-saves every 10 seconds. 
If the user presses "Save" at the same time, it should not conflict with auto-save.

## Proposed Solution
Use a hash + queue–based design with sequence numbers to:
- Avoid redundant writes (skip identical content).
- Serialize actual saves to prevent write conflicts.
- Ensure newer content is never overwritten by older requests.
## Approach
### 👉 Assign Sequence Numbers
- Each incoming request is assigned a **monotonic sequence number** (or timestamp) at arrival.
- Used to detect if an older request should be skipped before saving.
### 👉 Hash Computation Stage (Parallel)
- Requests are submitted to a **thread pool** to compute a **content hash** in parallel.
### 👉 Duplicate Filtering
- A **ConcurrentHashMap** stores the **latest saved hash** for each document.
- If the incoming request’s hash matches the **latest saved hash**, the request is **dropped**.
### 👉 Queued Sequential Save
- Unique requests are placed into a **LinkedBlockingQueue**.
- A single **consumer thread** dequeues requests FIFO, performing saves **sequentially**.
- Before saving, the consumer **compares the request’s sequence number** with the **latest saved sequence**.
- If the request is **older than the last saved sequence**, it is **skipped** to avoid overwriting newer content.