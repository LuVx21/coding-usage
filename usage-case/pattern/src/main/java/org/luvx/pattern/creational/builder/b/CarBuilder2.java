package org.luvx.pattern.creational.builder.b;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.creational.builder.Car;

@Slf4j
public class CarBuilder2 extends Builder1 {
    @Override
    public Builder1 wheels(String wheels) {
        log.info("开始造轮子......{}", wheels);
        car.setWheels(wheels);
        return this;
    }

    @Override
    public Builder1 engine(String engine) {
        log.info("开始造发动机......{}", engine);
        car.setEngine(engine);
        return this;
    }

    @Override
    public Car build() {
        return car;
    }
}
