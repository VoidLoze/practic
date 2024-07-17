package novikov.nikita.express_delivery.service;

import java.util.List;

import novikov.nikita.express_delivery.domain.Customer;
import novikov.nikita.express_delivery.domain.Order;
import novikov.nikita.express_delivery.model.CustomerDTO;
import novikov.nikita.express_delivery.repos.CustomerRepository;
import novikov.nikita.express_delivery.repos.OrderRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import novikov.nikita.express_delivery.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getId();
    }

    public void update(final Long id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setDistrict(customer.getDistrict());
        customerDTO.setStreet(customer.getStreet());
        customerDTO.setHouse(customer.getHouse());
        customerDTO.setAppartment(customer.getAppartment());
        customerDTO.setBonusPoints(customer.getBonusPoints());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setDistrict(customerDTO.getDistrict());
        customer.setStreet(customerDTO.getStreet());
        customer.setHouse(customerDTO.getHouse());
        customer.setAppartment(customerDTO.getAppartment());
        customer.setBonusPoints(customerDTO.getBonusPoints());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order customerOrder = orderRepository.findFirstByCustomer(customer);
        if (customerOrder != null) {
            referencedWarning.setKey("customer.orders.customer.referenced");
            referencedWarning.addParam(customerOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
