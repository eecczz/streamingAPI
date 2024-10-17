package com.example.demo.myproject.service;

import com.example.demo.myproject.dto.MemoDTO;
import com.example.demo.myproject.entity.Memo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemoService {

    MemoDTO read(Long mno);
    @Transactional
    List<MemoDTO> memoList();
    @Transactional
    Long keepMemo(MemoDTO memoDTO);

    default Memo dtoToEntity(MemoDTO dto) {
        Memo entity = Memo.builder()
                .mno(dto.getMno())
                .memoText(dto.getMemoText())
                .imageUrl(dto.getImageUrl())
                .videoUrl(dto.getVideoUrl())
                .build();
        return entity;
    }

    default MemoDTO entityToDto(Memo entity){

        MemoDTO dto  = MemoDTO.builder()
                .mno(entity.getMno())
                .memoText(entity.getMemoText())
                .imageUrl(entity.getImageUrl())
                .videoUrl(entity.getVideoUrl())
                .build();

        return dto;
    }
}
