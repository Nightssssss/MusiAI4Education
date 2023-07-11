package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    @Select("select notice.*,farm.name as farmName from notice,farm where notice.nid = #{nid} and notice.fid = farm.fid")
    public Notice getNoticeByNid(String nid);

    @Select("select notice.*,farm.name as farmName from notice,farm where notice.fid = farm.fid order by notice.postTime desc")
    public List<Notice> getNoticeList();
}
