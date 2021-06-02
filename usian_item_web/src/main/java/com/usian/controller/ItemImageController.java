package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Title: ItemImageController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/12 14:49
 */
@RestController
@RequestMapping("file")
public class ItemImageController {

    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping("upload")
    public Result upload(MultipartFile file){
        //1. 文件格式的校验
        // 文件大小的校验

        String extFileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));//获取后缀名

        try {
            //2 上传文件
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extFileName, null);

            //文件路径返回给浏览器端？   上传文件的图片的路径是否需要保存到数据库？ 何时触发保存到数据库
           return Result.ok("http://image.usian.com/"+ storePath.getFullPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.error("上传失败了！！");

    }
}
