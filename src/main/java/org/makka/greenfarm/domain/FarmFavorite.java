package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "farm_favorite")
public class FarmFavorite {
    @TableId
    private String ffid;

    private String uid;

    private String fid;

    private Date favoriteDate;
}
