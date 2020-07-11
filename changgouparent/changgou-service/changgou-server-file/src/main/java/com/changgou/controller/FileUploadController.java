package com.changgou.controller;

import com.changgou.file.pojo.FastDFSFile;
import com.changgou.file.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.common.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName com.changgou.controller.FileUploadController
 * @Description
 * @Author Maid
 * @Date 2020/7/9 0009 16:30
 * @Version v1.0
 */
@Controller
@RestController(value = "upload")
@CrossOrigin
public class FileUploadController {

    @PostMapping
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile = new FastDFSFile(
            file.getOriginalFilename(),
            file.getBytes(),
            StringUtils.getFilenameExtension(file.getOriginalFilename())
        );

        //[文件上传存储的Storage的名字group1，文件存储到Storage的文件名]
        String[] uploads = FastDFSUtil.upload(fastDFSFile);
        //拼切Url
        String url = FastDFSUtil.getTrackerInfo() + "/" + uploads[0] + "/" + uploads[1];
        return new Result(true, StatusCode.OK,"上传成功!",url);
    }
}
