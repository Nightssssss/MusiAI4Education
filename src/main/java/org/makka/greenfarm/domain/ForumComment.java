package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "forum_comment")
public class ForumComment {
    private String cid;     //评论ID

    @TableField(value = "commentTime")
    private LocalDateTime commentTime;     //评论时间

    private String uid;     //用户ID

    private String fid;    //帖子ID

    private String content;    //评论内容

    @TableField(exist = false)
    private String avatar;      //用户头像

    @TableField(exist = false)
    private String nickname;        //用户昵称
}
