package org.backend_iv.concurrentprogramming._06_web_based_text_editor.save;

public record SaveRequest(String docId, String content) {
}