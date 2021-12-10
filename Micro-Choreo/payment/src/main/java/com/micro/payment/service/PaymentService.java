package com.micro.payment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.dto.PaymentDto;
import com.micro.common.events.order.OrderEvent;
import com.micro.common.events.payment.PaymentEvent;
import com.micro.common.events.payment.PaymentStatus;
import com.micro.payment.entity.UserBalance;
import com.micro.payment.entity.UserTransaction;
import com.micro.payment.repository.UserBalanceRepository;
import com.micro.payment.repository.UserTransactionRepository;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository balanceRepository;

    @Autowired
    private UserTransactionRepository transactionRepository;

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent){
        var purchaseOrder = orderEvent.getPurchaseOrder();
        var dto = PaymentDto.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice());
        Optional<UserBalance> opt = this.balanceRepository.findById(purchaseOrder.getUserId());
        if(opt.isPresent())
        {
        	UserBalance ub = opt.get();
        	System.out.println(" user Balance " + ub.getBalance() + " purchace price "+ purchaseOrder.getPrice());
        }
        return this.balanceRepository.findById(purchaseOrder.getUserId())
                .filter(ub -> ub.getBalance() >= purchaseOrder.getPrice())
                .map(ub -> {
                    ub.setBalance(ub.getBalance() - purchaseOrder.getPrice());
                    this.transactionRepository.save(UserTransaction.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice()));
                    return new PaymentEvent(dto, PaymentStatus.RESERVED);
                })
                .orElse(new PaymentEvent(dto, PaymentStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent){
        this.transactionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(ut -> {
                    this.transactionRepository.delete(ut);
                    this.balanceRepository.findById(ut.getUserId())
                            .ifPresent(ub -> ub.setBalance(ub.getBalance() + ut.getAmount()));
                });
    }
}
