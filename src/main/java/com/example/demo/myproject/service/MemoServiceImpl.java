package com.example.demo.myproject.service;

import com.example.demo.myproject.dto.MemoDTO;
import com.example.demo.myproject.entity.Memo;
import com.example.demo.myproject.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService{
    // 의존성 주입
    private final MemoRepository memoRepository;

    @Transactional
    public List<MemoDTO> memoList()
    {
        List<Memo> memos = memoRepository.findAll();
        List<MemoDTO> memoDTOList = new ArrayList<>();

        for(Memo memo : memos)
        {
            MemoDTO memoDTO = entityToDto(memo);
            memoDTOList.add(memoDTO);
        }
        return memoDTOList;
    }

    @Override
    public MemoDTO read(Long mno) {

        Optional<Memo> result = memoRepository.findById(mno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    @Transactional
    public Long keepMemo(MemoDTO memoDTO) {
        System.out.println("Diary service saveDiary");
        Memo entity = dtoToEntity(memoDTO);
        Memo savedMemo = memoRepository.save(entity);
        return savedMemo.getMno();
    }
}
