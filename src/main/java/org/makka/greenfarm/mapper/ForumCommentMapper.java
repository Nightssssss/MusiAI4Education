package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ForumComment;

import javax.xml.stream.events.Comment;
import java.util.List;

@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {
    @Select("select forum_comment.cid,forum_comment.commentTime,forum_comment.uid,forum_comment.fid,forum_comment.content,user.avatar,user.nickname" +
            " from forum_comment,user " +
            "where forum_comment.fid = #{forumId} and forum_comment.uid = user.uid")
    public List<ForumComment> getForumCommentList(String forumId);
}
