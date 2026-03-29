package cn.bugstack.domain.activity.service.discount.impI;

import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.discount.AbstractDiscountCalculateService;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 无门槛直减
 */
@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType());

        String marketExpr = groupBuyDiscount.getMarketExpr();

        //无门槛
        BigDecimal deductionPrice = originalPrice.subtract(new BigDecimal(marketExpr));
        if(deductionPrice.compareTo(BigDecimal.ZERO) <= 0){
            return new BigDecimal(Constants.MIN_VAL);
        }
        return deductionPrice;
    }
}
