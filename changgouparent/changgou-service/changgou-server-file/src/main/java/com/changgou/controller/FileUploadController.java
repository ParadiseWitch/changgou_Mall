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
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws IOException, MyException {
        FastDFSFile fastDFSFile = new FastDFSFile(
            file.getOriginalFilename(),
            file.getBytes(),
            StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        FastDFSUtil.upload(fastDFSFile);

        return new Result(true, StatusCode.OK,"上传成功!");
    }
}
