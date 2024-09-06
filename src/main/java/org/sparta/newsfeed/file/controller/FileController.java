package org.sparta.newsfeed.file.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.file.component.FileStore;
import org.sparta.newsfeed.file.dto.InfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    // file 기능 구현 테스트만 해보았고 실제에선 사용안함
    private final FileStore fileStore;

    // file upload
    @PostMapping("/upload")
    public ResponseEntity<ResponseDto<String>> registerUser(@Valid @ModelAttribute InfoDto infoDto ,
                                                            @RequestParam MultipartFile file) throws IOException {
        log.info("email : {}", infoDto.getEmail());
        log.info("password : {}", infoDto.getPassword());
        log.info("name : {}", infoDto.getName());

        fileStore.storeFile(file);

        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "회원가입 완료되었습니다."));
    }
}
