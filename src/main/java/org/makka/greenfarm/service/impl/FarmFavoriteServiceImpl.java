package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.FarmFavorite;
import org.makka.greenfarm.mapper.FarmFavoriteMapper;
import org.makka.greenfarm.service.FarmFavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FarmFavoriteServiceImpl extends ServiceImpl<FarmFavoriteMapper, FarmFavorite> implements FarmFavoriteService {
}
