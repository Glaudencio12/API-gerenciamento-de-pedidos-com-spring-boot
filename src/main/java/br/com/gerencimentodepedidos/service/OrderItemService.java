package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService serviceOrder;
    private final HateoasLinks hateoas;
    private final ModelMapper modelMapper;
    private final PagedResourcesAssembler<OrderItemResponseDTO> assembler;

    Logger logger = LoggerFactory.getLogger(OrderItemService.class.getName());

    public OrderItemService(
            OrderItemRepository repository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderService serviceOrder,
            HateoasLinks hateoas,
            ModelMapper modelMapper,
            PagedResourcesAssembler<OrderItemResponseDTO> assembler
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.serviceOrder = serviceOrder;
        this.hateoas = hateoas;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }

    public OrderItemResponseDTO createOrderItem(OrderItemRequestDTO itemDTO) {
        logger.info("Creating an order item!");
        Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        Order order = orderRepository.findById(itemDTO.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setOrder(order);
        item.setQuantity(itemDTO.getQuantity());
        item = repository.save(item);

        ProductResponseDTO productDTO = new ProductResponseDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(product.getCategory());
        hateoas.links(productDTO);

        OrderItemResponseDTO responseDTO = new OrderItemResponseDTO();
        responseDTO.setId(item.getId());
        responseDTO.setProduct(productDTO);
        responseDTO.setQuantity(item.getQuantity());
        hateoas.links(responseDTO);

        return responseDTO;
    }


    public OrderItemResponseDTO findOrderItemById(Long id) {
        logger.info("Find a order item!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        var dto = modelMapper.map(entity, OrderItemResponseDTO.class);
        hateoas.links(dto.getProduct());
        hateoas.links(dto);
        return dto;
    }

    public PagedModel<EntityModel<OrderItemResponseDTO>> findAllOrderItemsPage(Pageable pageable) {
        logger.info("Finding all orders items");
        Page<OrderItem> orderItems = repository.findAll(pageable);

        Page<OrderItemResponseDTO> orderItemResponseDTOS = orderItems.map(orderItem -> {
            OrderItemResponseDTO orderItemResponseDTO = modelMapper.map(orderItem, OrderItemResponseDTO.class);
            hateoas.links(orderItemResponseDTO.getProduct());
            hateoas.links(orderItemResponseDTO);
            return orderItemResponseDTO;
        });

        return assembler.toModel(orderItemResponseDTOS);
    }

    public OrderItemResponseDTO updateOrderItemById(Long id, OrderItemRequestDTO item) {
        logger.info("Updating the order item");
        OrderItem itemEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        Product productEntity = productRepository.findById(item.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        itemEntity.setProduct(productEntity);
        itemEntity.setQuantity(item.getQuantity());
        var dto = modelMapper.map(repository.save(itemEntity), OrderItemResponseDTO.class);
        hateoas.links(dto.getProduct());
        hateoas.links(dto);
        return dto;

    }

    public void deleteOrderItemById(Long id) {
        OrderItem item = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        repository.delete(item);
        serviceOrder.updateTotalOrderValue();
    }
}
