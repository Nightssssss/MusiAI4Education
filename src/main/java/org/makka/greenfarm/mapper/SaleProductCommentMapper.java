package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.SaleProductComment;

import java.util.List;

@Mapper
public interface SaleProductCommentMapper extends BaseMapper<SaleProductComment> {
    @Select("select sale_product_comment.cid,sale_product_comment.commentTime,sale_product_comment.uid,sale_product_comment.spid,sale_product_comment.content,user.avatar,user.nickname" +
            " from sale_product_comment,user " +
            "where sale_product_comment.spid = #{spid} and sale_product_comment.uid = user.uid")
    List<SaleProductComment> getSaleCommentList(String spid);
}
