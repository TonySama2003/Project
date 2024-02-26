package vn.myhome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.myhome.dao.ServiceHotelRepository;
import vn.myhome.entity.ServiceHotel;

import java.util.List;

@Service
public class ServiceHotelImpl implements ServiceHotelService{

    private ServiceHotelRepository serviceHotelRepository;

    @Autowired
    public ServiceHotelImpl(ServiceHotelRepository theServiceHotelRepository){
        this.serviceHotelRepository = theServiceHotelRepository;
    }
    @Override
    public List<ServiceHotel> findAll() {
        return serviceHotelRepository.findAll();
    }

    @Override
    public ServiceHotel findById(int id) {
        return serviceHotelRepository.findById(id);
    }

    @Override
    public Page<ServiceHotel> findAllPage(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,10);
        return serviceHotelRepository.findAll(pageable);
    }

    @Override
    public ServiceHotel findByServiceName(String serviceName) {
        return serviceHotelRepository.findByServiceName(serviceName);
    }

    @Override
    public void save(ServiceHotel serviceHotel) {
        List<ServiceHotel> serviceHotelList = serviceHotelRepository.findAll();
        int id = 0;
        if (!serviceHotelList.isEmpty()){
            id = serviceHotelList.size();
        }
        serviceHotel.setId(id+1);
        serviceHotelRepository.save(serviceHotel);
    }

    @Override
    public void deleteById(int theId) {
        serviceHotelRepository.deleteById(theId);
    }
}
