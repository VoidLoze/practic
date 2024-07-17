package novikov.nikita.express_delivery.service;

import java.util.List;

import novikov.nikita.express_delivery.domain.*;
import novikov.nikita.express_delivery.domain.Order;
import novikov.nikita.express_delivery.model.CustomerDTO;
import novikov.nikita.express_delivery.model.OrderDTO;
import novikov.nikita.express_delivery.model.ProductDTO;
import novikov.nikita.express_delivery.repos.CustomerRepository;
import novikov.nikita.express_delivery.repos.DeliveryRepository;
import novikov.nikita.express_delivery.repos.OrderRepository;
import novikov.nikita.express_delivery.repos.ProductRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import novikov.nikita.express_delivery.util.ReferencedWarning;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrdersService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;

    public OrdersService(final OrderRepository orderRepository,
            final ProductRepository productRepository, final CustomerRepository customerRepository,
            final DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(ord -> mapToDTO(ord, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(orders -> mapToDTO(orders, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        addBonusIfNeeded(orderDTO, order);
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    @Transactional
    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        addBonusIfNeeded(orderDTO, order);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }


    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }
    // Добавляем 10% от стоимости товара, если впервые устанавливаем значение даты доставки
    @Transactional
    private void addBonusIfNeeded(@NotNull final OrderDTO orderDTO, @NotNull final Order order) {
        if (orderDTO.getDataGet() == null || order.getDataGet() != null) return;
        Product product;
        if (order.getProduct() == null) {
            product = productRepository.findById(orderDTO.getProduct().getId()).orElseThrow(NotFoundException::new);
        } else {
            product = order.getProduct();
        }

        Customer customer;
        if (order.getCustomer() == null) {
            customer = customerRepository.findById(orderDTO.getCustomer().getId()).orElseThrow(NotFoundException::new);
        } else {
            customer = order.getCustomer();
        }
        customer.setBonusPoints(customer.getBonusPoints() + product.getPrice()*.1);
    }


    public OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setDataGet(order.getDataGet());
        if (order.getProduct() != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getProduct().getId());
            orderDTO.setProduct(productDTO);
        }
        if (order.getCustomer() != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(order.getCustomer().getId());
            orderDTO.setCustomer(customerDTO);
        }
        return orderDTO;
    }

    public void mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setDataGet(orderDTO.getDataGet());
        final Product product = orderDTO.getProduct() == null ? null : productRepository.findById(orderDTO.getProduct().getId())
                .orElseThrow(() -> new NotFoundException("product not found"));
        order.setProduct(product);
        final Customer customer = orderDTO.getCustomer() == null ? null : customerRepository.findById(orderDTO.getCustomer().getId())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        order.setCustomer(customer);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Delivery orderDelivery = deliveryRepository.findFirstByOrder(order);
        if (orderDelivery != null) {
            referencedWarning.setKey("orders.delivery.order.referenced");
            referencedWarning.addParam(orderDelivery.getId());
            return referencedWarning;
        }
        return null;
    }

}
