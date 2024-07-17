package novikov.nikita.express_delivery.service;

import java.util.List;

import novikov.nikita.express_delivery.domain.Courier;
import novikov.nikita.express_delivery.domain.Delivery;
import novikov.nikita.express_delivery.model.CourierDTO;
import novikov.nikita.express_delivery.repos.CourierRepository;
import novikov.nikita.express_delivery.repos.DeliveryRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import novikov.nikita.express_delivery.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CourierService {

    private final CourierRepository courierRepository;
    private final DeliveryRepository deliveryRepository;

    public CourierService(final CourierRepository courierRepository, final DeliveryRepository deliveryRepository) {
        this.courierRepository = courierRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<CourierDTO> findAll() {
        final List<Courier> couriers = courierRepository.findAll(Sort.by("id"));
        return couriers.stream().map(courier -> mapToDTO(courier, new CourierDTO())).toList();
    }

    public CourierDTO get(final Long id) {
        return courierRepository.findById(id).map(courier -> mapToDTO(courier, new CourierDTO())).orElseThrow(NotFoundException::new);
    }

    public Long create(final CourierDTO courierDTO) {
        final Courier courier = new Courier();
        mapToEntity(courierDTO, courier);
        return courierRepository.save(courier).getId();
    }

    public void update(final Long id, final CourierDTO courierDTO) {
        final Courier courier = courierRepository.findById(id).orElseThrow(NotFoundException::new);
        mapToEntity(courierDTO, courier);
        courierRepository.save(courier);
    }

    public void delete(final Long id) {
        courierRepository.deleteById(id);
    }


    public List<CourierDTO> findTopCouriersByDeliveredOrders() {
        return courierRepository.findTopCouriersByDeliveredOrders().stream()
                .map(courier -> mapToDTO(courier, new CourierDTO())).toList();
    }


    public CourierDTO mapToDTO(final Courier courier, final CourierDTO courierDTO) {
        courierDTO.setId(courier.getId());
        courierDTO.setFirstName(courier.getFirstName());
        courierDTO.setLastName(courier.getLastName());
        courierDTO.setPhoneNumber(courier.getPhoneNumber());
        courierDTO.setDeliveryType(courier.getDeliveryType());
        return courierDTO;
    }

    public Courier mapToEntity(final CourierDTO courierDTO, final Courier courier) {
        courier.setFirstName(courierDTO.getFirstName());
        courier.setLastName(courierDTO.getLastName());
        courier.setPhoneNumber(courierDTO.getPhoneNumber());
        courier.setDeliveryType(courierDTO.getDeliveryType());
        return courier;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Courier courier = courierRepository.findById(id).orElseThrow(NotFoundException::new);
        final Delivery courierDelivery = deliveryRepository.findFirstByCourier(courier);
        if (courierDelivery != null) {
            referencedWarning.setKey("courier.delivery.courier.referenced");
            referencedWarning.addParam(courierDelivery.getId());
            return referencedWarning;
        }
        return null;
    }

}
