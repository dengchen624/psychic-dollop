package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * The type Common controller.
 *
 * @Author wild
 * @Date 2022 /4/19 0019 下午 17:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    /**
     * 上传路径配置
     */
    @Value("${reggie.path}")
    private String basePath;

    /**
     * Upload r.
     *
     * @param file the file
     * @return the r
     * @throws IOException the io exception
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //       获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //        使用UUID生产文件名，防止文件名重复覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //        创建目录对象
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //文件转存
        file.transferTo(new File(basePath + fileName));
        return R.success(fileName);
    }


    /**
     * Down load.
     * 文件下载
     *
     * @param name     the name
     * @param response the response
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response) {

        try {
            FileInputStream fs = new FileInputStream(new File(basePath + name));
            ServletOutputStream servletOutputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fs.read(bytes)) != -1) {
                servletOutputStream.write(bytes, 0, len);
                servletOutputStream.flush();
            }

            servletOutputStream.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
