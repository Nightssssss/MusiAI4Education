package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "forum_comment")
public class ForumComment {
    private String cid;     //评论ID

    private String commentTime;     //评论时间

    private String uid;     //用户ID

    private String fid;    //帖子ID

    private String content;    //评论内容
}
