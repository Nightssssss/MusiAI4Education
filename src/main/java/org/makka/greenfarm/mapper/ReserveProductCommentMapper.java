package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ReserveProductComment;

import java.util.List;

@Mapper
public interface ReserveProductCommentMapper extends BaseMapper<ReserveProductComment> {
    @Select("select reserve_product_comment.cid,reserve_product_comment.commentTime,reserve_product_comment.uid,reserve_product_comment.rpid,reserve_product_comment.content,user.avatar,user.nickname" +
            " from reserve_product_comment,user " +
            "where reserve_product_comment.rpid = #{rpid} and reserve_product_comment.uid = user.uid")
    List<ReserveProductComment> getReserveCommentList(String rpid);
}
