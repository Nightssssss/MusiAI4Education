package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;

import java.util.List;

@Mapper
public interface ReserveProductMapper extends BaseMapper<ReserveProduct> {
    @Select("select reserve_product_comment.cid,reserve_product_comment.commentTime,reserve_product_comment.uid,reserve_product_comment.content,user.avatar,user.nickname" +
            " from reserve_product_comment,user " +
            "where reserve_product_comment.rpid = #{productId} and reserve_product_comment.uid = user.uid")
    @Results({
            @Result(property = "cid", column = "cid"),
            @Result(property = "commentTime", column = "commentTime"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "content", column = "content"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickname", column = "nickname")
    })
    List<ReserveProductComment> getReserveProductComment(String productId);
}
