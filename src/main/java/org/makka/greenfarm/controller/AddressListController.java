package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.AddressList;
import org.makka.greenfarm.service.AddressListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressListController {
    @Autowired
    private AddressListService addressListService;

    @GetMapping("/list")
    public CommonResponse<List<AddressList>> getAddressList() {
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            List<AddressList> addressList = addressListService.getAddressListByUid(uid);
            return CommonResponse.creatForSuccess(addressList);
        }
        else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @DeleteMapping("")
    public CommonResponse<List<AddressList>> deleteAddress(@RequestParam String addressId) {
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if (addressListService.deleteAddressByAid(addressId)){
                List<AddressList> addressList = addressListService.getAddressListByUid(uid);
                return CommonResponse.creatForSuccess(addressList);
            }
            else {
                return CommonResponse.creatForError("删除失败！");
            }
        }
        else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("")
    public CommonResponse<List<AddressList>> addAddress(@RequestParam String name, @RequestParam String phone, @RequestParam String province,
                                                        @RequestParam String city, @RequestParam String detail) {
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            AddressList addressList = new AddressList();
            addressList.setAid(String.valueOf(System.currentTimeMillis()));
            addressList.setUid(uid);
            addressList.setName(name);
            addressList.setPhone(phone);
            addressList.setProvince(province);
            addressList.setCity(city);
            addressList.setDetail(detail);
            addressList.setIsDefault(0);
            if (addressListService.addAddress(addressList)){
                List<AddressList> addressLists = addressListService.getAddressListByUid(uid);
                return CommonResponse.creatForSuccess(addressLists);
            }
            else {
                return CommonResponse.creatForError("添加失败！");
            }
        }
        else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PutMapping("")
    public CommonResponse<List<AddressList>> updateAddress(@RequestParam String addressId, @RequestParam String name, @RequestParam String phone, @RequestParam String province,
                                                           @RequestParam String city, @RequestParam String detail, @RequestParam int isDefault) {
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            AddressList addressList = new AddressList();
            addressList.setAid(addressId);
            addressList.setUid(uid);
            addressList.setName(name);
            addressList.setPhone(phone);
            addressList.setProvince(province);
            addressList.setCity(city);
            addressList.setDetail(detail);
            addressList.setIsDefault(isDefault);
            if (addressListService.updateAddress(addressList)){
                List<AddressList> addressLists = addressListService.getAddressListByUid(uid);
                return CommonResponse.creatForSuccess(addressLists);
            }
            else {
                return CommonResponse.creatForError("修改失败！");
            }
        }
        else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PutMapping("/default")
    public CommonResponse<List<AddressList>> updateDefaultAddress(@RequestParam String addressId) {
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if (addressListService.updateDefaultAddress(addressId)){
                List<AddressList> addressLists = addressListService.getAddressListByUid(uid);
                return CommonResponse.creatForSuccess(addressLists);
            }
            else {
                return CommonResponse.creatForError("修改失败！");
            }
        }
        else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
