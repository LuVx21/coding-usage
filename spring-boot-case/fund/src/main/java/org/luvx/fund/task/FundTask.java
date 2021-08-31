package org.luvx.fund.task;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.luvx.fund.constant.DataType;
import org.luvx.fund.entity.Fund;
import org.luvx.fund.entity.FundData;
import org.luvx.fund.service.FundDataService;
import org.luvx.fund.service.FundService;
import org.luvx.fund.util.RequestUtils;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.RateLimiter;

import io.vavr.control.Option;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FundTask {
    @Resource
    private FundService fundService;
    @Resource
    private FundDataService fundDataService;

    @Scheduled(cron = "0 50 17 * * 1-5")
    @Scheduled(cron = "0 45 14 * * 1-5")
    public void exec() {
        RateLimiter rateLimiter = RateLimiter.create(0.5);
        List<Fund> funds = fundService.getBaseMapper().selectList(new QueryWrapper<Fund>().orderByAsc("code"));
        funds.forEach(fund -> {
            log.info("fund: {}:{}", fund.getCode(), fund.getName());
            rateLimiter.acquire();
            readRequest(fund.getCode());
        });
        log.info("fund data catch finish...");
    }

    @SneakyThrows
    public void readRequest(String code) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();
        String json = RequestUtils.getJson(code);
        engine.eval(json);
        extracted(engine);
    }

    private void extracted(ScriptEngine engine) throws ScriptException, NoSuchMethodException {
        if (engine instanceof Invocable invocable) {
            String json = (String) invocable.invokeFunction("data");
            Fund fund = JSON.parseObject(json, Fund.class);
            fundService.saveOrUpdate(fund);

            handleWorthOrGrand(fund, DataType.WORTH);
            handleWorthOrGrand(fund, DataType.GRAND);
        }
    }

    private void handleWorthOrGrand(Fund fund, DataType dataType) {
        String code = fund.getCode();
        int type = dataType.getValue();
        FundData one = fundDataService.getOne(
                new QueryWrapper<FundData>().eq("code", code)
                        .eq("type", type)
                        .orderByDesc("x")
                        .last("limit 1")
        );
        Long maxX = Option.of(one).map(FundData::getX).getOrElse(0L);
        List<List<BigDecimal>> data = dataType == DataType.WORTH ? fund.getWorth() : fund.getGrand();
        List<FundData> collect = data.stream()
                .filter(l -> l != null && l.size() >= 2)
                .filter(l -> l.get(0).longValue() > maxX)
                .map(l -> FundData.builder().code(code)
                        .type(type)
                        .x(l.get(0).longValue())
                        .y(l.get(1))
                        .build())
                .collect(Collectors.toList());
        fundDataService.saveBatch(collect);
    }
}
