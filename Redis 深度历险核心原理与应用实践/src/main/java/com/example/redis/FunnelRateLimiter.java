package com.example.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * 漏斗限流
 * @author jeymingwu
 * @date 2021/4/4 13:29
 */
public class FunnelRateLimiter {

    static class Funnel {
        int capacity; // 漏斗容量
        float leakingRate; // 漏嘴漏水速率
        int leftQuota; // 剩余配额
        long leakingTs; // 漏水时间

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }

        /**
         * 漏嘴流水
         */
        void makeSpace() {
            long nowTs = System.currentTimeMillis();
            long deltaTs = nowTs - this.leakingTs;
            int deltaQuota = (int) (deltaTs * this.leakingRate);

            // 间隔时间太长，整数数字过大溢出
            if (deltaQuota < 0) {
                this.leftQuota = this.capacity;
                this.leakingTs = nowTs;
                return;
            }

            // 腾出空间太小，最小单位为1
            if (deltaQuota < 1) {
                return;
            }

            this.leftQuota += deltaQuota;
            this.leakingTs = nowTs;
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }
        }

        /**
         * 灌水
         * @param quota 灌水容量
         * @return true/false
         */
        boolean watering(int quota) {
            makeSpace();
            if (this.leftQuota >= quota) {
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }

    private Map<String, Funnel> funnels = new HashMap<>();

    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
        String key = String.format("%s:$s", userId, actionKey);
        Funnel funnel = funnels.get(key);
        if (funnel == null) {
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(key, funnel);
        }
        return funnel.watering(1);
    }
}
