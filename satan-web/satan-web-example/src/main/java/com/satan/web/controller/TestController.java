package com.satan.web.controller;

import com.satan.common.utils.JacksonUtil;
import com.satan.common.web.fastjson.FastJson;
import com.satan.common.web.result.JSONResult;
import com.satan.common.web.result.Result;
import com.satan.web.dto.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by huangpin on 17/3/16.
 */
@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Result test(){
        return JSONResult.ok();
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Result test(@FastJson PostDTO postDTO){
        log.info("postDTO:{}", JacksonUtil.objToJson(postDTO));
        return JSONResult.ok();
    }
}
