package cn.bugstack.config;

import cn.bugstack.infrastructure.redis.RedissonService;
import cn.bugstack.types.annotations.DCCValue;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "group_buy_market_dcc_";

    private final RedissonClient  RedissonClient ;

    private final Map<String, Object> doccObjMap = new HashMap<>();

    public DCCValueBeanFactory(RedissonClient  redissonClient) {
        this.RedissonClient  = redissonClient ;
    }

    /**
     * 创建DCC监听
     * @param redissonClient
     * @return
     */
    @Bean("dccTopic")
    public RTopic testRedisTopicListener(RedissonClient redissonClient){
        // 订阅,发布来更新
        RTopic topic = redissonClient.getTopic("group_buy_market_dcc");

        topic.addListener(String.class,(charSequence,s) ->{
            String[] split = s.split(Constants.SPLIT);

            // 获取DCC配置
            String attribute = split[0];
            String key = BASE_CONFIG_PATH + attribute;
            String value = split[1];

            // 设置值
            RBucket<String> bucket = redissonClient.getBucket(key);
            boolean exists = bucket.isExists();
            // 不存在则不处理
            if( !exists){
                return ;
            }
            bucket.set(value);

            Object objBean = doccObjMap.get(key);
            if(null ==  objBean){
                return;
            }
            // 获取对象
            Class<?> objBeanClass = objBean.getClass();
            // 检查是否是代理对象
            if(AopUtils.isAopProxy(objBeanClass)){
                // 获取原始对象
                objBeanClass = AopUtils.getTargetClass(objBean);
            }

            // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
            // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
            try {
                Field declaredField = objBeanClass.getDeclaredField(attribute);
                declaredField.setAccessible(true);
                declaredField.set(objBean,value);
                declaredField.setAccessible(false);

                log.info("DCC 节点监听，动态设置值 {} {}", key, value);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });

        return topic;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetBeanClass  = bean.getClass();
        Object targetBeanObject = bean;

        if(AopUtils.isAopProxy(targetBeanClass )){
            // 获取原始对象
            targetBeanClass  = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }
        Field[] fields = targetBeanClass.getDeclaredFields();
        for(Field field : fields){
            // 获取注解,处理自定义注解
            if(!field.isAnnotationPresent(DCCValue.class)){
                continue;
            }
            DCCValue dccValue = field.getAnnotation(DCCValue.class);

            String value = dccValue.value();

            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case 「isSwitch/isSwitch:1」");
            }
            String[] splits = value.split(":");
            String key = BASE_CONFIG_PATH.concat(splits[0]);
            String defaultValue = splits.length == 2 ? splits[1] : null;

            // 设置值
            String setValue = defaultValue;

            try {
                if(StringUtils.isBlank(defaultValue)){
                    throw new RuntimeException("dcc config error " + key + " is not null - 请配置默认值！");
                }
                // Redis 操作，判断配置Key是否存在，不存在则创建，存在则获取最新值
                RBucket<String> bucket = RedissonClient.getBucket(key);

                if(!bucket.isExists()){
                    bucket.set(defaultValue);
                }else{
                    setValue = bucket.get();
                }

                field.setAccessible( true);
                field.set(targetBeanObject, setValue);
                field.setAccessible( false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // 缓存对象
            doccObjMap.put(key, targetBeanObject);
        }
        return bean;
    }
}
