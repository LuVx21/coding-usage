package org.luvx.pattern.creational.builder.a;

import lombok.extern.slf4j.Slf4j;

/**
 * 有篷车的建造者
 *
 * @author: Ren, Xie
 */
@Slf4j
public class CarBuilder extends Builder {
    @Override
    public Builder wheels(String wheels) {
        log.info("开始造轮子......{}", wheels);
        car.setWheels(wheels);
        return this;
    }

    @Override
    public Builder engine(String engine) {
        log.info("开始造发动机......{}", engine);
        car.setEngine(engine);
        return this;
    }

    @Override
    public Builder sail(String sail) {
        log.info("开始造车棚......{}", sail);
        car.setSail(sail);
        return this;
    }

    /**
     * 不是敞篷车,即需要建造棚
     *
     * @return
     */
    @Override
    public boolean isConvertible() {
        return false;
    }
}
