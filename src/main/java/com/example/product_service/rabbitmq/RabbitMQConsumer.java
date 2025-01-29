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
        try {
            // Log the incoming message to check its format
            logger.info("Received message: {}", message);

            // Check if the message is in the expected format "productId,quantity"
            if (message != null && !message.isEmpty()) {
                String[] data = message.split(",");

                // Ensure the message has exactly two parts (productId and quantity)
                if (data.length == 2) {
                    Long productId = parseLongSafely(data[0].trim());
                    Integer quantity = parseIntegerSafely(data[1].trim());

                    // Proceed with stock rollback logic
                    if (productId != null && quantity != null) {
                        logger.info("Reverting stock for product ID: {}, quantity: {}", productId, quantity);
                        productService.restockProduct(productId, quantity);
                        logger.info("Stock successfully reverted for product ID: {} with quantity: {}", productId, quantity);
                    } else {
                        logger.error("Invalid data in the message. Product ID: {}, Quantity: {}", data[0], data[1]);
                    }
                } else {
                    logger.error("Invalid message format. Expected format: 'productId,quantity', received: {}", message);
                }
            } else {
                logger.error("Received empty or null message.");
            }
        } catch (Exception e) {
            logger.error("Error processing the rollback stock message: {}", message, e);
        }
    }

    // Safe parsing for Long
    private Long parseLongSafely(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid number format for product ID: {}", value);
            return null;
        }
    }

    // Safe parsing for Integer
    private Integer parseIntegerSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid number format for quantity: {}", value);
            return null;
        }
    }
}
