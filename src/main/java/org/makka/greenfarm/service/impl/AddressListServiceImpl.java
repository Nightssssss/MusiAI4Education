package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.AddressList;
import org.makka.greenfarm.mapper.AddressListMapper;
import org.makka.greenfarm.service.AddressListService;
import org.springframework.stereotype.Service;

@Service
public class AddressListServiceImpl extends ServiceImpl<AddressListMapper, AddressList> implements AddressListService {
}
