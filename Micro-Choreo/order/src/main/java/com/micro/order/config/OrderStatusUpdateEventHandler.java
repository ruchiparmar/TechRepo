package com.micro.order.config;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.common.events.inventory.InventoryStatus;
import com.micro.common.events.order.OrderStatus;
import com.micro.common.events.payment.PaymentStatus;
import com.micro.order.entity.PurchaseOrder;
import com.micro.order.repository.PurchaseOrderRepository;
import com.micro.order.service.OrderStatusPublisher;

@Service
public class OrderStatusUpdateEventHandler {

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private OrderStatusPublisher publisher;

    @Transactional
    public void updateOrder(final UUID id, Consumer<PurchaseOrder> consumer){
    	System.out.println("OrderStatusUpdateEventHandler.updateOrder "+ id);
        this.repository
                .findById(id)
                .ifPresent(consumer.andThen(this::updateOrder));

    }

    private void updateOrder(PurchaseOrder purchaseOrder){
    	System.out.println("OrderStatusUpdateEventHandler.updateOrder inventoryStatus =  "+purchaseOrder.getInventoryStatus() + 
    								" paymentStatus = " + purchaseOrder.getPaymentStatus());
        if(Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus()))
        {
        	System.out.println("OrderStatusUpdateEventHandlerupdateOrder .Either inventory status or purchase status is null");
            return;
        }
        var isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) && InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());
        var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if (!isComplete){
        	System.out.println("OrderStatusUpdateEventHandler.updateOrder raise OrderEvent "+ orderStatus);
            this.publisher.raiseOrderEvent(purchaseOrder, orderStatus);
        }
    }

}
