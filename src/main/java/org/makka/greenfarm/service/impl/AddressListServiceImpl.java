package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.AddressList;
import org.makka.greenfarm.mapper.AddressListMapper;
import org.makka.greenfarm.service.AddressListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressListServiceImpl extends ServiceImpl<AddressListMapper, AddressList> implements AddressListService {
    @Autowired
    private AddressListMapper addressListMapper;

    public List<AddressList> getAddressListByUid(String uid) {
        //获取用户地址列表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid", uid);
        List<AddressList> addressList = addressListMapper.selectList(wrapper);
        return addressList;
    }

    public boolean deleteAddressByAid(String aid) {
        //删除用户地址列表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("aid", aid);
        int result = addressListMapper.delete(wrapper);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addAddress(AddressList addressList) {
        //添加用户地址列表
        int result = addressListMapper.insert(addressList);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateAddress(AddressList addressList) {
        //更新用户地址列表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("aid", addressList.getAid());
        int result = addressListMapper.update(addressList, wrapper);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateDefaultAddress(String aid) {
        //把用户所有地址设为非默认
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("isDefault", 1);
        List<AddressList> addressListList = addressListMapper.selectList(wrapper1);
        for (AddressList addressList : addressListList) {
            addressList.setIsDefault(0);
            addressListMapper.update(addressList, wrapper1);
        }
        //更新用户默认地址
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("aid", aid);
        AddressList addressList = addressListMapper.selectOne(wrapper);
        addressList.setIsDefault(1);
        int result = addressListMapper.update(addressList, wrapper);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
}
