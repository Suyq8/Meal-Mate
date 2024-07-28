package com.mealmate.controller.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mealmate.constant.MessageConstant;
import com.mealmate.result.Result;
import com.mealmate.utils.GCPUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Common Controller
 */
@RestController("AdminCommonController")
@RequestMapping("/admin/common")
@Tag(name = "Common Controller")
@Slf4j
public class CommonController {

    @Autowired
    private GCPUtil gcpUtil;

    
    /**
     * upload file
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "upload file")
    public Result<String> upload(MultipartFile file) {
        log.info("upload file: {}", file);

        try {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            String fileUrl = gcpUtil.upload(file.getBytes(), newFileName);

            return Result.success(fileUrl);
        } catch (Exception e) {
            log.error("upload file error: {}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
