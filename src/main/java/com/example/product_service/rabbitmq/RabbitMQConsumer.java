package com.example.product_service.rabbitmq;

import com.example.product_service.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "rollbackStockQueue")
    public void rollbackStock(String message) {
        String[] data = message.split(",");
        Long productId = Long.parseLong(data[0]);
        Integer quantity = Integer.parseInt(data[1]);

        logger.info("Mensaje recibido para revertir stock, producto ID: {}, cantidad: {}", productId, quantity);

        try {
            productService.restockProduct(productId, quantity);
            logger.info("Stock restaurado para producto ID: {} con cantidad: {}", productId, quantity);
        } catch (Exception e) {
            logger.error("Error al restaurar stock para el producto ID: {}: {}", productId, e.getMessage());
        }
    }
}
