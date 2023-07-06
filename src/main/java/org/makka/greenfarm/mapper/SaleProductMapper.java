package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.domain.SaleProductComment;

import java.util.List;

@Mapper
public interface SaleProductMapper extends BaseMapper<SaleProduct> {

    @Select("select sale_product_comment.cid,sale_product_comment.commentTime,sale_product_comment.uid,sale_product_comment.content,user.avatar,user.nickname" +
            " from sale_product_comment,user " +
            "where sale_product_comment.spid = #{productId} and sale_product_comment.uid = user.uid")
    @Results({
            @Result(property = "cid", column = "cid"),
            @Result(property = "commentTime", column = "commentTime"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "content", column = "content"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickname", column = "nickname")
    })
    List<SaleProductComment> getSaleProductComment(String productId);
}

