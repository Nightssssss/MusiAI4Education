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
}
