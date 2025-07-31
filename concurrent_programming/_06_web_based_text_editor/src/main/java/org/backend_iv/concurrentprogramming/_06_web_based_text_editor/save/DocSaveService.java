package org.backend_iv.concurrentprogramming._06_web_based_text_editor.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DocSaveService {
    private final BlockingQueue<Document> saveQueue = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> sequenceMap = new ConcurrentHashMap<>();

    private final Map<String, String> contentMap = new HashMap<>();

    private final ExecutorService hashPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    private final AtomicLong seqGenerator = new AtomicLong();
    private final Logger logger = LoggerFactory.getLogger(DocSaveService.class);

    public DocSaveService() {
        Thread consumer = new Thread(this::processQueue);
        consumer.setDaemon(true);
        consumer.start();
        logger.info("✅ Document Save Service started.");
    }

    private void processQueue() {
        try {
            Document doc = saveQueue.take();
            contentMap.put(doc.docId(), doc.content());
            logger.info("✅ New version of Document {} saved successfully.", doc.docId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    public String getLatestVersion(String docId) {
        return contentMap.get(docId);
    }

    public void submitSaveRequest(SaveRequest request) {
        long seq = seqGenerator.getAndIncrement();
        Document document = new Document(seq, request.docId(), request.content());
        hashPool.submit(() -> processHashing(document));
        logger.info("✅ Document {} processing sunmitted successfully.", request.docId());
    }

    private void processHashing(Document document) {
        long reqSeqNumber = document.sequenceNumber();
        String reqDocId = document.docId();
        String reqContent = document.content();

        long latestSeqNumber = sequenceMap.getOrDefault(reqDocId, -1L);
        if (latestSeqNumber != -1L && latestSeqNumber >= reqSeqNumber) {
            logger.info("⛔ Document save attempt failed.Outdated version of Document {}.", reqDocId);
            return;
        }

        String newHash = hash(reqContent);
        String latestSavedHash = hashMap.getOrDefault(reqDocId, null);

        if (newHash.equals(latestSavedHash)) {
            logger.info("⛔ Document {} save attempt failed. No changes detected.", reqDocId);
            return;
        }

        hashMap.put(reqDocId, newHash);
        sequenceMap.put(reqDocId, reqSeqNumber);
        saveQueue.offer(document);
    }

    private String hash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error Computin Hash", e);
        }
    }
}
