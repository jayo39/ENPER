package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    public void add(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void add(Question question, MultipartFile file) {
        upload(question, file);
    }

    @Override
    public void deleteById(Long id, String fileName) {
        delFile(fileName);
        questionRepository.deleteById(id);
    }

    @Override
    public void edit(Long id, Book book, String content) {
        Question question = questionRepository.findById(id).orElseThrow(RuntimeException::new);
        question.setContent(content);
        question.setBook(book);
        questionRepository.save(question);
    }

    private void upload(Question question, MultipartFile file) {
        delFile(file.toString());
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null || originalFileName.isEmpty()) {
            question.setWorksheet(null);
            questionRepository.saveAndFlush(question);
            return;
        }

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

    private void delFile(String fileName) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();
        File f = new File(saveDirectory, fileName);
        f.delete();
    }
}


