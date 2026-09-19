package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.jacoco.agent.rt.internal_43f5073.core.runtime.AgentOptions.OutputMode.file;

@RestController
@Slf4j
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);

        try{
            //获取文件名
            String originalFilename=file.getOriginalFilename();
            //获取名字后缀
            String extention=originalFilename.substring(originalFilename.lastIndexOf("."));
            //生成新的文件名
            String newFileName= UUID.randomUUID().toString()+extention;
            //文件的请求路径
            String filePath=aliOssUtil.upload(file.getBytes(),newFileName);
            return Result.success(filePath);

        }catch(Exception e){
            log.error("文件上传失败",e);
        }

        return Result.error("文件上传失败");

    }

}
