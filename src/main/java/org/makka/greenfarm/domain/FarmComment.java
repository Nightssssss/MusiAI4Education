package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@TableName(value = "farm_comment")
public class FarmComment {

    @TableId
    private String cid;     //评论ID

    @TableField(value = "commentTime")
    private LocalDateTime commentTime;     //评论时间

    private String uid;     //用户ID

    private String fid;     //农场ID

    private String content;     //评论内容

    @TableField(exist = false)
    private String avatar;      //用户头像

    @TableField(exist = false)
    private String nickname;        //用户昵称
}
