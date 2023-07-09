package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.domain.ForumComment;

import java.util.List;

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {
    @Select("select forum_comment.*,user.avatar,user.nickname" +
            " from forum_comment,user " +
            "where forum_comment.fid = #{forumId} and forum_comment.uid = user.uid")
    public List<ForumComment> getForumComment(String forumId);

    @Select("select forum.*,user.avatar,user.nickname" +
            " from forum,user " +
            "where forum.fid = #{forumId} and forum.uid = user.uid")
    public Forum getForum(String forumId);

    @Select("select forum.*,user.avatar,user.nickname" +
            " from forum,user " +
            "where forum.uid = user.uid")
    public List<Forum> getForumList();
}
