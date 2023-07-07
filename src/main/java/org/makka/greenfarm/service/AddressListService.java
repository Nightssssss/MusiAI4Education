package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.AddressList;

import java.util.List;

public interface AddressListService extends IService<AddressList> {
    public List<AddressList> getAddressListByUid(String uid);

    public boolean deleteAddressByAid(String aid);

    public boolean addAddress(AddressList addressList);

    public boolean updateAddress(AddressList addressList);

    public boolean updateDefaultAddress(String aid);
}
