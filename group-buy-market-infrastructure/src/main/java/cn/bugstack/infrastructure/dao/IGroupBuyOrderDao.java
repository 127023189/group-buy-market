package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface IGroupBuyOrderDao {

    void insert(GroupBuyOrder groupBuyOrder);

    // 更新锁单量
    int updateAddLockCount(String teamId);

    int updateSubtractionLockCount(String teamId);

    GroupBuyOrder queryGroupBuyProgress(String teamId);

    List<GroupBuyOrder> queryGroupBuyTeamByTeamIds(@Param("teamIds") Set<String> teamIds);


    /**
     * 查询团购信息
     * @param teamId
     * @return
     */
    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

    /**
     * 更新完成单量
     * @param teamId
     * @return
     */
    int updateAddCompleteCount(String teamId);

    /**
     * 更新订单状态
     * @param teamId
     * @return
     */
    int updateOrderStatus2COMPLETE(String teamId);

    List<GroupBuyOrder> queryGroupBuyProgressByTeamIds(@Param("teamIds") Set<String> teamIds);

    Integer queryAllTeamCount(@Param("teamIds")Set<String> teamIds);

    Integer queryAllTeamCompleteCount(@Param("teamIds")Set<String> teamIds);

    Integer queryAllUserCount(@Param("teamIds")Set<String> teamIds);

    // 订单退款
    int unpaid2Refund(GroupBuyOrder groupBuyOrderReq);

    int paid2Refund(GroupBuyOrder groupBuyOrderReq);

    int paidTeam2Refund(GroupBuyOrder groupBuyOrderReq);

    int paidTeam2RefundFail(GroupBuyOrder groupBuyOrderReq);
}
