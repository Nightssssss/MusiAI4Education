package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Farm;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface FarmService extends IService<Farm> {

    public List<Farm> getFarmList();

    public Farm getFarmDetail(String farmId);
}
