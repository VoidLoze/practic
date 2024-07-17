package novikov.nikita.express_delivery.service;

import java.util.List;
import novikov.nikita.express_delivery.domain.Courier;
import novikov.nikita.express_delivery.domain.Delivery;
import novikov.nikita.express_delivery.domain.Order;
import novikov.nikita.express_delivery.model.CourierDTO;
import novikov.nikita.express_delivery.model.DeliveryDTO;
import novikov.nikita.express_delivery.model.OrderDTO;
import novikov.nikita.express_delivery.repos.CourierRepository;
import novikov.nikita.express_delivery.repos.DeliveryRepository;
import novikov.nikita.express_delivery.repos.OrderRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final CourierService courierService;
    private final OrdersService ordersService;

    public DeliveryService(final DeliveryRepository deliveryRepository,
                           final CourierRepository courierRepository, final OrderRepository orderRepository, CourierService courierService, OrdersService ordersService) {
        this.deliveryRepository = deliveryRepository;
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
        this.courierService = courierService;
        this.ordersService = ordersService;
    }

    public List<DeliveryDTO> findAll() {
        final List<Delivery> deliveries = deliveryRepository.findAll(Sort.by("id"));
        return deliveries.stream()
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .toList();
    }

    public DeliveryDTO get(final Long id) {
        return deliveryRepository.findById(id)
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DeliveryDTO deliveryDTO) {
        final Delivery delivery = new Delivery();
        mapToEntity(deliveryDTO, delivery);
        return deliveryRepository.save(delivery).getId();
    }

    public void update(final Long id, final DeliveryDTO deliveryDTO) {
        final Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(deliveryDTO, delivery);
        deliveryRepository.save(delivery);
    }

    public void delete(final Long id) {
        deliveryRepository.deleteById(id);
    }

    private DeliveryDTO mapToDTO(final Delivery delivery, final DeliveryDTO deliveryDTO) {
        deliveryDTO.setId(delivery.getId());
        deliveryDTO.setDataArrived(delivery.getDataArived());
        deliveryDTO.setTaken(delivery.getTaken());
        if (delivery.getCourier() != null) {
            CourierDTO courierDTO = courierService.mapToDTO(delivery.getCourier(), new CourierDTO());
            deliveryDTO.setCourier(courierDTO);
        }
        if (delivery.getOrder() != null) {
            OrderDTO orderDTO = ordersService.mapToDTO(delivery.getOrder(), new OrderDTO());
            deliveryDTO.setOrder(orderDTO);
        }
        return deliveryDTO;
    }

    private Delivery mapToEntity(final DeliveryDTO deliveryDTO, final Delivery delivery) {
        delivery.setDataArived(deliveryDTO.getDataArrived());
        delivery.setTaken(deliveryDTO.getTaken());
        final Courier courier = deliveryDTO.getCourier() == null ? null : courierRepository.findById(deliveryDTO.getCourier().getId())
                .orElseThrow(() -> new NotFoundException("courier not found"));
        delivery.setCourier(courier);
        final Order order = deliveryDTO.getOrder() == null ? null : orderRepository.findById(deliveryDTO.getOrder().getId())
                .orElseThrow(() -> new NotFoundException("order not found"));
        delivery.setOrder(order);
        return delivery;
    }

}
