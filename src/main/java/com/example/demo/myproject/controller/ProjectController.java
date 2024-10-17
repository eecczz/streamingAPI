package com.example.demo.myproject.controller;

import com.example.demo.myproject.config.S3Config;
import com.example.demo.myproject.dto.MemoDTO;
import com.example.demo.myproject.dto.S3UploadDTO;
import com.example.demo.myproject.dto.multipart.*;
import com.example.demo.myproject.entity.Memo;
import com.example.demo.myproject.service.MemoService;
import com.example.demo.myproject.service.MemoServiceImpl;
import com.example.demo.myproject.service.S3MultipartUploader;
import com.example.demo.myproject.service.S3Uploader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/myproject")
@RequiredArgsConstructor
public class ProjectController {

    private final MemoService memoService;

    @ResponseBody
    @GetMapping("/list")
    public List<MemoDTO> list(Model model)
    {
        List<MemoDTO> memoDTOList= memoService.memoList();
        model.addAttribute("memoList", memoDTOList);
        return memoDTOList;
    }

    @ResponseBody
    @GetMapping("/read/{mno}")
    public MemoDTO read(@PathVariable("mno") Long mno, Model model)
    {
        MemoDTO memoDTO = memoService.read(mno);
        model.addAttribute("memoDTO", memoDTO);
        return memoDTO;
    }

    @GetMapping("/image")
    public String image(){
        return "/myproject/image-upload";
    }

    @PostMapping("/image-upload")
    public void imageUpload(@RequestPart("memo") MemoDTO memoDTO) {
        System.out.println("memoRepository.saveMemo");
        memoService.keepMemo(memoDTO);
    }
}
