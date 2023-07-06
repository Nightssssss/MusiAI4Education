package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
public class FarmController {
    @Autowired
    private FarmService farmService;

    @GetMapping("/list")
    public CommonResponse<List<Farm>> getFarmList() {
        // Return the token to the frontend
        return CommonResponse.creatForSuccess(farmService.getFarmList());
    }

    @GetMapping("/details/{farmId}")
    public CommonResponse<Farm> getFarmDetail(@PathVariable String farmId){
        // Return the token to the frontend
        return CommonResponse.creatForSuccess(farmService.getFarmDetail(farmId));
    }
}
