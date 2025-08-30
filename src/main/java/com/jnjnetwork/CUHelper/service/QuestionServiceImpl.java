package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Value("${app.upload.path}")
    private String uploadDir;

    QuestionRepository questionRepository;

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public List<Question> findAllSortedByBookArPoint() {
        return questionRepository.findAllSortedByBookArPoint();
    }

    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Question findByBookId(Long book_id) {
        return questionRepository.findByBookId(book_id);
    }

    @Override
    @Transactional
    public void add(Question question) {
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void add(Question question, MultipartFile file) {
        upload(question, file);
    }

    @Override
    @Transactional
    public void deleteById(Long id, String fileName) {
        delFile(fileName);
        questionRepository.deleteById(id);
    }

    private void upload(Question question, MultipartFile file) {
        if(file == null) {
            delFile(question.getWorksheet());
            question.setWorksheet(null);
            questionRepository.saveAndFlush(question);
        } else {
            String originalFileName = file.getOriginalFilename();
            delFile(question.getWorksheet());
            question.setWorksheet(file.toString());

            String sourceName = StringUtils.cleanPath(originalFileName);
            String fileName = sourceName;

            File file1 = new File(uploadDir + File.separator + sourceName);
            if(file1.exists()) {
                int pos = fileName.lastIndexOf(".");
                if(pos > -1) {
                    String name = fileName.substring(0, pos);
                    String ext = fileName.substring(pos + 1);
                    fileName = name + "_" + System.currentTimeMillis() + "." + ext;
                } else {
                    fileName += "_" + System.currentTimeMillis();
                }
            }
            question.setWorksheet(fileName);
            Path copyOfLocation = Paths.get(new File(uploadDir + File.separator + fileName).getAbsolutePath());
            try {
                Files.copy(
                        file.getInputStream(),
                        copyOfLocation,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch(IOException error) {
                error.printStackTrace();
            }

            try {
                questionRepository.saveAndFlush(question);
            } catch(Exception error) {
                error.printStackTrace();
            }
        }

    }

    private void delFile(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return;
        }
        String saveDirectory = new File(uploadDir).getAbsolutePath();
        File f = new File(saveDirectory, fileName);
        if(f.exists()) {
            f.delete();
        }
    }
}


