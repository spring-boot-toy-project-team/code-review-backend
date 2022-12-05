package com.codereview.util.file;

import com.codereview.common.dto.file.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {
  FileDto store(MultipartFile multipartFile, String path);

  void remove(String path);
}
