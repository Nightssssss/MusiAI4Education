package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "forum")
public class Forum {
    private String fid;     //帖子ID

    private String uid;     //用户ID

    @TableField(value = "postTime")
    private LocalDateTime postTime;    //发帖时间

    private String title;   //帖子标题

    private String content; //帖子内容

    private String picture;     //产品ID

    @TableField(exist = false)
    private String avatar;      //用户头像

    @TableField(exist = false)
    private String nickname;        //用户昵称

    @TableField(exist = false)
    private List<ForumComment> commentList;       //评论数
}
