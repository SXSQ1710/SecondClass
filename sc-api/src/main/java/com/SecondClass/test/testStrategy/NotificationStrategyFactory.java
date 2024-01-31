package com.SecondClass.test.testStrategy;


import com.SecondClass.test.testStrategy.strategy.NotificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: NotificationStrategyFactory
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 19:29
 **/

@Component
public class NotificationStrategyFactory {
    private final Map<String, NotificationStrategy> strategyMap = new HashMap<>();

    @Autowired
    public NotificationStrategyFactory(List<NotificationStrategy> strategies) {
        for (NotificationStrategy strategy : strategies) {
            System.out.println(strategy.getType());
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public NotificationStrategy getStrategy(String type) {
        NotificationStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported messageType: " + type);
        }
        return strategy;
    }
}
