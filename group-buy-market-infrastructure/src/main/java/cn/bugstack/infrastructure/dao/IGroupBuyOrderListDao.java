package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGroupBuyOrderListDao {

    void insert(GroupBuyOrderList groupBuyOrderListReq);

    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);

    Integer queryOrderCountByActivityId(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 更新订单状态
     * @param groupBuyOrderListReq
     * @return
     */
    int updateOrderStatus2COMPLETE(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 查询团购完成订单
     * @param teamId
     * @return
     */
    List<String> queryGroupBuyCompleteOrderOutTradeNoListByTeamId(String teamId);

    /**
     * 查询进行中的拼团订单
     * @param groupBuyOrderListReq
     * @return
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByUserId(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 查询进行中的拼团订单
     * @param groupBuyOrderListReq
     * @return
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByRandom(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 查询进行中的拼团订单
     * @param activityId
     * @return
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByActivityId(Long activityId);

    // 退单
    int unpaid2Refund(GroupBuyOrderList groupBuyOrderListReq);

    // 成团退单
    int paid2Refund(GroupBuyOrderList groupBuyOrderListReq);

    int paidTeam2Refund(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 查询超时未支付订单列表
     * 条件：当前时间不在活动时间范围内、状态为0（初始锁定）、out_trade_time为空
     * @return 超时未支付订单列表，限制10条
     */
    List<GroupBuyOrderList> queryTimeoutUnpaidOrderList();

}
