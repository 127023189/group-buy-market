package cn.bugstack.domain.trade.adapter.repository;

import cn.bugstack.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.valobj.GroupBuyProgressVO;

import java.util.List;

/**
 * @description 交易仓储服务接口
 */
public interface ITradeRepository {

    /**
     * 查询订单信息
     *
     * @param userId
     * @param outTradeNo
     * @return
     */
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    /**
     * 锁定订单信息
     * @param groupBuyOrderAggregate
     * @return
     */
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    /**
     * 查询拼单进度
     * @param teamId
     * @return
     */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    /**
     * 查询活动信息
     * @param activityId
     * @return
     */
    GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId);

    /**
     * 查询用户活动订单数量
     * @param activityId
     * @param userId
     * @return
     */
    Integer queryOrderCountByActivityId(Long activityId, String userId);

    /**
     * 查询拼单信息
     * @param teamId
     * @return
     */
    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    /**
     * 拼单结算
     * @param groupBuyTeamSettlementAggregate
     */
    NotifyTaskEntity settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate);

    boolean isSCBlackIntercept(String source, String channel);
    // 获取待通知任务
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList();
    // 获取待通知任务
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList(String teamId);
    // 更新任务状态
    int updateNotifyTaskStatusSuccess(String teamId);
    // 更新任务状态
    int updateNotifyTaskStatusError(String teamId);

    int updateNotifyTaskStatusRetry(String teamId);

    // 占用拼单库存
    boolean occupyTeamStock(String teamStockKey, String recoveryTeamStockKey, Integer target, Integer validTime);

    // 恢复拼单库存
    void recoveryTeamStock(String recoveryTeamStockKey, Integer validTime);

    NotifyTaskEntity unpaid2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate);

    NotifyTaskEntity paid2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate);

    NotifyTaskEntity paidTeam2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate);

    void refund2AddRecovery(String recoveryTeamStockKey, String orderId);

    List<UserGroupBuyOrderDetailEntity> queryTimeoutUnpaidOrderList();
}
