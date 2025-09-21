package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.CustomerDTO;
import com.app.chatlinks.mysql.model.Customer;

public class CustomerMapper extends AbstractMapper<CustomerDTO, Customer>{
    @Override
    public Customer mapToModel(CustomerDTO src) {
        Customer dest = null;
        if(src != null){
            dest = new Customer();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setEmail(src.getEmail());
            dest.setJoinDadte(src.getJoinDadte());
            dest.setOwner(src.getOwner());
        }
        return dest;
    }

    @Override
    public CustomerDTO mapToDTO(Customer src) {
        CustomerDTO dest = null;
        if(src != null){
            dest = new CustomerDTO();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setEmail(src.getEmail());
            dest.setJoinDadte(src.getJoinDadte());
            dest.setOwner(src.getOwner());
            dest.setPaid(src.getPaid());

        }
        return dest;
    }
}
