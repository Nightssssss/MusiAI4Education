package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

@Data
@TableName(value = "notice")
public class Notice {
    @TableId
    private String nid;     //公告ID

    private String fid;     //发布农场

    @TableField(value = "postTime")
    private LocalDateTime postTime;        //发布时间

    private String title;       //公告标题

    private String content;     //公告内容

    private String picture;     //公告图片
}
