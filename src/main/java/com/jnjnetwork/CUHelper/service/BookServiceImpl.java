package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.repository.BookRepository;
import com.jnjnetwork.CUHelper.util.U;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;
    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;
    private BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll(Sort.by(Sort.Order.asc("title")));
    }

    @Override
    public List<Book> findAll(Integer page, Model model) {
        if(page == null) page = 1;
        if(page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;

        session.setAttribute("page", page);

        Page<Book> pageWrites = bookRepository.findAll(PageRequest.of(page - 1, pageRows, Sort.by(Sort.Order.asc("title"))));

        long cnt = pageWrites.getTotalElements();
        int totalPage =  pageWrites.getTotalPages();

        if(page > totalPage) page = totalPage;

        int fromRow = (page - 1) * pageRows;

        int startPage = (((page - 1) / writePages) * writePages) + 1;
        int endPage = startPage + writePages - 1;
        if (endPage >= totalPage) endPage = totalPage;

        model.addAttribute("cnt", cnt);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageRows", pageRows);

        model.addAttribute("url", U.getRequest().getRequestURI());
        model.addAttribute("writePages", writePages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        List<Book> list = pageWrites.getContent();
        model.addAttribute("list", list);

        return list;
    }

    @Override
    public void save(Book book) {
        bookRepository.saveAndFlush(book);
    }

    @Override
    public Book findById(Long id) {
        try {
            return bookRepository.findById(id).orElseThrow(RuntimeException::new);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void deleteById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(RuntimeException::new);
        bookRepository.delete(book);
    }

    @Override
    public void edit(Book book) {
        Book b = bookRepository.findById(book.getId()).orElseThrow(RuntimeException::new);
        if (b != null) {
            b.setTitle(book.getTitle());
            b.setSeries(book.getSeries());
            b.setDescription(book.getDescription());
            b.setSummary(book.getSummary());
            b.setBook_level(book.getBook_level());
            b.setAr_point(book.getAr_point());
            b.setWarn(book.getWarn());
            bookRepository.saveAndFlush(b);
        }
    }

    @Override
    public List<Book> findByKeyword(String keyword) {
        String keywordFormatted = keyword.toLowerCase();
        Sort sort = Sort.by(Sort.Order.asc("title"));
        return bookRepository.findByKeywordInColumns(keywordFormatted, sort);
    }
}
