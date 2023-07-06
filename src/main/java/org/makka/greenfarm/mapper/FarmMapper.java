package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.domain.FarmComment;

import javax.xml.stream.events.Comment;
import java.util.List;

@Mapper
public interface FarmMapper extends BaseMapper<Farm> {
    @Select("select farm_comment.cid,farm_comment.commentTime,farm_comment.uid,farm_comment.content,user.avatar,user.nickname" +
            " from farm_comment,user " +
            "where farm_comment.fid = #{farmId} and farm_comment.uid = user.uid")
    @Results({
            @Result(property = "cid", column = "cid"),
            @Result(property = "commentTime", column = "commentTime"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "content", column = "content"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickname", column = "nickname")
    })
    public List<FarmComment> getFarmComment(String farmId);
}
