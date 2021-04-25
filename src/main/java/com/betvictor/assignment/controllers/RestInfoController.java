package com.betvictor.assignment.controllers;

import com.betvictor.assignment.data.AppVersionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestInfoController {
    @Autowired
    AppVersionInfo versionInfo;

    @GetMapping("/version")
    @ResponseBody
    public AppVersionInfo getAppVersion(){
        return versionInfo;
    }
}
