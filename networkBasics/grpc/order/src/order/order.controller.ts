import { Body, Controller, Post } from '@nestjs/common';
import { firstValueFrom } from 'rxjs';
import { PaymentClientService } from 'src/payment/payment.client';

@Controller('order')
export class OrderController {
  constructor(private readonly paymentClient: PaymentClientService) {}

  @Post()
  async placeOrder(@Body() body: { orderId: string; amount: number }) {
    const result = await firstValueFrom(
      this.paymentClient.processPayment(body.orderId, body.amount),
    );
    return result;
  }
}
