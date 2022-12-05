package com.codereview.util.file;

import com.codereview.common.dto.file.FileDto;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {
  @Value("${save-path}")
  private String savePath;
  @Value("${db-path}")
  private String dbPath;
  private static final List<String> FILE_FORMAT = List.of(".jpg", ".png", ".jpeg");

  @Override
  public FileDto store(MultipartFile multipartFile, String destination) {
    try {
      makeFolder(destination);
      if(!multipartFile.isEmpty()) {
        return saveFile(multipartFile, destination);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new BusinessLogicException(ExceptionCode.FILE_UPLOAD_FAIL);
    }
    return null;
  }

  @Override
  public void remove(String path) {
    File file = new File(path);
    if(!file.delete()) {
      log.error("FILE IS NOT EXIST");
    }
  }

  private File makeFolder(String destination) {
    StringBuilder uploadPath = new StringBuilder(savePath + destination);
    File file = new File(uploadPath.toString());
    if(!file.exists()) {
      if(!file.mkdirs()) {
        log.error("FOLDER MAKE FAIL");
        throw new BusinessLogicException(ExceptionCode.FILE_UPLOAD_FAIL);
      }
    }
    return file;
  }

  private FileDto saveFile(MultipartFile multipartFile, String destination) throws IOException {
    StringBuffer fileName = new StringBuffer(Objects.requireNonNull(multipartFile.getOriginalFilename()).toLowerCase());
    int index = fileName.lastIndexOf(".");
    String format = fileName.substring(index);
    if(!FILE_FORMAT.contains(format))
      throw new BusinessLogicException(ExceptionCode.INVALID_FILE_FORMAT);
    fileName.replace(0, index, UUID.randomUUID().toString());
    Path path = Paths.get(new File(savePath + destination) + "/" + fileName);
    multipartFile.transferTo(path);
    return FileDto.builder()
      .uri(dbPath + destination + "/" + fileName)
      .path(path.toString())
      .build();
  }
}
