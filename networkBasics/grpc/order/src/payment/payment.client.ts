import { Injectable, OnModuleInit } from '@nestjs/common';
import { Client, ClientGrpc, Transport } from '@nestjs/microservices';
import { join } from 'path';
import { Observable } from 'rxjs';

interface PaymentServiceGrpc {
  processPayment(data: {
    orderId: string;
    amount: number;
  }): Observable<PaymentResponse>;
}

export interface PaymentResponse {
  success: boolean;
  transactionId: string;
  message: string;
}

@Injectable()
export class PaymentClientService implements OnModuleInit {
  private paymentService: PaymentServiceGrpc;

  @Client({
    transport: Transport.GRPC,
    options: {
      package: 'payment',
      protoPath: join(__dirname, '../proto/payment.proto'),
      url: 'localhost:9090',
    },
  })
  private client: ClientGrpc;

  onModuleInit() {
    this.paymentService =
      this.client.getService<PaymentServiceGrpc>('PaymentService');
  }

  processPayment(orderId: string, amount: number): Observable<PaymentResponse> {
    return this.paymentService.processPayment({ orderId, amount });
  }
}
