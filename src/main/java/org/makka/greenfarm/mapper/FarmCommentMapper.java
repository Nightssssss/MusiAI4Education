package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;

import java.util.List;

@Mapper
public interface FarmCommentMapper extends BaseMapper<FarmComment> {
    @Select("select farm_comment.cid,farm_comment.commentTime,farm_comment.uid,farm_comment.fid,farm_comment.content,user.avatar,user.nickname" +
            " from farm_comment,user " +
            "where farm_comment.fid = #{farmId} and farm_comment.uid = user.uid")
    public List<FarmComment> getFarmCommentList(String farmId);
}
