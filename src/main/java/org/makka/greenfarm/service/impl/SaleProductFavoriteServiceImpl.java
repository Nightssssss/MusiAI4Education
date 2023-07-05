package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.SaleProductFavorite;
import org.makka.greenfarm.mapper.SaleProductFavoriteMapper;
import org.makka.greenfarm.service.SaleProductFavoriteService;
import org.springframework.stereotype.Service;

@Service
public class SaleProductFavoriteServiceImpl extends ServiceImpl<SaleProductFavoriteMapper, SaleProductFavorite> implements SaleProductFavoriteService {
}
