import { Module } from '@nestjs/common';
import { OrderController } from './order/order.controller';
import { PaymentClientService } from './payment/payment.client';

@Module({
  imports: [],
  controllers: [OrderController],
  providers: [PaymentClientService],
})
export class AppModule {}
