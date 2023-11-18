package mybatisdemo.service.impl;

import mybatisdemo.dao.BookDao;
import mybatisdemo.domain.Book;
import mybatisdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public boolean update(Book book) {
        return bookDao.update(book) > 0;
    }
}
