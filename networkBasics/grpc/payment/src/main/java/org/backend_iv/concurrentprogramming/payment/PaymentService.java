package org.backend_iv.concurrentprogramming.payment;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.springframework.grpc.server.service.GrpcService;
import payment.Payment.PaymentRequest;
import payment.Payment.PaymentResponse;
import payment.PaymentServiceGrpc;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GrpcService
public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {
    Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Override
    public void processPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        try {
            logger.info("\uD83D\uDD04 Processing payment for order " + request.getOrderId());
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(e);
            return;
        }
        PaymentResponse response = PaymentResponse.newBuilder()
                .setSuccess(true)
                .setTransactionId(UUID.randomUUID().toString())
                .setMessage("Payment successful for order " + request.getOrderId())
                .build();
        logger.info("✅ Payment processed for order {}, Transaction ID: {}", request.getOrderId(), response.getTransactionId() );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
